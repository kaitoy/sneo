/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.pcap4j.core.PacketListener;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.MacAddress;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import com.github.kaitoy.sneo.network.protocol.EthernetHelper;

public class VlanInterface implements NetworkInterface {

  private static final LogAdapter logger
    = LogFactory.getLogger(VlanInterface.class);

  private final String name;
  private final MacAddress macAddress;
  private final List<NifIpAddress> ipAddresses
    = Collections.synchronizedList(new ArrayList<NifIpAddress>());
  private final int vid;
  private final Map<NetworkInterface, Boolean> tagged
    = new ConcurrentHashMap<NetworkInterface, Boolean>();
  private final Map<String, NetworkInterface> nifs
    = new ConcurrentHashMap<String, NetworkInterface>();
  private final PacketListener host;
  private final List<PacketListener> users
    = Collections.synchronizedList(new ArrayList<PacketListener>());

  private volatile boolean running = false;

  public VlanInterface(
    String name,
    MacAddress macAddress,
    int vid,
    PacketListener packetListener
  ) {
    this.name = name;
    this.macAddress = macAddress;
    this.vid = vid;
    this.host = packetListener;
  }

  public String getName() {
    return name;
  }

  public MacAddress getMacAddress() {
    return macAddress;
  }

  public List<NifIpAddress> getIpAddresses() {
    return new ArrayList<NifIpAddress>(ipAddresses);
  }

  public void addIpAddress(NifIpAddress addr) {
    ipAddresses.add(addr);
  }

  public void addUser(PacketListener user) {
    users.add(user);
  }

  public int getVid() {
    return vid;
  }

  public void start() {
    running = true;
  }

  public void stop() {
    running = false;
  }

  public void shutdown() {
    stop();
  }

  public boolean isRunning() {
    return running;
  }

  public void addNif(String ifName, NetworkInterface nif, Boolean tagged) {
    nif.addUser(new PacketListenerImpl(ifName));
    nifs.put(ifName, nif);
    this.tagged.put(nif, tagged);
  }

  public void sendPacket(Packet packet) {
    if (!running) {
      if (logger.isDebugEnabled()) {
        logger.warn("Not running. Can't send a packet: " + packet);
      }
    }

    for (NetworkInterface nif: nifs.values()) {
      if (tagged.get(nif)) {

      }

      if (logger.isDebugEnabled()) {
        StringBuilder sb = new StringBuilder();
        sb.append("Send a packet via ")
          .append(nif.getName())
          .append(": ")
          .append(packet);
        logger.debug(sb.toString());
      }
      nif.sendPacket(packet);
    }
  }

  private
  final class PacketListenerImpl implements PacketListener {

    private final String ifName;

    private PacketListenerImpl(String ifName) {
      this.ifName = ifName;
    }

    public void gotPacket(Packet packet) {
      for (PacketListener user: users) {
        user.gotPacket(packet);
      }

      // forward
      for (String name: nifs.keySet()) {
        if (ifName.equals(name)) {
          continue;
        }

        if (logger.isDebugEnabled()) {
          StringBuilder sb = new StringBuilder();
          sb.append("Forward a packet from ")
            .append(ifName)
            .append(" to ")
            .append(name)
            .append(": ")
            .append(packet);
          logger.debug(sb.toString());
        }
        nifs.get(name).sendPacket(packet);
      }

      if (EthernetHelper.matchesDestination(packet, macAddress)) {
        host.gotPacket(packet);
      }
      else {
        if (logger.isDebugEnabled()) {
          StringBuilder sb = new StringBuilder();
          sb.append("Dropped a packet not to me(")
            .append(macAddress)
            .append("): ")
            .append(packet);
          logger.debug(sb.toString());
        }
      }
    }

  }

}
