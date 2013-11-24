/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network;

import java.net.InetAddress;


public interface NifIpAddress {

  public InetAddress getIpAddr();

  public int getPrefixLength();

}
