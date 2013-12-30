/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network.protocol;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.pcap4j.packet.IcmpV4CommonPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV4Rfc791Tos;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.SimpleBuilder;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.pcap4j.util.ByteArrays;
import com.github.kaitoy.sneo.network.NetworkInterface;
import com.github.kaitoy.sneo.network.NifIpAddress;
import com.github.kaitoy.sneo.network.NifIpV4Address;

public final class IpV4Helper {

  public static final Inet4Address UNSPECIFIED_ADDRESS;

  static {
    try {
      UNSPECIFIED_ADDRESS = (Inet4Address)InetAddress.getByName("0.0.0.0");
    } catch (UnknownHostException e) {
      throw new AssertionError("Never get here.");
    }
  }

  private IpV4Helper() { throw new AssertionError(); }

  public static IpV4RoutingTable newRoutingTable() {
    return new IpV4RoutingTable();
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

  public static boolean matchesDestination(Packet packet, NetworkInterface nif) {
    for (NifIpAddress nifAddr: nif.getIpAddresses()) {
      if (nifAddr instanceof NifIpV4Address) {
        NifIpV4Address nifV4Addr = (NifIpV4Address)nifAddr;
        Inet4Address mask = getSubnetMaskFrom(nifV4Addr.getPrefixLength());
        if (matchesDestination(packet, nifV4Addr.getIpAddr(), mask)) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean isBroadcastAddr(Inet4Address addr, Inet4Address subnetmask) {
    int subnetmaskBitmap = ByteArrays.getInt(subnetmask.getAddress(), 0);
    int addrBitmap = ByteArrays.getInt(addr.getAddress(), 0);
    return ~((addrBitmap & ~subnetmaskBitmap) | subnetmaskBitmap) == 0;
  }

  public static boolean isNetworkAddr(Inet4Address addr, Inet4Address subnetmask) {
    int subnetmaskBitmap = ByteArrays.getInt(subnetmask.getAddress(), 0);
    int addrBitmap = ByteArrays.getInt(addr.getAddress(), 0);
    return (addrBitmap & ~subnetmaskBitmap) == 0;
  }

  public static boolean isSameNetwork(
    Inet4Address addr1, Inet4Address addr2, Inet4Address subnetmask
  ) {
    int addr1Bitmap = ByteArrays.getInt(addr1.getAddress(), 0);
    int addr2Bitmap = ByteArrays.getInt(addr2.getAddress(), 0);
    int subnetmaskBitmap = ByteArrays.getInt(subnetmask.getAddress(), 0);
    return (addr1Bitmap & subnetmaskBitmap) == (addr2Bitmap & subnetmaskBitmap);
  }

  public static boolean isSameNetwork(
    Inet4Address addr1, NetworkInterface nif
  ) {
    for (NifIpAddress nifAddr: nif.getIpAddresses()) {
      if (nifAddr instanceof NifIpV4Address) {
        NifIpV4Address nifV4Addr = (NifIpV4Address)nifAddr;
        Inet4Address mask = getSubnetMaskFrom(nifV4Addr.getPrefixLength());
        if (isSameNetwork(addr1, nifV4Addr.getIpAddr(), mask)) {
          return true;
        }
      }
    }
    return false;
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
      return (Inet4Address)InetAddress.getByAddress(mask);
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
      newAddr = (Inet4Address)InetAddress.getByAddress(rawAddr);
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
      newAddr = (Inet4Address)InetAddress.getByAddress(rawAddr);
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
    else if (payload instanceof TcpPacket) {
      ipNum = IpNumber.TCP;
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
             .payloadBuilder(new SimpleBuilder(payload))
             .correctChecksumAtBuild(true)
             .correctLengthAtBuild(true)
             .build();
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

  public static Inet4Address getNextHop(
    Inet4Address dstIpAddr, IpV4RoutingTable ipV4RoutingTable
  ) {
    return ipV4RoutingTable.getNextHop(dstIpAddr);
  }

  public static class IpV4RoutingTable {

    private final Map<Inet4Address, IpV4RoutingTableEntry> entries
      = new HashMap<Inet4Address, IpV4RoutingTableEntry>();

    private IpV4RoutingTable() {}

    public void addRoute(
      Inet4Address dst,
      Inet4Address mask,
      Inet4Address gw,
      int metric
    ) {
      synchronized (entries) {
        entries.put(
          dst,
          new IpV4RoutingTableEntry(dst, mask, gw, metric)
        );
      }
    }

    private Inet4Address getNextHop(Inet4Address dst) {
      Collection<IpV4RoutingTableEntry> values = null;

      synchronized (entries) {
        IpV4RoutingTableEntry justMatchedEntry = entries.get(dst);
        if (justMatchedEntry != null) {
          return justMatchedEntry.gw;
        }

        values = entries.values();
      }

      int dstBitmap = ByteArrays.getInt(dst.getAddress(), 0);
      IpV4RoutingTableEntry mostMatchedEntry = null;
      for (IpV4RoutingTableEntry entry: values) {
        if (
          entry.dstBitmap
            == (dstBitmap & entry.maskBitmap)
        ) {
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

    public List<IpV4RoutingTableEntry> getEntries() {
      return new ArrayList<IpV4RoutingTableEntry>(entries.values());
    }

    public final class IpV4RoutingTableEntry {

      private final Inet4Address dst;
      private final int dstBitmap;
      private final Inet4Address mask;
      private final int maskBitmap;
      private final int prefixLength;
      private final Inet4Address gw;
      private final int metric;

      private IpV4RoutingTableEntry(
        Inet4Address dst,
        Inet4Address mask,
        Inet4Address gw,
        int metric
      ) {
        this.dst = dst;
        this.dstBitmap = ByteArrays.getInt(dst.getAddress(), 0);
        this.mask = mask;
        this.maskBitmap = ByteArrays.getInt(mask.getAddress(), 0);
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
