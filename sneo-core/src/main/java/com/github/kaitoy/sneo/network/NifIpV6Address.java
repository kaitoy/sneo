/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network;

import java.net.Inet6Address;


public class NifIpV6Address implements NifIpAddress {

  private final Inet6Address ipAddr;
  private final int prefixLength;

  public NifIpV6Address(Inet6Address ipAddr, int prefixLength) {
    this.ipAddr = ipAddr;
    this.prefixLength = prefixLength;
  }

  public Inet6Address getIpAddr() {
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
