/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network.protocol;

import org.pcap4j.packet.Dot1qVlanTagPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.SimpleBuilder;
import org.pcap4j.packet.namednumber.EtherType;

public final class Dot1qVlanTagHelper {

  private Dot1qVlanTagHelper() { throw new AssertionError(); }

  public static Packet tag(Packet packet, int vid) {
    EthernetPacket ep = packet.get(EthernetPacket.class);
    if (ep == null) {
      return packet;
    }

    Dot1qVlanTagPacket.Builder vb = new Dot1qVlanTagPacket.Builder();
    vb.vid((short)vid)
      .type(ep.getHeader().getType())
      .payloadBuilder(new SimpleBuilder(ep.getPayload()));

    Packet.Builder pb = packet.getBuilder();
    pb.get(EthernetPacket.Builder.class)
      .type(EtherType.DOT1Q_VLAN_TAGGED_FRAMES)
      .payloadBuilder(vb);

    return pb.build();
  }

  public static Packet untag(Packet packet) {
    Dot1qVlanTagPacket vp = packet.get(Dot1qVlanTagPacket.class);
    if (vp == null) {
      return packet;
    }

    Packet.Builder pb = packet.getBuilder();
    pb.get(EthernetPacket.Builder.class)
      .type(vp.getHeader().getType())
      .payloadBuilder(new SimpleBuilder(vp.getPayload()));
    return pb.build();
  }

  public static boolean isTagged(Packet packet, int vid) {
    Dot1qVlanTagPacket vp = packet.get(Dot1qVlanTagPacket.class);
    return vp == null ? false : vp.getHeader().getVidAsInt() == vid;
  }

}
