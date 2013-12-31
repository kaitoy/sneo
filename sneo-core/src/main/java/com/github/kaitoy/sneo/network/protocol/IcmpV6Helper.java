/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network.protocol;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.pcap4j.packet.IcmpV6CommonPacket;
import org.pcap4j.packet.IcmpV6CommonPacket.IpV6NeighborDiscoveryOption;
import org.pcap4j.packet.IcmpV6DestinationUnreachablePacket;
import org.pcap4j.packet.IcmpV6EchoReplyPacket;
import org.pcap4j.packet.IcmpV6EchoRequestPacket;
import org.pcap4j.packet.IcmpV6NeighborAdvertisementPacket;
import org.pcap4j.packet.IcmpV6NeighborSolicitationPacket;
import org.pcap4j.packet.IcmpV6TimeExceededPacket;
import org.pcap4j.packet.IpV6NeighborDiscoverySourceLinkLayerAddressOption;
import org.pcap4j.packet.IpV6NeighborDiscoveryTargetLinkLayerAddressOption;
import org.pcap4j.packet.IpV6Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.SimpleBuilder;
import org.pcap4j.packet.namednumber.IcmpV6Code;
import org.pcap4j.packet.namednumber.IcmpV6Type;
import org.pcap4j.packet.namednumber.IpV6NeighborDiscoveryOptionType;
import org.pcap4j.util.MacAddress;
import com.github.kaitoy.sneo.network.NetworkInterface;
import com.github.kaitoy.sneo.network.NetworkPropertiesLoader;
import com.github.kaitoy.sneo.network.NifIpAddress;
import com.github.kaitoy.sneo.network.NifIpV6Address;
import com.github.kaitoy.sneo.network.Node;
import com.github.kaitoy.sneo.network.SendPacketException;

public final class IcmpV6Helper extends NeighborDiscoveryHelper {

  private static NdpRequestSender ndpReqSender = new NdpRequestSender();

  private IcmpV6Helper() { throw new AssertionError(); }

  public static NdpCache newNdpCache(long cacheLife) {
    return new NdpCache(cacheLife);
  }

  public static MacAddress resolveVirtualAddress(InetAddress ipAddress) {
    return NeighborDiscoveryHelper.resolveVirtualAddress(ipAddress);
  }

  public static MacAddress resolveRealAddress(
    InetAddress targetIpAddr,
    Node node,
    NetworkInterface nif,
    NdpCache ndpCache,
    long resolveTimeout,
    TimeUnit unit
  ) {
    return resolveRealAddress(
      ndpReqSender,
      targetIpAddr,
      node,
      nif,
      ndpCache,
      resolveTimeout,
      unit
    );
  }

  public static void cache(Packet packet, NdpCache ndpCache) {
    if (packet.contains(IcmpV6NeighborSolicitationPacket.class)) {
      cacheByNs(packet, ndpCache);
    }
    else if (packet.contains(IcmpV6NeighborAdvertisementPacket.class)) {
      cacheByNa(packet, ndpCache);
    }
  }

  private static void cacheByNs(Packet packet, NdpCache ndpCache) {
    IpV6Packet ipv6Packet = packet.get(IpV6Packet.class);
    if (ipv6Packet == null) {
      return;
    }

    Inet6Address srcAddr = ipv6Packet.getHeader().getSrcAddr();
    if (srcAddr.equals(IpV6Helper.UNSPECIFIED_ADDRESS)) {
      return;
    }

    IcmpV6NeighborSolicitationPacket nsPacket
      = packet.get(IcmpV6NeighborSolicitationPacket.class);
    IpV6NeighborDiscoverySourceLinkLayerAddressOption srcLinkOpt = null;
    for (IpV6NeighborDiscoveryOption opt: nsPacket.getHeader().getOptions()) {
      if (opt.getType().equals(IpV6NeighborDiscoveryOptionType.SOURCE_LINK_LAYER_ADDRESS)) {
        srcLinkOpt = (IpV6NeighborDiscoverySourceLinkLayerAddressOption)opt;
        break;
      }
    }
    if (srcLinkOpt == null) {
      return;
    }

    // if new, isRouter flag of Neighbor Cache is false.
    // if update, isRouter is not changed.
    NeighborDiscoveryHelper.cache(ndpCache, srcAddr, srcLinkOpt.getLinkLayerAddressAsMacAddress());
  }

