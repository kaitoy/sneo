/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network.protocol;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.pcap4j.packet.IcmpV6CommonPacket;
import org.pcap4j.packet.IpV6Packet;
import org.pcap4j.packet.IpV6SimpleFlowLabel;
import org.pcap4j.packet.IpV6SimpleTrafficClass;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.SimpleBuilder;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.util.MacAddress;
import com.github.kaitoy.sneo.network.NetworkInterface;
import com.github.kaitoy.sneo.network.NifIpAddress;
import com.github.kaitoy.sneo.network.NifIpV6Address;

public final class IpV6Helper {

  public static final Inet6Address NODE_LOCAL_ALL_NODES_ADDRESS;
  public static final Inet6Address LINK_LOCAL_ALL_NODES_ADDRESS;
  public static final Inet6Address LINK_LOCAL_ALL_ROUTERS_ADDRESS;
  public static final Inet6Address UNSPECIFIED_ADDRESS;

  static {
    try {
      NODE_LOCAL_ALL_NODES_ADDRESS = (Inet6Address)InetAddress.getByName("FF01:0:0:0:0:0:0:1");
      LINK_LOCAL_ALL_NODES_ADDRESS = (Inet6Address)InetAddress.getByName("FF02:0:0:0:0:0:0:1");
      LINK_LOCAL_ALL_ROUTERS_ADDRESS = (Inet6Address)InetAddress.getByName("FF02:0:0:0:0:0:0:2");
      UNSPECIFIED_ADDRESS = (Inet6Address)InetAddress.getByName("::");
    } catch (UnknownHostException e) {
      throw new AssertionError("Never get here.");
    }
  }

  private IpV6Helper() { throw new AssertionError(); }

  public static boolean matchesDestination(Packet packet, Inet6Address addr) {
    IpV6Packet ipv6Packet = packet.get(IpV6Packet.class);
    if (ipv6Packet == null) {
      throw new IllegalArgumentException(packet.toString());
    }

    Inet6Address dstAddr = ipv6Packet.getHeader().getDstAddr();
    if (dstAddr.equals(addr)) {
      return true;
    }

    if (dstAddr.equals(LINK_LOCAL_ALL_NODES_ADDRESS)) {
      return true;
    }

    if (dstAddr.equals(LINK_LOCAL_ALL_ROUTERS_ADDRESS)) {
      return true;
    }

    return false;
  }

