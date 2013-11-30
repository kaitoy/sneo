/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
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

public class LagInterface implements NetworkInterface {

  private static final LogAdapter logger
    = LogFactory.getLogger(LagInterface.class);

  private final String name;
  private final MacAddress macAddress;
  private final List<NifIpAddress> ipAddresses
    = Collections.synchronizedList(new ArrayList<NifIpAddress>());
  private final int channelGroupNumber;
  private final Map<String, PhysicalNetworkInterface> aggregatedNifs
    = new ConcurrentHashMap<String, PhysicalNetworkInterface>();
  private final PacketListener host;
  private final List<PacketListener> users
    = Collections.synchronizedList(new ArrayList<PacketListener>());

  private volatile boolean running = false;

  public LagInterface(
    String name,
    MacAddress macAddress,
    int channelGroupNumber,
    PacketListener host
  ) {
    this.name = name;
    this.macAddress = macAddress;
    this.channelGroupNumber = channelGroupNumber;
    this.host = host;
  }

  public String getName() {
    return name;
  }

  public MacAddress getMacAddress() {
    return macAddress;
  }

  public boolean isTrunk() {
    for (PhysicalNetworkInterface pnif: aggregatedNifs.values()) {
      return pnif.isTrunk();
    }
    return false;
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

  public int getChannelGroupNumber() {
    return channelGroupNumber;
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

  public void addNif(String ifName, PhysicalNetworkInterface nif) {
    nif.addUser(new PacketListenerImpl(ifName));
    aggregatedNifs.put(ifName, nif);
  }

  public void sendPacket(Packet packet) throws SendPacketException {
    if (!running) {
      if (logger.isDebugEnabled()) {
        logger.warn("Not running. Can't send a packet: " + packet);
        throw new SendPacketException();
      }
    }

    boolean packetIsSent = false;
    for (NetworkInterface nif: aggregatedNifs.values()) {
      if (nif.isRunning()) {
        if (logger.isDebugEnabled()) {
          StringBuilder sb = new StringBuilder();
          sb.append("Sending a packet via ")
            .append(nif.getName())
            .append(": ")
            .append(packet);
          logger.debug(sb.toString());
        }

        try {
          nif.sendPacket(packet);
          packetIsSent = true;
          break;
        } catch (SendPacketException e) {
          logger.error("Failed to send a packet: " + packet, e);
        }
      }
    }

    if (packetIsSent) {
      logger.debug("Succeeded in sending the packet.");
    }
    else {
      throw new SendPacketException();
    }
  }

  private final class PacketListenerImpl implements PacketListener {

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
