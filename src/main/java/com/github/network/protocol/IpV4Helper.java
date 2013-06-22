/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.network.protocol;

import org.pcap4j.packet.AbstractPacket.AbstractBuilder;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.pcap4j.packet.IcmpV4CommonPacket;
import org.pcap4j.packet.IpV4Rfc791Tos;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.pcap4j.util.ByteArrays;
import com.github.network.NetworkInterface;

public final class IpV4Helper {

  private IpV4Helper() { throw new AssertionError(); }

  public static RoutingTable newRoutingTable() {
    return new RoutingTable();
  }

  public static boolean matchesDestination(
    Packet packet, Inet4Address addr, Inet4Address subnetmask
  ) {
    IpV4Packet ipv4Packet = packet.get(IpV4Packet.class);
    if (ipv4Packet == null) {
      throw new IllegalArgumentException(packet.toString());
    }

    Inet4Address dstAddr = ipv4Packet.getHeader().getDstAddr();
    if (dstAddr.equals(addr)) {
      return true;
    }

    if (!isSameNetwork(addr, dstAddr, subnetmask)) {
      return false;
    }

    return isBroadcastAddr(dstAddr, subnetmask);
  }

  public static boolean isBroadcastAddr(Inet4Address addr, Inet4Address subnetmask) {
    int subnetmaskBitMap = ByteArrays.getInt(subnetmask.getAddress(), 0);
    int addrBitMap = ByteArrays.getInt(addr.getAddress(), 0);
    return ~((addrBitMap & ~subnetmaskBitMap) | subnetmaskBitMap) == 0;
  }

  public static boolean isNetworkAddr(Inet4Address addr, Inet4Address subnetmask) {
    int subnetmaskBitMap = ByteArrays.getInt(subnetmask.getAddress(), 0);
    int addrBitMap = ByteArrays.getInt(addr.getAddress(), 0);
    return (addrBitMap & ~subnetmaskBitMap) == 0;
  }

  public static boolean isSameNetwork(
    Inet4Address addr1, Inet4Address addr2, Inet4Address subnetmask
  ) {
    int addr1BitMap = ByteArrays.getInt(addr1.getAddress(), 0);
    int addr2BitMap = ByteArrays.getInt(addr2.getAddress(), 0);
    int subnetmaskBitMap = ByteArrays.getInt(subnetmask.getAddress(), 0);
    return (addr1BitMap & subnetmaskBitMap) == (addr2BitMap & subnetmaskBitMap);
  }

  public static Inet4Address getSubnetMaskFrom(int prefixLength) {
    if (prefixLength < 0 || prefixLength > 32) {
      throw new IllegalArgumentException(
              "Invalid prefix length: " + prefixLength
            );
    }

    byte[] mask = new byte[4];
    int byteIdx = 0;
    for (; byteIdx < prefixLength / 8; byteIdx++) {
      mask[byteIdx] = (byte)255;
    }

    if (!(byteIdx == mask.length)) {
      int value = 0;
      int tmp = 128;
      for (int i = 0; i < prefixLength % 8; i++) {
        value += tmp;
        tmp >>= 1;
      }
      mask[byteIdx] = (byte)value;
    }

    try {
      return (Inet4Address)Inet4Address.getByAddress(mask);
    } catch (UnknownHostException e) {
      throw new AssertionError("Never get here");
    }
  }

  public static int getPrefixLengthFrom(Inet4Address subnetMask) {
    int length = 0;
    for (byte b: subnetMask.getAddress()) {
      for (int mask = 128; mask > 0; mask >>= 1) {
        if ((b & mask) == 0) {
          return length;
        }
        length++;
      }
    }
    return length;
  }

  public static Inet4Address getNextAddress(Inet4Address addr, Inet4Address mask) {
    if (isBroadcastAddr(addr, mask)) {
      return null;
    }

    byte[] rawAddr = addr.getAddress();
    rawAddr[3]++;

    Inet4Address newAddr;
    try {
      newAddr = (Inet4Address)Inet4Address.getByAddress(rawAddr);
    } catch (UnknownHostException e) {
      throw new AssertionError("Never get here.");
    }

    if (isBroadcastAddr(newAddr, mask)) {
      return null;
    }
    else {
      return newAddr;
    }
  }

  public static Inet4Address getPrevAddress(Inet4Address addr, Inet4Address mask) {
    if (isNetworkAddr(addr, mask)) {
      return null;
    }

    byte[] rawAddr = addr.getAddress();
    rawAddr[3]--;

    Inet4Address newAddr;
    try {
      newAddr = (Inet4Address)Inet4Address.getByAddress(rawAddr);
    } catch (UnknownHostException e) {
      throw new AssertionError("Never get here.");
    }

    if (isNetworkAddr(newAddr, mask)) {
      return null;
    }
    else {
      return newAddr;
    }
  }

