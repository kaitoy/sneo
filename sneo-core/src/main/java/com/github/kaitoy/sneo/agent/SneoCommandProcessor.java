/*_##########################################################################
  _##
  _##  Copyright (C) 2014  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.agent;

import org.snmp4j.CommandResponderEvent;
import org.snmp4j.PDU;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.smi.OctetString;

public class SneoCommandProcessor extends CommandProcessor {

  private volatile int maxMessageSize;

  public SneoCommandProcessor(OctetString contextEngineID, int maxMessageSize) {
    super(contextEngineID);
    this.maxMessageSize = maxMessageSize;
  }

  public void setMaxMessageSize(int maxMessageSize) {
    this.maxMessageSize = maxMessageSize;
  }

  protected void sendResponse(CommandResponderEvent requestEvent, PDU response) {
    int tmp = requestEvent.getMaxSizeResponsePDU();
    requestEvent.setMaxSizeResponsePDU(maxMessageSize);
    super.sendResponse(requestEvent, response);
    requestEvent.setMaxSizeResponsePDU(tmp);
  }

}