  private static void cacheByNa(Packet packet, NdpCache ndpCache) {
    IcmpV6NeighborAdvertisementPacket naPacket
      = packet.get(IcmpV6NeighborAdvertisementPacket.class);

    InetAddress ipAddr = naPacket.getHeader().getTargetAddress();
    MacAddress macAddr = null;
    for (IpV6NeighborDiscoveryOption opt: naPacket.getHeader().getOptions()) {
      if (opt.getType().equals(IpV6NeighborDiscoveryOptionType.TARGET_LINK_LAYER_ADDRESS)) {
        macAddr
          = ((IpV6NeighborDiscoveryTargetLinkLayerAddressOption)opt).getLinkLayerAddressAsMacAddress();
      }
    }
    if (macAddr == null) {
      return;
    }

    // According to RFC 2461, If no entry exists, the advertisement SHOULD be silently discarded.
    // But cache it here with no check for entry existance.
    NeighborDiscoveryHelper.cache(ndpCache, ipAddr, macAddr);
  }

  public static void sendSolicitedNeighborAdvertisement(
    Packet invokingPacket,Node node, NetworkInterface nif
  ) {
    IpV6Packet ipPacket = invokingPacket.get(IpV6Packet.class);
    IcmpV6NeighborSolicitationPacket nsPacket
      = invokingPacket.get(IcmpV6NeighborSolicitationPacket.class);
    if (nsPacket == null || ipPacket == null) {
      throw new IllegalArgumentException(invokingPacket.toString());
    }

    Inet6Address nsSrcIpAddr
      = ipPacket.getHeader().getSrcAddr();
    Inet6Address nsTargetAddr
      = nsPacket.getHeader().getTargetAddress();

    List<IpV6NeighborDiscoveryOption> opts = new ArrayList<IpV6NeighborDiscoveryOption>();
    IpV6NeighborDiscoveryTargetLinkLayerAddressOption.Builder optBuilder
      = new IpV6NeighborDiscoveryTargetLinkLayerAddressOption.Builder();
    optBuilder.linkLayerAddress(nif.getMacAddress().getAddress())
      .correctLengthAtBuild(true);
    opts.add(optBuilder.build());
    IcmpV6NeighborAdvertisementPacket.Builder naBuilder
      = new IcmpV6NeighborAdvertisementPacket.Builder();
    naBuilder
      .routerFlag(true)
      .overrideFlag(true)
      .targetAddress(nsTargetAddr)
      .options(opts);

    Inet6Address naDstIpAddr;
    if (nsSrcIpAddr.equals(IpV6Helper.UNSPECIFIED_ADDRESS)) {
      naBuilder.solicitedFlag(false);
      naDstIpAddr = IpV6Helper.LINK_LOCAL_ALL_NODES_ADDRESS;
    }
    else {
      naBuilder.solicitedFlag(true);
      naDstIpAddr = nsSrcIpAddr;
    }

    IcmpV6CommonPacket.Builder icmpV6Common = new IcmpV6CommonPacket.Builder();
    icmpV6Common.type(IcmpV6Type.NEIGHBOR_ADVERTISEMENT)
                .code(IcmpV6Code.NO_CODE)
                .payloadBuilder(naBuilder)
                .srcAddr(nsTargetAddr)
                .dstAddr(naDstIpAddr)
                .correctChecksumAtBuild(true);

    try {
      node.sendL4Packet(
        icmpV6Common.build(),
        nsTargetAddr,
        naDstIpAddr,
        nif,
        255
      );
    } catch (SendPacketException e) {
      // TODO handle the exception.
      e.printStackTrace();
    }
  }