  public static boolean matchesDestination(Packet packet, NetworkInterface nif) {
    for (NifIpAddress nifAddr: nif.getIpAddresses()) {
      if (nifAddr instanceof NifIpV6Address) {
        NifIpV6Address nifV6Addr = (NifIpV6Address)nifAddr;
        if (matchesDestination(packet, nifV6Addr.getIpAddr())) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean isSameNetwork(
    Inet6Address addr1, Inet6Address addr2, int prefixLength
  ) {
    if (prefixLength < 0 || prefixLength > 128) {
      throw new IllegalArgumentException(
              "Invalid prefix length: " + prefixLength
            );
    }

    if (prefixLength <= 64) {
      long addr1Bitmap = ByteArrays.getLong(addr1.getAddress(), 0);
      long addr2Bitmap = ByteArrays.getLong(addr2.getAddress(), 0);
      return isSamePrefix(addr1Bitmap, addr2Bitmap, prefixLength);
    }
    else {
      long addr1Bitmap1h = ByteArrays.getLong(addr1.getAddress(), 0);
      long addr2Bitmap1h = ByteArrays.getLong(addr2.getAddress(), 0);
      if (addr1Bitmap1h != addr2Bitmap1h) {
        return false;
      }
      long addr1Bitmap2h = ByteArrays.getLong(addr1.getAddress(), 16);
      long addr2Bitmap2h = ByteArrays.getLong(addr2.getAddress(), 16);
      return isSamePrefix(addr1Bitmap2h, addr2Bitmap2h, prefixLength - 64);
    }
  }

  public static boolean isSameNetwork(
    Inet6Address addr1, NetworkInterface nif
  ) {
    for (NifIpAddress nifAddr: nif.getIpAddresses()) {
      if (nifAddr instanceof NifIpV6Address) {
        NifIpV6Address nifV6Addr = (NifIpV6Address)nifAddr;
        if (isSameNetwork(addr1, nifV6Addr.getIpAddr(), nifV6Addr.getPrefixLength())) {
          return true;
        }
      }
    }
    return false;
  }

  private static boolean isSamePrefix(long bitmap1, long bitmap2, int prefixLength) {
    long mask = 0;
    for (int i = 0; i < prefixLength; i++) {
      mask <<= 1;
      mask++;
    }
    for (int i = 0; i < 64 - prefixLength ; i++) {
      mask <<= 1;
    }
    return (bitmap1 & mask) == (bitmap2 & mask);
  }

  public static IpV6RoutingTable newRoutingTable() {
    return new IpV6RoutingTable();
  }

  public static Inet6Address getNextHop(
    Inet6Address dstIpAddr, IpV6RoutingTable ipV6RoutingTable
  ) {
    return ipV6RoutingTable.getNextHop(dstIpAddr);
  }

  public static IpV6Packet decrementTtl(
    IpV6Packet packet
  ) throws TimeoutException {
    int hopLimit = packet.getHeader().getHopLimitAsInt();
    if (hopLimit <= 1) {
      throw new TimeoutException();
    }

    hopLimit--;
    IpV6Packet.Builder b
      = packet.getBuilder().hopLimit((byte)hopLimit);

    return b.build();
  }

  public static Inet6Address generateLinkLocalIpV6Address(MacAddress macAddr) {
    try {
      return generateIpV6Address((Inet6Address)InetAddress.getByName("fe80::"), macAddr);
    } catch (UnknownHostException e) {
      throw new AssertionError("Never get here.");
    }
  }

  public static Inet6Address generateIpV6Address(Inet6Address prefix, MacAddress macAddr) {
    byte[] addr = new byte[16];
    System.arraycopy(prefix.getAddress(), 0, addr, 0, 8);
    System.arraycopy(convertToEui64(macAddr), 0, addr, 8, 8);
    try {
      return (Inet6Address)InetAddress.getByAddress(addr);
    } catch (UnknownHostException e) {
      throw new AssertionError("Never get here.");
    }
  }

  public static byte[] convertToEui64(MacAddress macAddr) {
    byte[] eui64 = new byte[8];
    byte[] oui = macAddr.getOui().valueAsByteArray();
    oui[0] = (byte)(oui[0] ^ 0x02);
    System.arraycopy(oui, 0, eui64, 0, 3);

    eui64[3] = (byte)0xFF;
    eui64[4] = (byte)0xFE;

    System.arraycopy(macAddr.getAddress(), 3, eui64, 5, 3);

    return eui64;
  }


  public static Inet6Address generateSolicitedNodeMulticastAddress(Inet6Address targetAddr) {
    byte[] addr = new byte[16];
    addr[0] = (byte)0xFE;
    addr[1] = (byte)0x02;
    addr[11] = (byte)0x01;
    addr[12] = (byte)0xFF;
    System.arraycopy(targetAddr.getAddress(), 13, addr, 13, 3);

    try {
      return (Inet6Address)InetAddress.getByAddress(addr);
    } catch (UnknownHostException e) {
      throw new AssertionError("Never get here.");
    }
  }

  public static MacAddress generateNeighborSolicitationMacAddress(Inet6Address dstAddr) {
    byte[] addr = new byte[6];
    addr[0] = (byte)0x33;
    addr[1] = (byte)0x33;
    System.arraycopy(dstAddr.getAddress(), 12, addr, 2, 4);
    return MacAddress.getByAddress(addr);
  }

  public static IpV6Packet pack(
    final Packet payload, Inet6Address src, Inet6Address dst, int hopLimit, short id
  ) {
    IpNumber nextHeader;
    if (payload instanceof UdpPacket) {
      nextHeader = IpNumber.UDP;
    }
    else if (payload instanceof IcmpV6CommonPacket) {
      nextHeader = IpNumber.ICMPV6;
    }
    else if (payload instanceof TcpPacket) {
      nextHeader = IpNumber.TCP;
    }
    else {
      throw new AssertionError();
    }

    IpV6Packet.Builder builder = new IpV6Packet.Builder();
    return builder.version(IpVersion.IPV6)
                  .trafficClass(IpV6SimpleTrafficClass.newInstance((byte)0))
                  .flowLabel(IpV6SimpleFlowLabel.newInstance(0))
                  .nextHeader(nextHeader)
                  .hopLimit((byte)hopLimit)
                  .srcAddr(src)
                  .dstAddr(dst)
                  .payloadBuilder(new SimpleBuilder(payload))
                  .correctLengthAtBuild(true)
                  .build();
  }

  public static Inet6Address getNextAddress(Inet6Address addr, int prefixLength) {
    byte[] rawAddr = addr.getAddress();
    rawAddr[15]++;
    try {
      return (Inet6Address)InetAddress.getByAddress(rawAddr);
    } catch (UnknownHostException e) {
      throw new AssertionError("Never get here.");
    }
  }

  public static class IpV6RoutingTable {

    private final Map<Inet6Address, IpV6RoutingTableEntry> entries
      = new HashMap<Inet6Address, IpV6RoutingTableEntry>();

    private IpV6RoutingTable() {}

    public void addRoute(
      Inet6Address dst,
      int prefixLength,
      Inet6Address gw,
      int metric
    ) {
      synchronized (entries) {
        entries.put(
          dst,
          new IpV6RoutingTableEntry(dst, prefixLength, gw, metric)
        );
      }
    }

    private Inet6Address getNextHop(Inet6Address dst) {
      Collection<IpV6RoutingTableEntry> candidates = null;

      synchronized (entries) {
        IpV6RoutingTableEntry justMatchedEntry = entries.get(dst);
        if (justMatchedEntry != null) {
          return justMatchedEntry.gw;
        }

        candidates = entries.values();
      }

      IpV6RoutingTableEntry mostMatchedEntry = null;
      for (IpV6RoutingTableEntry entry: candidates) {
        if (isSameNetwork(entry.dst, dst, entry.prefixLength)) {
          if (mostMatchedEntry == null) {
            mostMatchedEntry = entry;
          }
          else if (entry.prefixLength > mostMatchedEntry.prefixLength) {
            mostMatchedEntry = entry;
          }
          else if (
               entry.prefixLength == mostMatchedEntry.prefixLength
            && entry.metric < mostMatchedEntry.metric
          ) {
            mostMatchedEntry = entry;
          }
        }
      }

      if (mostMatchedEntry == null) {
        return null;
      }

      return mostMatchedEntry.gw;
    }

    public List<IpV6RoutingTableEntry> getEntries() {
      return new ArrayList<IpV6RoutingTableEntry>(entries.values());
    }

    public final class IpV6RoutingTableEntry {

      private final Inet6Address dst;
      private final int prefixLength;
      private final Inet6Address gw;
      private final int metric;

      private IpV6RoutingTableEntry(
        Inet6Address dst,
        int prefixLength,
        Inet6Address gw,
        int metric
      ) {
        this.dst = dst;
        this.prefixLength = prefixLength;
        this.gw = gw;
        this.metric = metric;
      }

      @Override
      public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DST[").append(dst).append("/").append(prefixLength).append("] ")
          .append("GW[").append(gw).append("] ")
          .append("METRIC[").append(metric).append("]");
        return sb.toString();
      }

    }

  }

}