  public static IpV4Packet pack(
    final Packet payload, Inet4Address src, Inet4Address dst, int ttl, short id
  ) {
    IpNumber ipNum;
    if (payload instanceof UdpPacket) {
      ipNum = IpNumber.UDP;
    }
    else if (payload instanceof IcmpV4CommonPacket) {
      ipNum = IpNumber.ICMPV4;
    }
    else {
      throw new AssertionError();
    }

    IpV4Packet.Builder builder = new IpV4Packet.Builder();
    return
      builder.version(IpVersion.IPV4)
             .tos(IpV4Rfc791Tos.newInstance((byte)0))
             .identification(id)
             .ttl((byte)ttl)
             .protocol(ipNum)
             .srcAddr(src)
             .dstAddr(dst)
             .payloadBuilder(
                new AbstractBuilder() {
                  public Packet build() { return payload; }
                }
              )
             .correctChecksumAtBuild(true)
             .correctLengthAtBuild(true)
             .build();
  }

  public static NetworkInterface selectNif4Neighbor(
    Inet4Address neighbor, Collection<? extends NetworkInterface> nifs
  ) {
    for (NetworkInterface nif: nifs) {
      if (nif.getIpAddress() == null) { continue; }

      if(
        IpV4Helper.isSameNetwork(
          neighbor,
          (Inet4Address)nif.getIpAddress(),
          (Inet4Address)nif.getSubnetMask()
        )
      ) {
        return nif;
      }
    }

    return null;
  }

  public static IpV4Packet decrementTtl(
    IpV4Packet packet
  ) throws TimeoutException {
    int ttl = packet.getHeader().getTtlAsInt();
    if (ttl <= 1) {
      throw new TimeoutException();
    }

    ttl--;
    IpV4Packet.Builder b
      = packet.getBuilder().ttl((byte)ttl).correctChecksumAtBuild(true);

    return b.build();
  }

//  public static InetAddress getNextHop(
//    Packet packet, RoutingTable routingTable
//  ) {
//    if (!packet.contains(IpV4Packet.class)) {
//      throw new IllegalArgumentException(packet.toString());
//    }
//
//    InetAddress dstIpAddr
//      = packet.get(IpV4Packet.class).getHeader().getDstAddr();
//    return getNextHop(dstIpAddr, routingTable);
//  }

  public static Inet4Address getNextHop(
    Inet4Address dstIpAddr, RoutingTable routingTable
  ) {
    return routingTable.getNextHop(dstIpAddr);
  }

  public static class RoutingTable {

    private final Map<Inet4Address, RoutingTableEntry> entries
      = new HashMap<Inet4Address, RoutingTableEntry>();

    private RoutingTable() {}

    public void addRoute(
      Inet4Address dst,
      Inet4Address mask,
      Inet4Address gw,
      int metric
    ) {
      synchronized (entries) {
        entries.put(
          dst,
          new RoutingTableEntry(dst, mask, gw, metric)
        );
      }
    }

    public void addDefaultRoute(Inet4Address gw) {
      Inet4Address addr;
      try {
        addr = (Inet4Address)Inet4Address.getByName("0.0.0.0");
      } catch (UnknownHostException e) {
        throw new AssertionError("Never get here");
      }

      synchronized (entries) {
        entries.put(
          addr,
          new RoutingTableEntry(addr, addr, gw, 1)
        );
      }
    }

    private Inet4Address getNextHop(Inet4Address dst) {
      RoutingTableEntry[] values = null;

      synchronized (entries) {
        RoutingTableEntry justMatchedEntry = entries.get(dst);
        if (justMatchedEntry != null) {
          return justMatchedEntry.gw;
        }

        values = entries.values().toArray(new RoutingTableEntry[0]);
      }

      int dstBitMap = ByteArrays.getInt(dst.getAddress(), 0);
      RoutingTableEntry mostMatchedEntry = null;
      for (RoutingTableEntry entry: values) {
        if (
          entry.dstBitMap
            == (dstBitMap & entry.maskBitMap)
        ) {
          if (mostMatchedEntry == null) {
            mostMatchedEntry = entry;
          }
          else if (entry.prefixLength > mostMatchedEntry.prefixLength) {
            mostMatchedEntry = entry;
          }
        }
      }

      if (mostMatchedEntry == null) {
        return null;
      }

      return mostMatchedEntry.gw;
    }

    public List<RoutingTableEntry> getEntries() {
      return new ArrayList<RoutingTableEntry>(entries.values());
    }

    public final class RoutingTableEntry {

      private final Inet4Address dst;
      private final int dstBitMap;
      private final Inet4Address mask;
      private final int maskBitMap;
      private final int prefixLength;
      private final Inet4Address gw;
      private final int metric;

      private RoutingTableEntry(
        Inet4Address dst,
        Inet4Address mask,
        Inet4Address gw,
        int metric
      ) {
        this.dst = dst;
        this.dstBitMap = ByteArrays.getInt(dst.getAddress(), 0);
        this.mask = mask;
        this.maskBitMap = ByteArrays.getInt(mask.getAddress(), 0);
        this.prefixLength = getPrefixLengthFrom(mask);
        this.gw = gw;
        this.metric = metric;
      }

      @Override
      public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DST[").append(dst).append("] ")
          .append("MASK[").append(mask).append("] ")
          .append("GW[").append(gw).append("] ")
          .append("METRIC[").append(metric).append("]");
        return sb.toString();
      }

    }

  }

}
