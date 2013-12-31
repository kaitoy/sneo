/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network;

import java.net.Inet4Address;
import java.net.Inet6Address;
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
import org.pcap4j.packet.IcmpV6EchoRequestPacket;
import org.pcap4j.packet.IcmpV6NeighborAdvertisementPacket;
import org.pcap4j.packet.IcmpV6NeighborSolicitationPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV6Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.UnknownPacket;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.IcmpV4Code;
import org.pcap4j.packet.namednumber.IcmpV4Type;
import org.pcap4j.packet.namednumber.IcmpV6Code;
import org.pcap4j.packet.namednumber.IcmpV6Type;
import org.pcap4j.packet.namednumber.UdpPort;
import org.pcap4j.util.MacAddress;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import com.github.kaitoy.sneo.agent.FileMibAgent;
import com.github.kaitoy.sneo.network.protocol.ArpHelper;
import com.github.kaitoy.sneo.network.protocol.ArpHelper.ArpCache;
import com.github.kaitoy.sneo.network.protocol.EthernetHelper;
import com.github.kaitoy.sneo.network.protocol.IcmpV4Helper;
import com.github.kaitoy.sneo.network.protocol.IcmpV6Helper;
import com.github.kaitoy.sneo.network.protocol.IcmpV6Helper.NdpCache;
import com.github.kaitoy.sneo.network.protocol.IpV4Helper;
import com.github.kaitoy.sneo.network.protocol.IpV4Helper.IpV4RoutingTable;
import com.github.kaitoy.sneo.network.protocol.IpV4Helper.IpV4RoutingTable.IpV4RoutingTableEntry;
import com.github.kaitoy.sneo.network.protocol.IpV6Helper;
import com.github.kaitoy.sneo.network.protocol.IpV6Helper.IpV6RoutingTable;
import com.github.kaitoy.sneo.network.protocol.IpV6Helper.IpV6RoutingTable.IpV6RoutingTableEntry;
import com.github.kaitoy.sneo.network.protocol.UdpHelper;
import com.github.kaitoy.sneo.transport.SneoUdpTransportMapping;

public class Node {

  private static final LogAdapter LOGGER = LogFactory.getLogger(Node.class);

  private static final int TRAP_SRC_PORT = 54321;

  private final String name;
  private final FileMibAgent agent;
  protected final IpV4RoutingTable ipV4routingTable = IpV4Helper.newRoutingTable();
  protected final IpV6RoutingTable ipV6routingTable = IpV6Helper.newRoutingTable();
  private final int ttl;
  protected final Map<String, NetworkInterface> nifs
    = new ConcurrentHashMap<String, NetworkInterface>();
  private final ArpCache arpCache
    = ArpHelper.newArpCache(NetworkPropertiesLoader.getArpCacheLife());
  private final NdpCache ndpCache
    = IcmpV6Helper.newNdpCache(NetworkPropertiesLoader.getNdpCacheLife());
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

  public List<IpV4RoutingTableEntry> getIpV4RoutingTableEntries() {
    return ipV4routingTable.getEntries();
  }

  public List<IpV6RoutingTableEntry> getIpV6RoutingTableEntries() {
    return ipV6routingTable.getEntries();
  }

  public int getTtl() {
    return ttl;
  }

  public List<NetworkInterface> getNifs() {
    return new ArrayList<NetworkInterface>(nifs.values());
  }

  public void addNif(String ifName, boolean trunk) {
    PhysicalNetworkInterface nif
      = new PhysicalNetworkInterface(
          ifName,
          MacAddressManager.getInstance().generateVirtualMacAddress(),
          trunk,
          new PacketListenerImpl(ifName)
        );
    nifs.put(ifName, nif);
  }

  public void addRealNif(String ifName, MacAddress macAddr, String deviceName) {
    RealNetworkInterface rnif
      = new RealNetworkInterface(
          ifName,
          macAddr,
          deviceName,
          new PacketListenerImpl(ifName)
        );
    nifs.put(ifName, rnif);
  }

  public void addVlan(String ifName, int vid) {
    NetworkInterface nif
      = new VlanInterface(
          ifName,
          MacAddressManager.getInstance().generateVirtualMacAddress(),
          vid,
          new PacketListenerImpl(ifName)
        );
    nifs.put(ifName, nif);
  }

