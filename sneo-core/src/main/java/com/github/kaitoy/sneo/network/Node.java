/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.ArpPacket.ArpHeader;
import org.pcap4j.packet.IcmpV4EchoPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.UnknownPacket;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.IcmpV4Code;
import org.pcap4j.packet.namednumber.IcmpV4Type;
import org.pcap4j.packet.namednumber.UdpPort;
import org.pcap4j.util.MacAddress;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import com.github.kaitoy.sneo.agent.FileMibAgent;
import com.github.kaitoy.sneo.network.protocol.ArpHelper;
import com.github.kaitoy.sneo.network.protocol.EthernetHelper;
import com.github.kaitoy.sneo.network.protocol.IcmpV4Helper;
import com.github.kaitoy.sneo.network.protocol.IpV4Helper;
import com.github.kaitoy.sneo.network.protocol.UdpHelper;
import com.github.kaitoy.sneo.network.protocol.ArpHelper.ArpTable;
import com.github.kaitoy.sneo.network.protocol.IpV4Helper.RoutingTable;
import com.github.kaitoy.sneo.network.protocol.IpV4Helper.RoutingTable.RoutingTableEntry;
import com.github.kaitoy.sneo.transport.SneoUdpTransportMapping;

public class Node {

  private static final LogAdapter logger = LogFactory.getLogger(Node.class);

  private static final int TRAP_SRC_PORT = 54321;

  private final String name;
  private final FileMibAgent agent;
  protected final RoutingTable routingTable = IpV4Helper.newRoutingTable();
  private final int ttl;
  protected final Map<String, NetworkInterface> nifs
    = new ConcurrentHashMap<String, NetworkInterface>();
  private final ArpTable arpTable
    = ArpHelper.newArpTable(NetworkPropertiesLoader.getArpCacheLife());
  private final Object thisLock = new Object();
  private final Object ipV4PacketIdLock = new Object();

  private short ipV4PacketId = (short)0;
  private volatile boolean running = false;
  private volatile boolean listeningIcmp = false;

  public Node(String name, FileMibAgent agent, int ttl) {
    if (agent != null) {
      if (!(agent.getTransportMapping() instanceof SneoUdpTransportMapping)) {
        throw new IllegalArgumentException();
      }
      ((SneoUdpTransportMapping)agent.getTransportMapping()).setNode(this);
    }

    this.name = name;
    this.agent = agent;
    this.ttl = ttl;
  }

  public String getName() {
    return name;
  }

  public FileMibAgent getAgent() {
    return agent;
  }

  public List<RoutingTableEntry> getRoutingTableEntries() {
    return routingTable.getEntries();
  }

  public int getTtl() {
    return ttl;
  }

  List<NetworkInterface> getNifs() {
    return new ArrayList<NetworkInterface>(nifs.values());
  }

  public void addNif(String ifName, InetAddress ipAddr, InetAddress subnetMask) {
    PhysicalNetworkInterface nif
      = new PhysicalNetworkInterface(
          ifName,
          MacAddressManager.getInstance().generateVirtualMacAddress(),
          ipAddr,
          subnetMask,
          new PacketListenerImpl(ifName)
        );

    if (ipAddr != null) {
      MacAddressManager.getInstance()
        .registerVirtualMacAddress(ipAddr, nif.getMacAddress());
    }

    nifs.put(ifName, nif);
  }

  public void addNif(String ifName, String ipAddr, int prefixLength) {
    InetAddress inetAddr;
    try {
      inetAddr = InetAddress.getByName(ipAddr);
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException(e);
    }
    addNif(ifName, inetAddr, IpV4Helper.getSubnetMaskFrom(prefixLength));
  }

  public void addRealNif(String ifName, MacAddress macAddr, InetAddress ipAddr, InetAddress mask, String deviceName) {
    RealNetworkInterface rnif
      = new RealNetworkInterface(
          ifName,
          macAddr,
          ipAddr,
          mask,
          deviceName,
          new PacketListenerImpl(ifName)
        );
    nifs.put(ifName, rnif);
  }

  public void addRealNif(String ifName, String macAddr, String ipAddr, int prefixLength, String deviceName) {
    InetAddress inetAddr;
    try {
      inetAddr = InetAddress.getByName(ipAddr);
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException(e);
    }
    addRealNif(
      ifName,
      MacAddress.getByName(macAddr),
      inetAddr,
      IpV4Helper.getSubnetMaskFrom(prefixLength),
      deviceName
    );
  }

