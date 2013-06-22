/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.network.protocol;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.ArpHardwareType;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.util.MacAddress;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import com.github.network.MacAddressManager;
import com.github.network.NetworkInterface;
import com.github.network.Node;
import com.github.network.SendPacketException;
import com.github.util.FutureData;
import com.github.util.NamedThreadFactory;

public final class ArpHelper {

  private static final LogAdapter logger
    = LogFactory.getLogger(ArpHelper.class.getPackage().getName());

  private ArpHelper() { throw new AssertionError(); }

  public static ArpTable newArpTable(long cacheLife) {
    return new ArpTable(cacheLife);
  }

  public static boolean matchesDestination(Packet packet, InetAddress addr) {
    ArpPacket arpPacket = packet.get(ArpPacket.class);
    if (arpPacket == null) {
      throw new IllegalArgumentException(packet.toString());
    }

    return addr.equals(
             arpPacket.getHeader().getDstProtocolAddr()
           );
  }

  public static MacAddress resolveVirtualAddress(InetAddress ipAddress) {
    return MacAddressManager.getInstance().resolveVirtualAddress(ipAddress);
  }

  public static MacAddress resolveRealAddress(
    InetAddress targetIpAddr,
    Node node,
    NetworkInterface nif,
    ArpTable arpTable,
    long resolveTimeout,
    TimeUnit unit
  ) {
    InetAddress srcIpAddr = nif.getIpAddress();
    MacAddress srcMacAddr = nif.getMacAddress();

    FutureData<MacAddress> fd;
    Boolean hit;
    synchronized (arpTable) {
      fd = arpTable.getFutureData(targetIpAddr);
      if (fd != null) {
        logger.debug("Hit ARP cache");
        hit = true;
      }
      else {
        fd = arpTable.newEntry(targetIpAddr);
        hit = false;
      }
    }

    if (!hit) {
      sendRequest(targetIpAddr, srcIpAddr, srcMacAddr, node, nif);
    }

    MacAddress macAddress = null;
    try {
      macAddress = fd.get(resolveTimeout, unit);
      if (logger.isDebugEnabled()) {
        logger.debug(targetIpAddr + " was resolved to " + macAddress);
      }
    } catch (InterruptedException e) {
      logger.warn(e);
    } catch (TimeoutException e) {
      logger.warn(e);
    }

    return macAddress;
  }

  private static void sendRequest(
    InetAddress dstAddr,
    InetAddress srcIpAddr,
    MacAddress srcMacAddr,
    Node node,
    NetworkInterface nif
  ) {
    ArpPacket.Builder arpBuilder = new ArpPacket.Builder();
    arpBuilder.hardwareType(ArpHardwareType.ETHERNET)
              .protocolType(EtherType.IPV4)
              .hardwareLength((byte)MacAddress.SIZE_IN_BYTES)
              .protocolLength((byte)ByteArrays.INET4_ADDRESS_SIZE_IN_BYTES)
              .operation(ArpOperation.REQUEST)
              .srcHardwareAddr(srcMacAddr)
              .srcProtocolAddr(srcIpAddr)
              .dstHardwareAddr(MacAddress.ETHER_BROADCAST_ADDRESS)
              .dstProtocolAddr(dstAddr);
    try {
      node.sendL3Packet(arpBuilder.build(), nif);
    } catch (SendPacketException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
  }

  public static void cache(Packet packet, ArpTable arpTable) {
    ArpPacket arpPacket = packet.get(ArpPacket.class);
    if (arpPacket == null) {
      throw new IllegalArgumentException(packet.toString());
    }

    InetAddress ipAddr
      = arpPacket.getHeader().getSrcProtocolAddr();
    MacAddress macAddr
      = arpPacket.getHeader().getSrcHardwareAddr();

    arpTable.cache(ipAddr, macAddr);

    if (logger.isDebugEnabled()) {
      logger.debug(
        "Cached result of ARP request: (" + ipAddr + ", " + macAddr + ")"
      );
    }
  }

  public static void clearTable(ArpTable arpTable) {
    arpTable.clearTable();
  }

  public static void reply(
    Packet packet, MacAddress macAddr, Node node, NetworkInterface nif
  ) {
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
              .hardwareLength((byte)MacAddress.SIZE_IN_BYTES)
              .protocolLength((byte)ByteArrays.INET4_ADDRESS_SIZE_IN_BYTES)
              .operation(ArpOperation.REPLY)
              .srcHardwareAddr(macAddr)
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

  public static class ArpTable {

    private final long cacheLife; // [s]
    private final ScheduledExecutorService scheduler
      = Executors.newSingleThreadScheduledExecutor(
          new NamedThreadFactory(
            ArpCacheInvalidater.class.getSimpleName(),
            true
          )
        ); // TODO shutdown
    private final Map<InetAddress, FutureData<MacAddress>> table
      = new HashMap<InetAddress, FutureData<MacAddress>>();
    private final Map<InetAddress, Future<?>> invalidateArpCacheFutures
      = new HashMap<InetAddress, Future<?>>();

    private ArpTable(long cacheLife) {
      this.cacheLife = cacheLife;
    }

    private FutureData<MacAddress> newEntry(InetAddress ipAddress) {
      synchronized (table) {
        FutureData<MacAddress> fd = new FutureData<MacAddress>();
        table.put(ipAddress, fd);
        return fd;
      }
    }

    private FutureData<MacAddress> getFutureData(InetAddress ipAddr) {
      synchronized (table) {
        return table.get(ipAddr);
      }
    }

    private void cache(InetAddress ipAddr, MacAddress macAddr) {
      synchronized (table) {
        if (invalidateArpCacheFutures.containsKey(ipAddr)) {
          invalidateArpCacheFutures.get(ipAddr).cancel(true);
        }

        if (table.containsKey(ipAddr)) {
          FutureData<MacAddress> f = table.get(ipAddr);
          f.set(macAddr);
        }
        else {
          table.put(ipAddr, new FutureData<MacAddress>(macAddr));
        }

        invalidateArpCacheFutures.put(
          ipAddr,
          scheduler.schedule(
            new ArpCacheInvalidater(ipAddr), cacheLife, TimeUnit.SECONDS
          )
        );
      }
    }

    private void clearTable() {
      synchronized (table) {
        table.clear();
        for (Future<?> f: invalidateArpCacheFutures.values()) {
          f.cancel(false);
        }
        invalidateArpCacheFutures.clear();
      }
    }

    private class ArpCacheInvalidater implements Runnable {

      private InetAddress ipAddress;

      public ArpCacheInvalidater(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
      }

      public void run() {
        synchronized (table) {
          if (!Thread.interrupted()) {
            table.remove(ipAddress);
          }
        }
      }

    }

  }

}