  public void addNifToVlan(String ifName, int vid) {
    getVlanNif(vid).addNif(ifName, nifs.get(ifName));
  }

  public void addLag(String name, int channelGroupNumber) {
    NetworkInterface nif
      = new LagInterface(
          name,
          MacAddressManager.getInstance().generateVirtualMacAddress(),
          channelGroupNumber,
          new PacketListenerImpl(name)
        );
    nifs.put(name, nif);
  }

  public void addNifToLag(String ifName, int channelGroupNumber) {
    getLagNif(channelGroupNumber)
      .addNif(ifName, (PhysicalNetworkInterface)nifs.get(ifName));
  }

  public void addIpAddress(
    String ifName, InetAddress addr, int prefixLength
  ) {
    NetworkInterface target = nifs.get(ifName);
    MacAddressManager.getInstance()
      .registerVirtualMacAddress(addr, target.getMacAddress());
    if (addr instanceof Inet4Address) {
      target.addIpAddress(new NifIpV4Address((Inet4Address)addr, prefixLength));
    }
    else {
      target.addIpAddress(new NifIpV6Address((Inet6Address)addr, prefixLength));
    }
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
    addIpAddress(ifName, inetAddr, prefixLength);
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

  public LagInterface getLagNif(int channelGroupNumber) {
    for (NetworkInterface nif: nifs.values()) {
      if (nif instanceof LagInterface) {
        LagInterface li = (LagInterface)nif;
        if (li.getChannelGroupNumber() == channelGroupNumber) {
          return li;
        }
      }
    }
    return null;
  }

  public void start() {
    start(true);
  }

  public void start(boolean startAgent) {
    synchronized (thisLock) {
      for (NetworkInterface nif: nifs.values()) {
        nif.start();
      }

      if (agent != null && startAgent) {
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

      ArpHelper.clearCache(arpCache);
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

    LOGGER.info("A node has been shutdown.");
  }

  protected void consumeIpV4(Packet packet, NetworkInterface getter) {
    if (packet.contains(UdpPacket.class)) {
      consumeUdp(packet, getter);
    }
    else if (packet.contains(IcmpV4EchoPacket.class)) {
      if (!listeningIcmp) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("not listening ICMP. Drop a packet: " + packet);
        }
        return;
      }
      IcmpV4Helper.reply(packet, this, getter);
    }
    else {
      LOGGER.warn("I don't care this packet: " + packet);
    }
  }

  protected void consumeIpV6(Packet packet, NetworkInterface getter) {
    if (packet.contains(UdpPacket.class)) {
      consumeUdp(packet, getter);
    }
    else if (packet.contains(IcmpV6EchoRequestPacket.class)) {
      if (!listeningIcmp) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("not listening ICMP. Drop a packet: " + packet);
        }
        return;
      }
      IcmpV6Helper.sendEchoReply(packet, this, getter);
    }
    else {
      LOGGER.warn("I don't care this packet: " + packet);
    }
  }

  private void consumeUdp(Packet packet, NetworkInterface getter) {
    if (agent == null || !agent.isRunning()) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Not listening SNMP. Dropped a packet: " + packet);
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
        .registerContext(new SnmpContext(packet, getter));
      tm.processMessage(packet);
    }
    else {
      if (LOGGER.isDebugEnabled()) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dropped a packet not to me(")
          .append(tm.getAddress().getPort())
          .append("): ")
          .append(packet);
        LOGGER.debug(sb.toString());
      }
    }
  }

  public NetworkInterface getNifByDstIpAddr(Inet4Address dstIpAddr) {
    for (NetworkInterface nif: nifs.values()) {
      if (IpV4Helper.isSameNetwork(dstIpAddr, nif)) {
        return nif;
      }
    }

    Inet4Address nextHop = IpV4Helper.getNextHop(dstIpAddr, ipV4routingTable);
    if (nextHop == null) {
      LOGGER.warn("Couldn't get next hop by dest IP address: " + dstIpAddr);
      return null;
    }

    for (NetworkInterface nif: nifs.values()) {
      if (IpV4Helper.isSameNetwork(nextHop, nif)) {
        return nif;
      }
    }

    LOGGER.error("Couldn't find a NIF for the next hop " + nextHop + ". Dest IP address: " + dstIpAddr);
    return null;
  }

  public NetworkInterface getNifByDstIpAddr(Inet6Address dstIpAddr) {
    for (NetworkInterface nif: nifs.values()) {
      if (IpV6Helper.isSameNetwork(dstIpAddr, nif)) {
        return nif;
      }
    }

    Inet6Address nextHop = IpV6Helper.getNextHop(dstIpAddr, ipV6routingTable);
    if (nextHop == null) {
      LOGGER.warn("Couldn't get next hop by dest IP address: " + dstIpAddr);
      return null;
    }

    for (NetworkInterface nif: nifs.values()) {
      if (IpV6Helper.isSameNetwork(nextHop, nif)) {
        return nif;
      }
    }

    LOGGER.error("Couldn't find a NIF for the next hop " + nextHop + ". Dest IP address: " + dstIpAddr);
    return null;
  }

  public void addIpV4Route(
    Inet4Address destination,
    Inet4Address subnetMask,
    Inet4Address gateway,
    int metric
  ) {
    ipV4routingTable.addRoute(destination, subnetMask, gateway, metric);
  }

  public void addIpV4Route(
    String destination,
    String subnetMask,
    String gateway,
    int metric
  ) {
    try {
      addIpV4Route(
        (Inet4Address)InetAddress.getByName(destination),
        (Inet4Address)InetAddress.getByName(subnetMask),
        (Inet4Address)InetAddress.getByName(gateway),
        metric
      );
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void addIpV6Route(
    Inet6Address destination,
    int prefixLength,
    Inet6Address gateway,
    int metric
  ) {
    ipV6routingTable.addRoute(destination, prefixLength, gateway, metric);
  }

  public void addIpV6Route(
    String destination,
    int prefixLength,
    String gateway,
    int metric
  ) {
    try {
      addIpV6Route(
        (Inet6Address)InetAddress.getByName(destination),
        prefixLength,
        (Inet6Address)InetAddress.getByName(gateway),
        metric
      );
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void sendL3Packet(Packet packet, NetworkInterface sender) throws SendPacketException {
    MacAddress dstMacAddr = getDstMacAddress(packet, sender);

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
      LOGGER.error(e);
    }
  }

  private MacAddress getDstMacAddress(
    Packet packet, NetworkInterface sender
  ) throws SendPacketException {
    if (packet instanceof IpV4Packet) {
      Inet4Address nextHop;
      Inet4Address dstIpAddr
        = packet.get(IpV4Packet.class).getHeader().getDstAddr();

      if (IpV4Helper.isSameNetwork(dstIpAddr, sender)) {
        nextHop = dstIpAddr;
      }
      else {
        nextHop = IpV4Helper.getNextHop(dstIpAddr, ipV4routingTable);
        if (nextHop == null) {
          LOGGER.debug("Couldn't get next hop: " + packet);
          throw new SendPacketException(
                  IcmpV4Type.DESTINATION_UNREACHABLE,
                  IcmpV4Code.DST_NETWORK_UNKNOWN
                );
        }
      }

      if (sender instanceof RealNetworkInterface){
        MacAddress dstMacAddr
          = ArpHelper.resolveRealAddress(
              nextHop,
              this,
              sender,
              arpCache,
              NetworkPropertiesLoader.getArpResolveTimeout(),
              TimeUnit.MILLISECONDS
            );

        if (dstMacAddr == null) {
          LOGGER.debug(
            "Couldn't resolve an IPv4 address to a Mac address, dropped the packet: "
              + packet
          );
          throw new SendPacketException(
                  IcmpV4Type.DESTINATION_UNREACHABLE,
                  IcmpV4Code.HOST_UNREACHABLE
                );
        }

        return dstMacAddr;
      }
      else {
        MacAddress dstMacAddr = ArpHelper.resolveVirtualAddress(nextHop);
        if (dstMacAddr == null) {
          throw new SendPacketException(
                  IcmpV4Type.DESTINATION_UNREACHABLE,
                  IcmpV4Code.HOST_UNREACHABLE
                );
        }

        return dstMacAddr;
      }
    }
    else if (packet instanceof ArpPacket) {
      return ((ArpPacket)packet).getHeader().getDstHardwareAddr();
    }
    else if (packet instanceof IpV6Packet) {
      if (packet.contains(IcmpV6NeighborSolicitationPacket.class)) {
        Inet6Address dstAddr = packet.get(IpV6Packet.class).getHeader().getDstAddr();
        return IpV6Helper.generateNeighborSolicitationMacAddress(dstAddr);
      }
      else {
        Inet6Address nextHop;
        Inet6Address dstIpAddr
          = packet.get(IpV6Packet.class).getHeader().getDstAddr();

        if (IpV6Helper.isSameNetwork(dstIpAddr, sender)) {
          nextHop = dstIpAddr;
        }
        else {
          nextHop = IpV6Helper.getNextHop(dstIpAddr, ipV6routingTable);
          if (nextHop == null) {
            LOGGER.debug("Couldn't get next hop: " + packet);
            throw new SendPacketException(
                    IcmpV6Type.DESTINATION_UNREACHABLE,
                    IcmpV6Code.NO_ROUTE_TO_DST
                  );
          }
        }

        if (sender instanceof RealNetworkInterface){
          MacAddress dstMacAddr
            = IcmpV6Helper.resolveRealAddress(
                nextHop,
                this,
                sender,
                ndpCache,
                NetworkPropertiesLoader.getNdpResolveTimeout(),
                TimeUnit.MILLISECONDS
              );

          if (dstMacAddr == null) {
            LOGGER.warn(
              "Couldn't resolve an IPv6 address to a Mac address, dropped the packet: "
                + packet
            );
            throw new SendPacketException(
                    IcmpV4Type.DESTINATION_UNREACHABLE,
                    IcmpV4Code.HOST_UNREACHABLE
                  );
          }

          return dstMacAddr;
        }
        else {
          MacAddress dstMacAddr = IcmpV6Helper.resolveVirtualAddress(nextHop);
          if (dstMacAddr == null) {
            throw new SendPacketException(
                    IcmpV6Type.DESTINATION_UNREACHABLE,
                    IcmpV6Code.NO_ROUTE_TO_DST
                  );
          }

          return dstMacAddr;
        }
      }
    }
    else {
      LOGGER.debug(
        "Cannot get a destination MAC address for an unsupported packet: "
          + packet
      );
      throw new SendPacketException(
              IcmpV4Type.DESTINATION_UNREACHABLE,
              IcmpV4Code.DST_NETWORK_UNKNOWN
            );
    }
  }

  public void sendL4Packet(
    Packet packet,
    InetAddress srcAddr,
    InetAddress dstAddr,
    NetworkInterface sender,
    int ttl
  ) throws SendPacketException {
    Packet l3Packet;
    if (srcAddr instanceof Inet4Address) {
      l3Packet = IpV4Helper.pack(
                   packet,
                   (Inet4Address)srcAddr,
                   (Inet4Address)dstAddr,
                   ttl,
                   getNextIdentification()
                 );
    }
    else {
      l3Packet = IpV6Helper.pack(
                   packet,
                   (Inet6Address)srcAddr,
                   (Inet6Address)dstAddr,
                   ttl,
                   getNextIdentification()
                 );
    }

    sendL3Packet(l3Packet, sender);
  }

  public void sendL4Packet(
    Packet packet,
    InetAddress srcAddr,
    InetAddress dstAddr,
    NetworkInterface sender
  ) throws SendPacketException {
    sendL4Packet(
      packet,
      srcAddr,
      dstAddr,
      sender,
      ttl
    );
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
    InetAddress srcAddr;
    int srcPort;
    NetworkInterface sender;

    SnmpContext context = agent.getContextfulWorkerPool().unregisterContext();
    if (context != null) {
      // response
      sender = context.getGetter();
      IpV4Packet ipV4Packet = context.getRequestPacket().get(IpV4Packet.class);
      if (ipV4Packet != null) {
        srcAddr = context.getRequestPacket().get(IpV4Packet.class).getHeader()
                    .getDstAddr();
      }
      else {
        srcAddr = context.getRequestPacket().get(IpV6Packet.class).getHeader()
                    .getDstAddr();
      }
      srcPort = context.getRequestPacket().get(UdpPacket.class).getHeader()
                  .getDstPort().value() & 0xFFFF;
    }
    else {
      // trap
      if (dstAddr instanceof Inet4Address) {
        sender = getNifByDstIpAddr((Inet4Address)dstAddr);
      }
      else {
        sender = getNifByDstIpAddr((Inet6Address)dstAddr);
      }
      srcAddr = agent.getInetAddress();
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
        handleIpV4(packet, getter);
      }
      if (packet.contains(IpV6Packet.class)) {
        if (packet.contains(IcmpV6NeighborSolicitationPacket.class)) {
          handleIcmpV6Ns(packet, getter);
        }
        else if (packet.contains(IcmpV6NeighborAdvertisementPacket.class)) {
          handleIcmpV6Na(packet, getter);
        }
        else {
          handleIpV6(packet, getter);
        }
      }
      else if (packet.contains(ArpPacket.class)) {
        handleArp(packet, getter);
      }
    }

    private void handleIpV4(Packet packet, NetworkInterface getter) {
      for (NetworkInterface nif: nifs.values()) {
        if (IpV4Helper.matchesDestination(packet, nif)) {
          consumeIpV4(packet, getter);
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

    private void handleIcmpV6Ns(Packet packet, NetworkInterface getter) {
      IcmpV6Helper.cache(packet, ndpCache);

      IcmpV6NeighborSolicitationPacket nsPacket
        = packet.get(IcmpV6NeighborSolicitationPacket.class);
      boolean toMe = false;
      for (NifIpAddress nifIpAddr: getter.getIpAddresses()) {
        if (nifIpAddr.getIpAddr().equals(nsPacket.getHeader().getTargetAddress())) {
          toMe = true;
          break;
        }
      }
      if (!toMe) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Dropped an NS packet not to me: " + packet);
        }
        return;
      }

      IcmpV6Helper.sendSolicitedNeighborAdvertisement(
        packet, Node.this, getter
      );
    }

    private void handleIcmpV6Na(Packet packet, NetworkInterface getter) {
      IcmpV6Helper.cache(packet, ndpCache);
    }

    private void handleIpV6(Packet packet, NetworkInterface getter) {
      for (NetworkInterface nif: nifs.values()) {
        if (IpV6Helper.matchesDestination(packet, nif)) {
          consumeIpV6(packet, getter);
          return;
        }
      }

      IpV6Packet ipV6packet = packet.get(IpV6Packet.class);
      NetworkInterface sender
        = getNifByDstIpAddr(ipV6packet.getHeader().getDstAddr());

      try {
        ipV6packet = IpV6Helper.decrementTtl(ipV6packet);
      } catch (TimeoutException e) {
        IcmpV6Helper.sendErrorMessage(
          IcmpV6Type.TIME_EXCEEDED,
          IcmpV6Code.HOP_LIMIT_EXCEEDED,
          ipV6packet,
          Node.this,
          getter
        );
        return;
      }

      try {
        sendL3Packet(ipV6packet, sender);
      } catch (SendPacketException e) {
        // TODO 自動生成された catch ブロック
        e.printStackTrace();
      }
    }

    private void handleArp(Packet packet, NetworkInterface getter) {
      ArpHeader ah = packet.get(ArpPacket.class).getHeader();

      boolean toMe = false;
      for (NifIpAddress nifIpAddr: getter.getIpAddresses()) {
        if (nifIpAddr.getIpAddr().equals(ah.getDstProtocolAddr())) {
          toMe = true;
          break;
        }
      }
      if (!toMe) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Dropped an ARP packet not to me: " + packet);
        }
        return;
      }

      ArpOperation op = ah.getOperation();
      if (op.equals(ArpOperation.REQUEST)) {
        ArpHelper.cache(packet, arpCache);
        ArpHelper.reply(
          packet,
          Node.this,
          getter
        );
      }
      else if (op.equals(ArpOperation.REPLY)) {
        ArpHelper.cache(packet, arpCache);
      }
      else {
        LOGGER.warn(
          "Dropped a packet with unknown ARP operation: " + packet
        );
      }
    }

  }

}