  public void addVlan(
    String ifName, InetAddress ipAddr, InetAddress subnetMask, int vid
  ) {
    NetworkInterface nif
      = new VlanInterface(
          ifName,
          MacAddressManager.getInstance().generateVirtualMacAddress(),
          ipAddr,
          subnetMask,
          vid,
          new PacketListenerImpl(ifName)
        );

    if (ipAddr != null) {
      MacAddressManager.getInstance()
        .registerVirtualMacAddress(ipAddr, nif.getMacAddress());
    }

    nifs.put(ifName, nif);
  }

  public void addVlan(String ifName, String ipAddr, int prefixLength, int vid) {
    InetAddress inetAddr;
    try {
      inetAddr = InetAddress.getByName(ipAddr);
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException(e);
    }
    addVlan(ifName, inetAddr, IpV4Helper.getSubnetMaskFrom(prefixLength), vid);
  }

  public void addNifToVlan(String ifName, int vid, boolean tagged) {
    getVlanNif(vid).addNif(ifName, nifs.get(ifName), tagged);
  }

  public void addIpAddress(
    String ifName, InetAddress addr, InetAddress subnetMask
  ) {
    String name = ifName + ":" + addr;

    MacAddress macAddr
      = MacAddressManager.getInstance().generateVirtualMacAddress();
    MacAddressManager.getInstance()
      .registerVirtualMacAddress(addr, macAddr);

    NetworkInterface nif = nifs.get(ifName);

    nifs.put(
      name,
      new AliasNetworkInterface(
        name,
        macAddr,
        addr,
        subnetMask,
        nif,
        new PacketListenerImpl(name)
      )
    );
  }

  public void addIpAddress(
    String ifName, String addr, int prefixLength
  ) {
    InetAddress inetAddr;
    try {
      inetAddr = InetAddress.getByName(addr);
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException(e);
    }
    addIpAddress(ifName, inetAddr, IpV4Helper.getSubnetMaskFrom(prefixLength));
  }

  public NetworkInterface getNif(String ifName) {
    return nifs.get(ifName);
  }

  public VlanInterface getVlanNif(int vid) {
    for (NetworkInterface nif: nifs.values()) {
      if (nif instanceof VlanInterface) {
        VlanInterface vNif = (VlanInterface)nif;
        if (vNif.getVid() == vid) {
          return vNif;
        }
      }
    }
    return null;
  }

  public void addDefaultRoute(Inet4Address gwAddr) {
    routingTable.addDefaultRoute(gwAddr);
  }

  public void start() {
    synchronized (thisLock) {
      for (NetworkInterface nif: nifs.values()) {
        nif.start();
      }

      if (agent != null) {
        agent.start();
      }

      listeningIcmp = true;
      running = true;
    }
  }

  public void startAgent() {
    if (agent != null) {
      agent.start();
    }
  }

  public void startListeningIcmp() {
    listeningIcmp = true;
  }

  public void stop() {
    synchronized (thisLock) {
      for (NetworkInterface nif: nifs.values()) {
        nif.stop();
      }

      if (agent != null) {
        agent.stop();
      }

      ArpHelper.clearTable(arpTable);
      listeningIcmp = false;
      running = false;
    }
  }

  public void stopAgent() {
    if (agent != null) {
      agent.stop();
    }
  }

  public void stopListeningIcmp() {
    listeningIcmp = false;
  }

  public boolean isRunning() {
    return running;
  }

  public boolean isRunningAgent() {
    return agent != null ? agent.isRunning() : false;
  }

  public boolean isListeningIcmp() {
    return listeningIcmp;
  }

  public void shutdown() {
    synchronized (thisLock) {
      if (running) {
        stop();
      }

      if (agent != null) {
        agent.shutdown();
      }

      for (NetworkInterface n: nifs.values()) {
        n.shutdown();
      }
    }

    logger.info("shutdowned");
  }

  protected void process(Packet packet, NetworkInterface getter) {
    if (packet.contains(UdpPacket.class)) {
      if (agent == null || !agent.isRunning()) {
        if (logger.isDebugEnabled()) {
          logger.debug("not listening SNMP. Drop a packet: " + packet);
        }
        return;
      }

      UdpPacket udpPacket = packet.get(UdpPacket.class);
      SneoUdpTransportMapping tm
        = (SneoUdpTransportMapping)agent.getTransportMapping();

      if (
        (udpPacket.getHeader().getDstPort().value() & 0xFFFF)
          == tm.getAddress().getPort()
      ) {
        agent.getContextfulWorkerPool()
          .registerContext(new SneoContext(packet, getter));
        tm.processMessage(packet);
      }
    }
    else if (packet.contains(IcmpV4EchoPacket.class)) {
      if (!listeningIcmp) {
        if (logger.isDebugEnabled()) {
          logger.debug("not listening ICMP. Drop a packet: " + packet);
        }
        return;
      }
      IcmpV4Helper.reply(packet, this, getter);
    }
    else {
      logger.warn("Can't process a packet: " + packet);
    }
  }

