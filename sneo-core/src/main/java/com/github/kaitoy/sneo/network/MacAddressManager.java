/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network;

import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.pcap4j.packet.namednumber.Oui;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.util.MacAddress;

public class MacAddressManager {

  public static final Oui VIRTUAL_MACADDRESS_OUI
    = Oui.getInstance(new byte[] { (byte)0xFE, (byte)0, (byte)0 });

  private static final MacAddressManager INSTANCE
    = new MacAddressManager();
  private static final int MAX_SERIAL_NUMBER
    = ByteArrays.getInt(
        new byte[] { (byte)0, (byte)0xFF, (byte)0xFF, (byte)0xFF },
        0
      );


  private final Map<InetAddress, MacAddress> registry
    = new ConcurrentHashMap<InetAddress, MacAddress>();
  private final Object counterLock = new Object();

  private int counter = 0;

  private MacAddressManager() {}

  public static MacAddressManager getInstance() { return INSTANCE; }

  public MacAddress generateVirtualMacAddress() {
    int serialNumber;
    synchronized (counterLock) {
      if (counter > MAX_SERIAL_NUMBER) {
        throw new AssertionError("too many MAC addresses.");
      }
      serialNumber = counter;
      counter++;
    }

    // TODO IPv6
    byte[] rawAddr = new byte[6];
    System.arraycopy(
      VIRTUAL_MACADDRESS_OUI.valueAsByteArray(), 0, rawAddr, 0, 3
    );
    System.arraycopy(
      ByteArrays.toByteArray(serialNumber), 1, rawAddr, 3, 3
    );

    MacAddress macAddr = MacAddress.getByAddress(rawAddr);
    return macAddr;
  }

  public boolean isVirtualMacAddress(MacAddress macAddress) {
    return macAddress.getOui().equals(VIRTUAL_MACADDRESS_OUI);
  }

  public MacAddress resolveVirtualAddress(InetAddress ipAddr) {
    return registry.get(ipAddr);
  }

  public void registerVirtualMacAddress(InetAddress ipAddr, MacAddress macAddr) {
    registry.put(ipAddr, macAddr);
  }

}
