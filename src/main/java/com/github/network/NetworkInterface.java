/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.network;

import java.net.InetAddress;
import org.pcap4j.core.PacketListener;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.MacAddress;

public interface NetworkInterface {

  public String getName();

  public MacAddress getMacAddress();

  public InetAddress getIpAddress();

  public InetAddress getSubnetMask();

  public void addUser(PacketListener user);

  public void start();

  public void stop();

  public void shutdown();

  public boolean isRunning();

  public void sendPacket(Packet packet);

}
