/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.network.protocol;

import java.net.InetAddress;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.SimpleBuilder;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.namednumber.UdpPort;

public final class UdpHelper {

  public static UdpPacket pack(
    Packet payload,
    UdpPort srcPort,
    UdpPort dstPort,
    InetAddress srcAddr,
    InetAddress dstAddr
  ) {
    UdpPacket.Builder builder = new UdpPacket.Builder();
    return builder.srcPort(srcPort)
                  .dstPort(dstPort)
                  .payloadBuilder(new SimpleBuilder(payload))
                  .srcAddr(srcAddr)
                  .dstAddr(dstAddr)
                  .correctChecksumAtBuild(true)
                  .correctLengthAtBuild(true)
                  .build();
  }

}
