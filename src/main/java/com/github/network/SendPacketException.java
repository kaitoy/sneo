/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.network;

import org.pcap4j.packet.namednumber.IcmpV4Code;
import org.pcap4j.packet.namednumber.IcmpV4Type;

public class SendPacketException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = -1908496627559641793L;

  private final IcmpV4Type errorType;
  private final IcmpV4Code errorCode;

  public SendPacketException() {
    errorType = null;
    errorCode = null;
  }

  public SendPacketException(IcmpV4Type errorType, IcmpV4Code errorCode) {
    this.errorType = errorType;
    this.errorCode = errorCode;
  }

  public IcmpV4Type getErrorType() { return errorType; }

  public IcmpV4Code getErrorCode() { return errorCode; }

}
