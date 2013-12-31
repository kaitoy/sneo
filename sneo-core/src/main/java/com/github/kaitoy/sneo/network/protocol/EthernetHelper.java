/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network.protocol;

import org.pcap4j.packet.AbstractPacket.AbstractBuilder;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.Dot1qVlanTagPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV6Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.MacAddress;

public final class EthernetHelper {

  private EthernetHelper() { throw new AssertionError(); }

  public static boolean matchesDestination(Packet packet, MacAddress addr) {
    EthernetPacket etherPacket = packet.get(EthernetPacket.class);
    if (etherPacket == null) {
      throw new IllegalArgumentException(packet.toString());
    }

    MacAddress dst
      = etherPacket.getHeader().getDstAddr();
    return    dst.equals(addr)
           || dst.equals(MacAddress.ETHER_BROADCAST_ADDRESS);
  }

  public static EthernetPacket pack(
    final Packet payload, MacAddress src, MacAddress dst
  ) {
    EtherType type;
    if (payload instanceof IpV4Packet) {
      type = EtherType.IPV4;
    }
    else if (payload instanceof IpV6Packet) {
      type = EtherType.IPV6;
    }
    else if (payload instanceof ArpPacket) {
      type = EtherType.ARP;
    }
    else if (payload instanceof Dot1qVlanTagPacket) {
      type = EtherType.DOT1Q_VLAN_TAGGED_FRAMES;
    }
    else {
      throw new AssertionError(payload.getClass().getName());
    }

    EthernetPacket.Builder etherBuilder = new EthernetPacket.Builder();
    return etherBuilder.dstAddr(dst)
                       .srcAddr(src)
                       .type(type)
                       .payloadBuilder(
                          new AbstractBuilder() {
                            @Override
                            public Packet build() {
                              return payload;
                            }
                          }
                        )
                       .paddingAtBuild(true)
                       .build();
  }

}
