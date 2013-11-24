/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network;

import java.util.List;
import org.pcap4j.core.PacketListener;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.MacAddress;

public interface NetworkInterface {

  public String getName();

  public MacAddress getMacAddress();

  public List<NifIpAddress> getIpAddresses();

  public void addIpAddress(NifIpAddress addr);

  public void addUser(PacketListener user);

  public void start();

  public void stop();

  public void shutdown();

  public boolean isRunning();

  public void sendPacket(Packet packet);

}
