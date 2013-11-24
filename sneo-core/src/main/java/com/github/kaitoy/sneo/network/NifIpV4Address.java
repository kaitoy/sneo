/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network;

import java.net.Inet4Address;


public class NifIpV4Address implements NifIpAddress {

  private final Inet4Address ipAddr;
  private final int prefixLength;

  public NifIpV4Address(Inet4Address ipAddr, int prefixLength) {
    this.ipAddr = ipAddr;
    this.prefixLength = prefixLength;
  }

  public Inet4Address getIpAddr() {
    return ipAddr;
  }

  public int getPrefixLength() {
    return prefixLength;
  }

  @Override
  public String toString() {
    String ipAddrStr = ipAddr.toString();
    StringBuilder sb = new StringBuilder();
    sb.append(ipAddrStr.substring(ipAddrStr.lastIndexOf("/") + 1))
      .append("/")
      .append(prefixLength);
    return sb.toString();
  }

}