  public static void sendEchoReply(Packet packet, Node node, NetworkInterface nif) {
    IcmpV6EchoRequestPacket echo = packet.get(IcmpV6EchoRequestPacket.class);
    Packet.Builder outer = packet.getBuilder().getOuterOf(IcmpV6EchoRequestPacket.Builder.class);
    IpV6Packet ipv6 = packet.get(IpV6Packet.class);

    if (
         echo == null
      || ipv6 == null
      || outer == null
      || !(outer instanceof IcmpV6CommonPacket.Builder)
    ) {
      throw new IllegalArgumentException(packet.toString());
    }

    IcmpV6EchoReplyPacket.Builder repb = new IcmpV6EchoReplyPacket.Builder();
    repb.identifier(echo.getHeader().getIdentifier())
        .sequenceNumber(echo.getHeader().getSequenceNumber())
        .payloadBuilder(new SimpleBuilder(echo.getPayload()));

    Inet6Address repSrcAddr = ipv6.getHeader().getDstAddr();
    Inet6Address repDstAddr = ipv6.getHeader().getSrcAddr();
    ((IcmpV6CommonPacket.Builder)outer)
      .type(IcmpV6Type.ECHO_REPLY)
      .srcAddr(repSrcAddr)
      .dstAddr(repDstAddr)
      .payloadBuilder(repb)
      .correctChecksumAtBuild(true);

    try {
      node.sendL4Packet(
        outer.build(),
        repSrcAddr,
        repDstAddr,
        nif
      );
    } catch (SendPacketException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
  }

  public static void sendErrorMessage(
    IcmpV6Type type,
    IcmpV6Code code,
    Packet packet,
    Node node,
    NetworkInterface nif
  ) {
    IpV6Packet ipv6Packet = packet.get(IpV6Packet.class);
    if (ipv6Packet == null) {
      throw new IllegalArgumentException(packet.toString());
    }

    Packet.Builder icmpV6Inet;
    int size = NetworkPropertiesLoader.getMtu() - 40 - 8;
    if (type.equals(IcmpV6Type.DESTINATION_UNREACHABLE)) {
      icmpV6Inet
        = new IcmpV6DestinationUnreachablePacket.Builder()
            .payload(
               org.pcap4j.util.IcmpV6Helper
                 .makePacketForInvokingPacketField(ipv6Packet, size)
             );
    }
    else if (type.equals(IcmpV6Type.TIME_EXCEEDED)) {
      icmpV6Inet
        = new IcmpV6TimeExceededPacket.Builder()
            .payload(
               org.pcap4j.util.IcmpV6Helper
                 .makePacketForInvokingPacketField(ipv6Packet, size)
             );
    }
    else {
      throw new IllegalArgumentException(packet.toString());
    }

    Inet6Address errDstAddr = ipv6Packet.getHeader().getSrcAddr();
    Inet6Address errSrcAddr = null;
    for (NifIpAddress nifIp: nif.getIpAddresses()) {
      if (nifIp instanceof NifIpV6Address) {
        errSrcAddr = ((NifIpV6Address)nifIp).getIpAddr();
      }
    }
    if (errSrcAddr == null) {
      throw new IllegalArgumentException(nif.getName() + " doesn't have IPv6 addresses.");
    }

    IcmpV6CommonPacket.Builder icmpV6Common = new IcmpV6CommonPacket.Builder();
    icmpV6Common.type(type)
                .code(code)
                .srcAddr(errSrcAddr)
                .dstAddr(errDstAddr)
                .payloadBuilder(icmpV6Inet)
                .correctChecksumAtBuild(true);

    try {
      node.sendL4Packet(
        icmpV6Common.build(),
        errSrcAddr,
        errDstAddr,
        nif
      );
    } catch (SendPacketException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
  }

  private static class NdpRequestSender implements RequestSender {

    public void sendRequest(
      InetAddress targetIpAddr,
      Node node,
      NetworkInterface nif
    ) {
      InetAddress srcIpAddr = null;
      for (NifIpAddress addr: nif.getIpAddresses()) {
        if (addr.getIpAddr() instanceof Inet6Address) {
          srcIpAddr = addr.getIpAddr();
        }
      }
      if (srcIpAddr == null) {
        throw new IllegalArgumentException("No IPv6 address is found in " + nif);
      }

      List<IpV6NeighborDiscoveryOption> opts = new ArrayList<IpV6NeighborDiscoveryOption>();
      IpV6NeighborDiscoverySourceLinkLayerAddressOption.Builder optBuilder
        = new IpV6NeighborDiscoverySourceLinkLayerAddressOption.Builder();
      optBuilder.linkLayerAddress(nif.getMacAddress().getAddress())
        .correctLengthAtBuild(true);
      opts.add(optBuilder.build());

      IcmpV6NeighborSolicitationPacket.Builder nsBuilder
        = new IcmpV6NeighborSolicitationPacket.Builder()
            .targetAddress((Inet6Address)targetIpAddr)
            .options(opts);

      Inet6Address nsDstAddr
        = IpV6Helper.generateSolicitedNodeMulticastAddress((Inet6Address)targetIpAddr);
      IcmpV6CommonPacket.Builder commonBuilder = new IcmpV6CommonPacket.Builder();
      commonBuilder
        .type(IcmpV6Type.NEIGHBOR_SOLICITATION)
        .code(IcmpV6Code.NO_CODE)
        .srcAddr((Inet6Address)srcIpAddr)
        .dstAddr(nsDstAddr)
        .correctChecksumAtBuild(true)
        .payloadBuilder(nsBuilder);

      try {
        node.sendL4Packet(
          commonBuilder.build(),
          srcIpAddr,
          nsDstAddr,
          nif,
          255
        );
      } catch (SendPacketException e) {
        // TODO handle the exception.
        e.printStackTrace();
      }
    }

  }

  public static class NdpCache extends NeighborCache {

    private NdpCache(long cacheLife) {
      super(cacheLife);
    }

  }

}
