/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.IcmpV4CommonPacket;
import org.pcap4j.packet.IllegalPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UnknownPacket;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.MacAddress;
import org.pcap4j.util.NifSelector;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import com.github.network.protocol.EthernetHelper;
import com.github.util.NamedThreadFactory;

public final class RealNetworkInterface implements NetworkInterface {

  private static final LogAdapter logger
    = LogFactory.getLogger(RealNetworkInterface.class);

  private static final int MAX_INBOUND_MESSAGE_SIZE = 65536;
  private static final int PCAP_READ_TIMEOUT = 10;
  private static final int SEND_PACKET_RETRY_COUNT = 5;
  private static final long SHUTDOWN_TIMEOUT = 2000;
  private static final TimeUnit SHUTDOWN_TIMEOUT_UNIT = TimeUnit.MILLISECONDS;

  private final String name;
  private final MacAddress macAddress;
  private final InetAddress ipAddress;
  private final InetAddress subnetMask;
  private final String deviceName;
  private final PacketListener host;
  private final PcapNetworkInterface pcapNif;
  private final PcapHandle handle2capture;
  private final PcapHandle handle2send;
  private final ExecutorService packetCaptorExecutor;
  private final Random random = new Random(System.currentTimeMillis());
  private final Object thisLock = new Object();

  private volatile boolean running = false;

