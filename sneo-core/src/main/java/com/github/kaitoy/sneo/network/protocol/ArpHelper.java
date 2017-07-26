/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2017  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network.protocol;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.ArpHardwareType;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.util.MacAddress;
import com.github.kaitoy.sneo.network.NetworkInterface;
import com.github.kaitoy.sneo.network.NifIpAddress;
import com.github.kaitoy.sneo.network.Node;
import com.github.kaitoy.sneo.network.SendPacketException;

public final class ArpHelper extends NeighborDiscoveryHelper {

  private static ArpRequestSender reqSender = new ArpRequestSender();

  private ArpHelper() { throw new AssertionError(); }

  public static boolean matchesDestination(Packet packet, InetAddress addr) {
    ArpPacket arpPacket = packet.get(ArpPacket.class);
    if (arpPacket == null) {
      throw new IllegalArgumentException(packet.toString());
    }

    return addr.equals(arpPacket.getHeader().getDstProtocolAddr());
  }

  public static ArpCache newArpCache(long cacheLife) {
    return new ArpCache(cacheLife);
  }

  public static MacAddress resolveVirtualAddress(InetAddress ipAddress) {
    return NeighborDiscoveryHelper.resolveVirtualAddress(ipAddress);
  }

  public static MacAddress resolveRealAddress(
    InetAddress targetIpAddr,
    Node node,
    NetworkInterface nif,
    ArpCache arpCache,
    long resolveTimeout,
    TimeUnit unit
  ) {
    return resolveRealAddress(
      reqSender,
      targetIpAddr,
      node,
      nif,
      arpCache,
      resolveTimeout,
      unit
    );
  }

  public static void cache(Packet packet, ArpCache arpCache) {
    ArpPacket arpPacket = packet.get(ArpPacket.class);
    if (arpPacket == null) {
      throw new IllegalArgumentException(packet.toString());
    }

    InetAddress ipAddr
      = arpPacket.getHeader().getSrcProtocolAddr();
    MacAddress macAddr
      = arpPacket.getHeader().getSrcHardwareAddr();

    NeighborDiscoveryHelper.cache(arpCache, ipAddr, macAddr);
  }

  public static void clearCache(ArpCache arpCache) {
    NeighborDiscoveryHelper.clearCache(arpCache);
  }

  public static void reply(Packet packet, Node node, NetworkInterface nif) {
    ArpPacket arpPacket = packet.get(ArpPacket.class);
    if (arpPacket == null) {
      throw new IllegalArgumentException(packet.toString());
    }

    InetAddress srcIpAddr
      = arpPacket.getHeader().getSrcProtocolAddr();
    MacAddress srcMacAddr
      = arpPacket.getHeader().getSrcHardwareAddr();
    InetAddress dstIpAddr
      = arpPacket.getHeader().getDstProtocolAddr();

    ArpPacket.Builder arpBuilder = new ArpPacket.Builder();
    arpBuilder.hardwareType(ArpHardwareType.ETHERNET)
              .protocolType(EtherType.IPV4)
              .hardwareAddrLength((byte)MacAddress.SIZE_IN_BYTES)
              .protocolAddrLength((byte)ByteArrays.INET4_ADDRESS_SIZE_IN_BYTES)
              .operation(ArpOperation.REPLY)
              .srcHardwareAddr(nif.getMacAddress())
              .srcProtocolAddr(dstIpAddr)
              .dstHardwareAddr(srcMacAddr)
              .dstProtocolAddr(srcIpAddr);
    try {
      node.sendL3Packet(arpBuilder.build(), nif);
    } catch (SendPacketException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
  }

  private static class ArpRequestSender implements RequestSender {

    public void sendRequest(
      InetAddress targetIpAddr,
      Node node,
      NetworkInterface nif
    ) {
      InetAddress srcIpAddr = null;
      for (NifIpAddress addr: nif.getIpAddresses()) {
        if (addr.getIpAddr() instanceof Inet4Address) {
          srcIpAddr = addr.getIpAddr();
        }
      }
      if (srcIpAddr == null) {
        throw new IllegalArgumentException("No IPv4 address is found in " + nif);
      }
      MacAddress srcMacAddr = nif.getMacAddress();

      ArpPacket.Builder arpBuilder = new ArpPacket.Builder();
      arpBuilder.hardwareType(ArpHardwareType.ETHERNET)
                .protocolType(EtherType.IPV4)
                .hardwareAddrLength((byte)MacAddress.SIZE_IN_BYTES)
                .protocolAddrLength((byte)ByteArrays.INET4_ADDRESS_SIZE_IN_BYTES)
                .operation(ArpOperation.REQUEST)
                .srcHardwareAddr(srcMacAddr)
                .srcProtocolAddr(srcIpAddr)
                .dstHardwareAddr(MacAddress.ETHER_BROADCAST_ADDRESS)
                .dstProtocolAddr(targetIpAddr);
      try {
        node.sendL3Packet(arpBuilder.build(), nif);
      } catch (SendPacketException e) {
        // TODO 自動生成された catch ブロック
        e.printStackTrace();
      }
    }

  }

  public static class ArpCache extends NeighborCache {

    private ArpCache(long cacheLife) {
      super(cacheLife);
    }

  }

}
