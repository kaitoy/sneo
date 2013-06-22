/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.transport;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UdpPacket;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.UdpTransportMapping;
import com.github.network.Node;
import com.github.network.SendPacketException;

public class SneoUdpTransportMapping extends UdpTransportMapping {

  private static final int MAX_INBOUND_MESSAGE_SIZE = 65536; //TODO RealNetworkInterface
  private volatile Node node;

  public SneoUdpTransportMapping(UdpAddress udpAddress) {
    super(udpAddress);
    maxInboundMessageSize = MAX_INBOUND_MESSAGE_SIZE;
  }

  public void setNode(Node node) {
    this.node = node;
  }

  public boolean isListening() { return true; }

  @Deprecated
  @Override
  public void listen() throws IOException {}

  @Deprecated
  @Override
  public void close() throws IOException {}

  @Override
  public void sendMessage(Address address, byte[] message) throws IOException {
    if (!(address instanceof UdpAddress)) {
      throw new IllegalArgumentException();
    }

    try {
      node.sendSnmpMessage(
        ((UdpAddress)address).getInetAddress(),
        ((UdpAddress)address).getPort(),
        message
      );
    } catch (SendPacketException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
  }

  public void processMessage(Packet packet) {
    UdpPacket udpPacket = packet.get(UdpPacket.class);
    byte[] snmpMessage = udpPacket.getPayload().getRawData();
    InetAddress srcAddr
      = packet.get(IpV4Packet.class).getHeader().getSrcAddr();
    int srcPort = udpPacket.getHeader().getSrcPort().value() & 0xFFFF;

    ByteBuffer bis;
    if (isAsyncMsgProcessingSupported()) {
      byte[] rawData = new byte[snmpMessage.length];
      System.arraycopy(snmpMessage, 0, rawData, 0, rawData.length);
      bis = ByteBuffer.wrap(rawData);
    }
    else {
      bis = ByteBuffer.wrap(snmpMessage);
    }

    fireProcessMessage(
      new UdpAddress(srcAddr, srcPort),
      bis
    );
  }

}