  public RealNetworkInterface(
    String name,
    MacAddress macAddress,
    InetAddress ipAddress,
    InetAddress subnetMask,
    String deviceName,
    PacketListener host
  ) {
    if (
         name == null
      || macAddress == null
      || ipAddress == null
      || subnetMask == null
      || host == null
    ) {
      StringBuilder sb = new StringBuilder(80);
      sb.append("name: ").append(name)
        .append(" macAddress: ").append(macAddress)
        .append(" ipAddress: ").append(ipAddress)
        .append(" subnetMask: ").append(subnetMask)
        .append(" host: ").append(host);
      throw new NullPointerException(sb.toString());
    }

    this.name = name;
    this.macAddress = macAddress;
    this.ipAddress = ipAddress;
    this.subnetMask = subnetMask;
    this.deviceName = deviceName;
    this.host = host;
    this.packetCaptorExecutor
      = Executors.newSingleThreadExecutor(
          new NamedThreadFactory(
            name + "_" + PacketCaptor.class.getSimpleName(),
            true
          )
        );

    PcapNetworkInterface pnif = null;
    InetAddress rnifAddr
      = NetworkPropertiesLoader.getRealNetworkInterfaceIpAddress();
    if (rnifAddr != null) {
      try {
        pnif = Pcaps.getDevByAddress(rnifAddr);
        if (pnif == null) {
          throw new IllegalStateException("No NIF has " + rnifAddr);
        }
      } catch (PcapNativeException e) {
        throw new IllegalStateException(e);
      }
    }
    else if (deviceName != null){
        try {
          pnif = Pcaps.getDevByName(deviceName);
        } catch (PcapNativeException e) {
          throw new IllegalStateException(e);
        }
    } else {
      try {
        pnif = new NifSelector().selectNetworkInterface();
        if (pnif == null) {
          throw new IllegalStateException("No NIF was selected.");
        }
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    }

    this.pcapNif = pnif;

    try {
      this.handle2capture
        = this.pcapNif.openLive(
            MAX_INBOUND_MESSAGE_SIZE,
            PromiscuousMode.PROMISCUOUS,
            PCAP_READ_TIMEOUT
          );
      this.handle2send
        = this.pcapNif.openLive(
            MAX_INBOUND_MESSAGE_SIZE,
            PromiscuousMode.PROMISCUOUS,
            PCAP_READ_TIMEOUT
          );
    } catch (PcapNativeException e) {
      throw new IllegalStateException(e);
    }

    StringBuilder sb = new StringBuilder();
    sb.append("(")
      .append("ether dst ")
      .append(Pcaps.toBpfString(macAddress))
      .append(")")
      .append(" or ")
      .append("(")
      .append("arp and ether dst ")
      .append(Pcaps.toBpfString(MacAddress.ETHER_BROADCAST_ADDRESS))
      .append(")");

    try {
      logger.info("Filtering expression: " + sb);
      handle2capture.setFilter(
        sb.toString(),
        BpfCompileMode.OPTIMIZE
      );
    } catch (PcapNativeException e) {
      throw new AssertionError("Never get here.");
    } catch (NotOpenException e) {
      throw new AssertionError("Never get here.");
    }
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

  @Deprecated
  public void addUser(PacketListener user) {
    throw new UnsupportedOperationException();
  }

  public void start() {
    synchronized (thisLock) {
      if (running) {
        logger.warn("Already started.");
        return;
      }

      packetCaptorExecutor.execute(new PacketCaptor());
      running = true;
    }
    logger.info("started");
  }

  public void stop() {
    synchronized (thisLock) {
      if (!running) {
        logger.warn("Already stopped.");
        return;
      }

      handle2capture.breakLoop();
      running = false;
    }
    logger.info("stopped");
  }

  public boolean isRunning() {
    return running;
  }

  public void sendPacket(Packet packet) {
    if (!running) {
      logger.warn("Not running. Can't send packet: " + packet);
      return;
    }

    for (int i = 1; i <= SEND_PACKET_RETRY_COUNT; i ++) {
      try {
        handle2send.sendPacket(packet);
        if (logger.isDebugEnabled()) {
          logger.debug("Sent a packet: " + packet);
        }
        return;
      } catch (PcapNativeException e) {
        logger.warn("Failed to send a packet. Retry: " + i);
        try {
          Thread.sleep(0, random.nextInt(1000000));
        } catch (InterruptedException ie) {
          logger.warn(ie);
          break;
        }
      } catch (NotOpenException e) {
        throw new AssertionError("Never get here.");
      }
    }

    logger.error("Couldn't send packet: " + packet);
  }

  public void shutdown() {
    synchronized (thisLock) {
      if (running) {
        stop();
      }

      packetCaptorExecutor.shutdown();
      try {
        boolean terminated
          = packetCaptorExecutor.awaitTermination(
              SHUTDOWN_TIMEOUT, SHUTDOWN_TIMEOUT_UNIT
            );
        if (!terminated) {
          logger.warn("Termination timeout occured.");

          EthernetPacket.Builder eb = new EthernetPacket.Builder();
          eb.type(EtherType.ARP)
            .srcAddr(macAddress)
            .dstAddr(MacAddress.ETHER_BROADCAST_ADDRESS)
            .paddingAtBuild(true)
            .payloadBuilder(new UnknownPacket.Builder().rawData(new byte[5]));

          try {
            logger.info("Try to send a bogus packet to let the captor break.");
            handle2send.sendPacket(eb.build());
          } catch (PcapNativeException e) {
            logger.error(e);
          } catch (NotOpenException e) {
            throw new AssertionError("Never get here.");
          }
        }
      } catch (InterruptedException e) {
        logger.warn(e);
      }

      handle2capture.close();
      handle2send.close();
    }

    logger.info("shutdowned");
  }

  private class PacketCaptor implements Runnable {

    public void run() {
      if (handle2capture == null || !handle2capture.isOpen()) {
        throw new IllegalStateException("handle: " + handle2capture);
      }

      logger.info("start.");
      try {
        handle2capture.loop(-1, new PacketListenerImpl());
      } catch (PcapNativeException e) {
        logger.error(e);
      } catch (InterruptedException e) {
        logger.info("broken.");
      } catch (NotOpenException e) {
        throw new AssertionError("Never get here.");
      }
      logger.info("stopped.");
    }

  }

  private class PacketListenerImpl implements PacketListener {

    public void gotPacket(Packet packet) {
      if (logger.isDebugEnabled()) {
        logger.debug("Received a packet: " + packet);
      }

      if (!(packet instanceof EthernetPacket)) {
        if (logger.isDebugEnabled()) {
          logger.debug("Dropped an unknown packet: " + packet);
        }
        return;
      }

      if (!EthernetHelper.matchesDestination(packet, macAddress)) {
        if (logger.isDebugEnabled()) {
          StringBuilder sb = new StringBuilder();
          logger.debug(
            sb.append("Dropped a packet not to me(")
              .append(macAddress)
              .append("): ")
              .append(packet)
              .toString()
          );
        }
        return;
      }

      IpV4Packet ipv4 = packet.get(IpV4Packet.class);
      if (ipv4 != null && !ipv4.getHeader().hasValidChecksum(true)) {
        logger.warn("Dropped an invalid IPv4 packet: " + packet);
        return;
      }

      IcmpV4CommonPacket icmpv4 = packet.get(IcmpV4CommonPacket.class);
      if (icmpv4 != null && !icmpv4.hasValidChecksum(true)) {
        logger.warn("Dropped an invalid ICMPv4 packet: " + packet);
        return;
      }

//      UdpPacket udp = packet.get(UdpPacket.class);
//      if (udp != null && !udp.hasValidChecksum(srcAddr, dstAddr, true)) {
//        logger.warn("Dropped an invalid ICMPv4 packet: " + packet);
//        return;
//      }

      if (packet.contains(IllegalPacket.class)) {
        logger.warn("Dropped a broken packet: " + packet);
        return;
      }

      host.gotPacket(packet);
    }

  }

}