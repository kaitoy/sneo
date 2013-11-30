/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network;

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
  private final boolean trunk;
  private final List<NifIpAddress> ipAddresses
    = Collections.synchronizedList(new ArrayList<NifIpAddress>());
  private final PacketListener host;
  private final List<PacketListener> users
    = Collections.synchronizedList(new ArrayList<PacketListener>());

  private volatile BlockingQueue<PacketContainer> sendPacketQueue = null;

  public PhysicalNetworkInterface(
    String name,
    MacAddress macAddress,
    boolean trunk,
    PacketListener host
  ) {
    super(name);
    this.macAddress = macAddress;
    this.trunk = trunk;
    this.host = host;
  }

  public MacAddress getMacAddress() {
    return macAddress;
  }

  public boolean isTrunk() {
    return trunk;
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

  void setSendPacketQueue(BlockingQueue<PacketContainer> sendPacketQueue) {
    this.sendPacketQueue = sendPacketQueue;
  }

  public void sendPacket(Packet packet) throws SendPacketException {
    if (!isRunning()) {
      if (logger.isDebugEnabled()) {
        logger.warn("Not running. Can't send a packet: " + packet);
        throw new SendPacketException();
      }
    }

    BlockingQueue<PacketContainer> q = sendPacketQueue;
    if (q == null) {
      logger.warn("Not yet be connected. Dropped a packet: " + packet);
      throw new SendPacketException();
    }

    boolean offered = q.offer(new PacketContainer(packet, this));
    if (offered) {
      if (logger.isDebugEnabled()) {
        logger.debug("Sent a packet: " + packet);
      }
    }
    else {
      logger.error("Couldn't send a packet: " + packet);
      throw new SendPacketException();
    }
  }

  @Override
  protected void process(PacketContainer pc) {
    Packet packet = pc.getPacket();

    if (logger.isDebugEnabled()) {
      StringBuilder sb = new StringBuilder();
      sb.append("Received a packet from ");
      if (pc.getSrc() != null) {
        sb.append(pc.getSrc().getName());
      }
      else {
        sb.append("a L2 connection");
      }
      sb.append(": ")
        .append(packet);
      logger.debug(sb.toString());
    }

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
      .append("macAddress[").append(macAddress).append("]");
    for (NifIpAddress ipAddr: ipAddresses) {
      sb.append("ipAddress[").append(ipAddr).append("]");
    }
    sb.append("}");
    return sb.toString();
  }

}