  public NetworkInterface getNifByDstIpAddr(Inet4Address dstIpAddr) {
    for (NetworkInterface nif: nifs.values()) {
      if (nif.getIpAddress() == null) {
        continue;
      }

      if (
        IpV4Helper.isSameNetwork(
          dstIpAddr,
          (Inet4Address)nif.getIpAddress(),
          (Inet4Address)nif.getSubnetMask()
        )
      ) {
        return nif;
      }
    }

    Inet4Address nextHop = IpV4Helper.getNextHop(dstIpAddr, routingTable);
    if (nextHop == null) {
      logger.warn("Couldn't get next hop from dst IP address: " + dstIpAddr);
      // TODO throw new SendPacketException
      return null;
    }

    for (NetworkInterface nif: nifs.values()) {
      if (nif.getIpAddress() == null) {
        continue;
      }

      if (
        IpV4Helper.isSameNetwork(
          nextHop,
          (Inet4Address)nif.getIpAddress(),
          (Inet4Address)nif.getSubnetMask()
        )
      ) {
        return nif;
      }
    }

    logger.error("Couldn't get NIF for dst IP address: " + dstIpAddr);
    // TODO throw new SendPacketException
    return null;
  }

  public void addRoute(
    Inet4Address destination,
    Inet4Address subnetMask,
    Inet4Address gateway,
    int metric
  ) {
    routingTable.addRoute(destination, subnetMask, gateway, metric);
  }

