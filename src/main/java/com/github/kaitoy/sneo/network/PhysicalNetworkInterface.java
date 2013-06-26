/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.pcap4j.core.PacketListener;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.MacAddress;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import com.github.kaitoy.sneo.network.protocol.EthernetHelper;

public final class PhysicalNetworkInterface
extends PacketReceiver implements NetworkInterface {

  private static final LogAdapter logger
    = LogFactory.getLogger(PhysicalNetworkInterface.class);

  private final MacAddress macAddress;
  private final InetAddress ipAddress;
  private final InetAddress subnetMask;
  private final PacketListener host;
  private final List<PacketListener> users
    = Collections.synchronizedList(new ArrayList<PacketListener>());

  private volatile BlockingQueue<Packet> sendPacketQueue = null;

  public PhysicalNetworkInterface(
    String name,
    MacAddress macAddress,
    InetAddress ipAddress,
    InetAddress subnetMask,
    PacketListener host
  ) {
    super(name);
    this.macAddress = macAddress;
    this.ipAddress = ipAddress;
    this.subnetMask = subnetMask;
    this.host = host;
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

  void setSendPacketQueue(BlockingQueue<Packet> sendPacketQueue) {
    this.sendPacketQueue = sendPacketQueue;
  }

  public void sendPacket(Packet packet) {
    BlockingQueue<Packet> q = sendPacketQueue;
    if (q == null) {
      logger.warn("Not yet be connected, dropped a packet: " + packet);
      return;
    }

    boolean offered = q.offer(packet);
    if (offered) {
      if (logger.isDebugEnabled()) {
        logger.debug("Sent a packet: " + packet);
      }
    }
    else {
      logger.error("Couldn't send a packet: " + packet);
    }
  }

  @Override
  protected void process(Packet packet) {
    if (logger.isDebugEnabled()) {
      logger.debug("Received a packet: " + packet);
    }

//  no need
//  if (!packet.isValid()) {
//    logger.warn("Dropped an invalid packet: " + packet);
//    return;
//  }

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

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName()).append("{")
      .append("name[").append(getName()).append("] ")
      .append("macAddress[").append(macAddress).append("]")
      .append("ipAddress[").append(ipAddress).append("]")
      .append("subnetMask[").append(subnetMask).append("]")
      .append("}");
    return sb.toString();
  }

}
