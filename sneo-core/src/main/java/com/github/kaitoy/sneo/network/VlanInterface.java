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
import com.github.kaitoy.sneo.network.protocol.Dot1qVlanTagHelper;
import com.github.kaitoy.sneo.network.protocol.EthernetHelper;

public class VlanInterface implements NetworkInterface {

  private static final LogAdapter logger
    = LogFactory.getLogger(VlanInterface.class);

  private final String name;
  private final MacAddress macAddress;
  private final List<NifIpAddress> ipAddresses
    = Collections.synchronizedList(new ArrayList<NifIpAddress>());
  private final int vid;
  private final Map<String, NetworkInterface> memberNifs
    = new ConcurrentHashMap<String, NetworkInterface>();
  private final PacketListener host;
  private final List<PacketListener> users
    = Collections.synchronizedList(new ArrayList<PacketListener>());

  private volatile boolean running = false;

  public VlanInterface(
    String name,
    MacAddress macAddress,
    int vid,
    PacketListener host
  ) {
    this.name = name;
    this.macAddress = macAddress;
    this.vid = vid;
    this.host = host;
  }

  public String getName() {
    return name;
  }

  public MacAddress getMacAddress() {
    return macAddress;
  }

  public boolean isTrunk() { return false; }

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

  public void addNif(String ifName, NetworkInterface nif) {
    if (memberNifs.containsKey(ifName)) {
      return;
    }

    nif.addUser(new PacketListenerImpl(ifName));
    memberNifs.put(ifName, nif);
  }

  public void sendPacket(Packet packet) throws SendPacketException {
    if (!running) {
      if (logger.isDebugEnabled()) {
        logger.warn("Not running. Can't send a packet: " + packet);
        throw new SendPacketException();
      }
    }

    Packet taggedPacket = null;
    Packet untaggedPacket = null;
    for (NetworkInterface nif: memberNifs.values()) {
      Packet sendingPacket;
      if (nif.isTrunk()) {
        if (taggedPacket == null) {
          taggedPacket = Dot1qVlanTagHelper.tag(packet, vid);
        }
        sendingPacket = taggedPacket;
      }
      else {
        if (untaggedPacket == null) {
          untaggedPacket = Dot1qVlanTagHelper.untag(packet);
        }
        sendingPacket = untaggedPacket;
      }

      try {
        nif.sendPacket(sendingPacket);
        if (logger.isDebugEnabled()) {
          StringBuilder sb = new StringBuilder();
          sb.append("Sent a packet via ")
            .append(nif.getName())
            .append(": ")
            .append(sendingPacket);
          logger.debug(sb.toString());
        }
      } catch (SendPacketException e) {
        logger.error("Failed to send a packet: " + sendingPacket, e);
      }
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

      if (memberNifs.get(ifName).isTrunk()) {
        if (!Dot1qVlanTagHelper.isTagged(packet, vid)) {
          if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Dropped a packet not tagged with VLAN ")
              .append(vid)
              .append(": ")
              .append(packet);
            logger.debug(sb.toString());
          }
          return;
        }
      }

      // forwarding
      Packet taggedPacket = null;
      Packet untaggedPacket = null;
      for (NetworkInterface nif: memberNifs.values()) {
        if (ifName.equals(nif.getName())) {
          continue;
        }

        Packet sendingPacket;
        if (nif.isTrunk()) {
          if (taggedPacket == null) {
            taggedPacket = Dot1qVlanTagHelper.tag(packet, vid);
          }
          sendingPacket = taggedPacket;
        }
        else {
          if (untaggedPacket == null) {
            untaggedPacket = Dot1qVlanTagHelper.untag(packet);
          }
          sendingPacket = untaggedPacket;
        }

        try {
          nif.sendPacket(sendingPacket);
          if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Forwarded a packet from ")
              .append(ifName)
              .append(" to ")
              .append(nif.getName())
              .append(": ")
              .append(sendingPacket);
            logger.debug(sb.toString());
          }
        } catch (SendPacketException e) {
          logger.error("Failed to forward a packet: " + sendingPacket, e);
        }
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
