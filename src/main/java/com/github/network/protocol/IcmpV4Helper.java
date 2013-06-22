/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.network.protocol;

import org.pcap4j.packet.IcmpV4CommonPacket;
import org.pcap4j.packet.IcmpV4DestinationUnreachablePacket;
import org.pcap4j.packet.IcmpV4EchoPacket;
import org.pcap4j.packet.IcmpV4EchoReplyPacket;
import org.pcap4j.packet.IcmpV4TimeExceededPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.SimpleBuilder;
import org.pcap4j.packet.namednumber.IcmpV4Code;
import org.pcap4j.packet.namednumber.IcmpV4Type;
import com.github.network.NetworkInterface;
import com.github.network.Node;
import com.github.network.SendPacketException;

public final class IcmpV4Helper {

  private IcmpV4Helper() { throw new AssertionError(); }

  public static void reply(Packet packet, Node node, NetworkInterface nif) {
    IcmpV4EchoPacket echo = packet.get(IcmpV4EchoPacket.class);
    Packet.Builder outer = packet.getBuilder().getOuterOf(IcmpV4EchoPacket.Builder.class);
    IpV4Packet ipv4 = packet.get(IpV4Packet.class);

    if (
         echo == null
      || ipv4 == null
      || outer == null
      || !(outer instanceof IcmpV4CommonPacket.Builder)
    ) {
      throw new IllegalArgumentException(packet.toString());
    }

    IcmpV4EchoReplyPacket.Builder repb = new IcmpV4EchoReplyPacket.Builder();
    repb.identifier(echo.getHeader().getIdentifier())
        .sequenceNumber(echo.getHeader().getSequenceNumber())
        .payloadBuilder(new SimpleBuilder(echo.getPayload()));

    ((IcmpV4CommonPacket.Builder)outer).type(IcmpV4Type.ECHO_REPLY)
                                       .payloadBuilder(repb)
                                       .correctChecksumAtBuild(true);

    try {
      node.sendL4Packet(
        outer.build(),
        ipv4.getHeader().getDstAddr(),
        ipv4.getHeader().getSrcAddr(),
        nif
      );
    } catch (SendPacketException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
  }

  public static void sendErrorMessage(
    IcmpV4Type type,
    IcmpV4Code code,
    Packet packet,
    Node node,
    NetworkInterface nif
  ) {
    IpV4Packet ipv4Packet = packet.get(IpV4Packet.class);
    if (ipv4Packet == null) {
      throw new IllegalArgumentException(packet.toString());
    }

    Packet.Builder icmpV4Inet;
    if (type.equals(IcmpV4Type.DESTINATION_UNREACHABLE)) {
      icmpV4Inet
        = new IcmpV4DestinationUnreachablePacket.Builder()
            .payload(
               org.pcap4j.util.IcmpV4Helper
                 .makePacketForInvokingPacketField(ipv4Packet)
             );
    }
    else if (type.equals(IcmpV4Type.TIME_EXCEEDED)) {
      icmpV4Inet
        = new IcmpV4TimeExceededPacket.Builder()
            .payload(
               org.pcap4j.util.IcmpV4Helper
                 .makePacketForInvokingPacketField(ipv4Packet)
             );
    }
    else {
      throw new IllegalArgumentException(packet.toString());
    }

    IcmpV4CommonPacket.Builder icmpV4Common = new IcmpV4CommonPacket.Builder();
    icmpV4Common.type(type)
                .code(code)
                .payloadBuilder(icmpV4Inet)
                .correctChecksumAtBuild(true);

    try {
      node.sendL4Packet(
        icmpV4Common.build(),
        nif.getIpAddress(),
        ipv4Packet.getHeader().getSrcAddr(),
        nif
      );
    } catch (SendPacketException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
  }

}