  public void addRoute(
    String destination,
    String subnetMask,
    String gateway,
    int metric
  ) {
    try {
      addRoute(
        (Inet4Address)InetAddress.getByName(destination),
        (Inet4Address)InetAddress.getByName(subnetMask),
        (Inet4Address)InetAddress.getByName(gateway),
        metric
      );
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void sendL3Packet(Packet packet, NetworkInterface sender) throws SendPacketException {
    MacAddress dstMacAddr;

    if (packet instanceof IpV4Packet) {
      Inet4Address nextHop;
      Inet4Address dstIpAddr
        = packet.get(IpV4Packet.class).getHeader().getDstAddr();

      if (
        IpV4Helper.isSameNetwork(
          dstIpAddr,
          (Inet4Address)sender.getIpAddress(),
          (Inet4Address)sender.getSubnetMask()
        )
      ) {
        nextHop = dstIpAddr;
      }
      else {
        nextHop = IpV4Helper.getNextHop(dstIpAddr, routingTable);
      }

      if (nextHop == null) {
        logger.warn("Couldn't get next hop: " + packet);
        return;
      }

      if (sender instanceof RealNetworkInterface){
        dstMacAddr
          = ArpHelper.resolveRealAddress(
              nextHop,
              this,
              sender,
              arpTable,
              NetworkPropertiesLoader.getArpResolveTimeout(),
              TimeUnit.MILLISECONDS
            );

        if (dstMacAddr == null) {
          logger.warn(
            "Couldn't resolve IP address to Mac address, dropped the packet: "
              + packet
          );
          return;
        }
      }
      else {
        dstMacAddr = ArpHelper.resolveVirtualAddress(nextHop);
        if (dstMacAddr == null) {
          throw new SendPacketException(
                  IcmpV4Type.DESTINATION_UNREACHABLE,
                  IcmpV4Code.HOST_UNREACHABLE
                );
        }
      }
    }
    else if (packet instanceof ArpPacket) {
      dstMacAddr = ((ArpPacket)packet).getHeader().getDstHardwareAddr();
    }
    else {
      throw new AssertionError("Never get here");
    }

    try {
      if (
           packet instanceof IpV4Packet
        && sender instanceof RealNetworkInterface
      ) {
        List<IpV4Packet> fragmentedPackets
          = org.pcap4j.util.IpV4Helper.fragment(
              (IpV4Packet)packet, NetworkPropertiesLoader.getMtu()
            );
        for (Packet p: fragmentedPackets) {
          sender.sendPacket(
            EthernetHelper.pack(
              p,
              sender.getMacAddress(),
              dstMacAddr
            )
          );
        }
      }
      else {
        sender.sendPacket(
          EthernetHelper.pack(
            packet,
            sender.getMacAddress(),
            dstMacAddr
          )
        );
      }
    } catch (IllegalStateException e) {
      logger.error(e);
    }
  }

  public void sendL4Packet(
    Packet packet,
    InetAddress srcAddr,
    InetAddress dstAddr,
    NetworkInterface sender
  ) throws SendPacketException {
    if (srcAddr instanceof Inet4Address) {
      sendL3Packet(
        IpV4Helper.pack(
          packet,
          (Inet4Address)srcAddr,
          (Inet4Address)dstAddr,
          ttl,
          getNextIdentification()
        ),
        sender
      );
    }
  }

  private short getNextIdentification() {
    synchronized (ipV4PacketIdLock) {
      ipV4PacketId = (short)(((0xFFFF) & ipV4PacketId) + 1);
      return ipV4PacketId;
    }
  }

  public void sendSnmpMessage(
    InetAddress dstAddr, int dstPort,
    byte[] message
  ) throws SendPacketException {
    Inet4Address srcAddr;
    int srcPort;
    NetworkInterface sender;

    SneoContext context = agent.getContextfulWorkerPool().unregisterContext();
    if (context != null) {
      sender = context.getGetter();
      srcAddr = context.getRequestPacket().get(IpV4Packet.class).getHeader()
                  .getDstAddr();
      srcPort = context.getRequestPacket().get(UdpPacket.class).getHeader()
                  .getDstPort().value() & 0xFFFF;
    }
    else {
      sender = getNifByDstIpAddr((Inet4Address)dstAddr);
      srcAddr = (Inet4Address)sender.getIpAddress();
      srcPort = TRAP_SRC_PORT;
    }
    UnknownPacket.Builder unknownb = new UnknownPacket.Builder();
    unknownb.rawData(message);

    UdpPacket udpp
      = UdpHelper.pack(
          unknownb.build(),
          UdpPort.getInstance((short)srcPort),
          UdpPort.getInstance((short)dstPort),
          srcAddr,
          dstAddr
        );

    sendL4Packet(udpp, srcAddr, dstAddr, sender);
  }

  private final class PacketListenerImpl implements PacketListener {

    private final String ifName;

    private PacketListenerImpl(String ifName) {
      this.ifName = ifName;
    }

    public void gotPacket(Packet packet) {
      NetworkInterface getter = nifs.get(ifName);
      if (packet.contains(IpV4Packet.class)) {
        for (NetworkInterface nif: nifs.values()) {
          if (nif.getIpAddress() == null) {
            continue;
          }

          if (
            IpV4Helper.matchesDestination(
              packet,
              (Inet4Address)nif.getIpAddress(),
              (Inet4Address)nif.getSubnetMask()
            )
          ) {
            process(packet, getter);
            return;
          }
        }

        IpV4Packet ipV4packet = packet.get(IpV4Packet.class);
        NetworkInterface sender
          = getNifByDstIpAddr(ipV4packet.getHeader().getDstAddr());

        try {
          ipV4packet = IpV4Helper.decrementTtl(ipV4packet);
        } catch (TimeoutException e) {
          IcmpV4Helper.sendErrorMessage(
            IcmpV4Type.TIME_EXCEEDED,
            IcmpV4Code.TIME_TO_LIVE_EXCEEDED,
            ipV4packet,
            Node.this,
            getter
          );
          return;
        }

        try {
          sendL3Packet(ipV4packet, sender);
        } catch (SendPacketException e) {
          // TODO 自動生成された catch ブロック
          e.printStackTrace();
        }
      }
      else if (packet.contains(ArpPacket.class)) {
        ArpHeader ah = packet.get(ArpPacket.class).getHeader();

        if (!getter.getIpAddress().equals(ah.getDstProtocolAddr())) {
          if (logger.isDebugEnabled()) {
            logger.debug("Dropped an ARP packet not to me: " + packet);
          }
          return;
        }

        ArpOperation op = ah.getOperation();
        if (op.equals(ArpOperation.REQUEST)) {
          ArpHelper.cache(packet, arpTable);
          ArpHelper.reply(
            packet,
            getter.getMacAddress(),
            Node.this,
            getter
          );
        }
        else if (op.equals(ArpOperation.REPLY)) {
          ArpHelper.cache(packet, arpTable);
        }
        else {
          logger.warn(
            "Dropped a packet with unknown ARP operation: " + packet
          );
        }

        return;
      }
    }

  }

}
