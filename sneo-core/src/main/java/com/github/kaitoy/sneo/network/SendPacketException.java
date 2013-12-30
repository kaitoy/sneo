/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network;

import org.pcap4j.packet.namednumber.IcmpV4Code;
import org.pcap4j.packet.namednumber.IcmpV4Type;
import org.pcap4j.packet.namednumber.IcmpV6Code;
import org.pcap4j.packet.namednumber.IcmpV6Type;

public class SendPacketException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = -1908496627559641793L;

  private final IcmpV4Type v4ErrorType;
  private final IcmpV4Code v4ErrorCode;
  private final IcmpV6Type v6ErrorType;
  private final IcmpV6Code v6ErrorCode;

  public SendPacketException() {
    v4ErrorType = null;
    v4ErrorCode = null;
    v6ErrorType = null;
    v6ErrorCode = null;
  }

  public SendPacketException(IcmpV4Type v4ErrorType, IcmpV4Code v4ErrorCode) {
    this.v4ErrorType = v4ErrorType;
    this.v4ErrorCode = v4ErrorCode;
    v6ErrorType = null;
    v6ErrorCode = null;
  }

  public SendPacketException(IcmpV6Type v6ErrorType, IcmpV6Code v6ErrorCode) {
    this.v6ErrorType = v6ErrorType;
    this.v6ErrorCode = v6ErrorCode;
    v4ErrorType = null;
    v4ErrorCode = null;
  }

  public IcmpV4Type getV4ErrorType() { return v4ErrorType; }

  public IcmpV4Code getV4ErrorCode() { return v4ErrorCode; }

  public IcmpV6Type getV6ErrorType() { return v6ErrorType; }

  public IcmpV6Code getV6ErrorCode() { return v6ErrorCode; }

}
