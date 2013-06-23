/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.network;

import java.net.InetAddress;
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
import com.github.kaitoy.network.protocol.EthernetHelper;

public class LagInterface implements NetworkInterface {

  private static final LogAdapter logger
    = LogFactory.getLogger(LagInterface.class);

  private final String name;
  private final MacAddress macAddress;
  private final InetAddress ipAddress;
  private final InetAddress subnetMask;
  private final int lagId;
  private final Map<String, NetworkInterface> nifs
    = new ConcurrentHashMap<String, NetworkInterface>();
  private final PacketListener host;
  private final List<PacketListener> users
    = Collections.synchronizedList(new ArrayList<PacketListener>());

  private volatile boolean running = false;

  public LagInterface(
    String name,
    MacAddress macAddress,
    InetAddress ipAddress,
    InetAddress subnetMask,
    int lagId,
    PacketListener packetListener
  ) {
    this.name = name;
    this.macAddress = macAddress;
    this.ipAddress = ipAddress;
    this.subnetMask = subnetMask;
    this.lagId = lagId;
    this.host = packetListener;
  }

  public String getName() {
    return name;
  }

  public MacAddress getMacAddress() {
    return macAddress;
  }

  public InetAddress getIpAddress() {
    return ipAddress;
  }

  public InetAddress getSubnetMask() {
    return subnetMask;
  }

  public void addUser(PacketListener user) {
    users.add(user);
  }

  public int getVid() {
    return lagId;
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

  public void addNif(String ifName, NetworkInterface nif) {
    nif.addUser(new PacketListenerImpl(ifName));
    nifs.put(ifName, nif);
  }

  public void sendPacket(Packet packet) {
    if (!running) {
      if (logger.isDebugEnabled()) {
        logger.warn("Not running. Can't send a packet: " + packet);
      }
    }

    for (NetworkInterface nif: nifs.values()) {
      if (nif.isRunning()) {
        if (logger.isDebugEnabled()) {
          StringBuilder sb = new StringBuilder();
          sb.append("Send a packet via ")
            .append(nif.getName())
            .append(": ")
            .append(packet);
          logger.debug(sb.toString());
        }
        nif.sendPacket(packet);
        break;
      }
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

//  @Override String toString() {
//
//  }

}
