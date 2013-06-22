/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.tools.console;


import org.snmp4j.SNMP4JSettings;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.log.LogFactory;
import org.snmp4j.tools.console.SnmpRequest;
import com.github.log.Log4jPropertiesLoader;
import com.github.smi.SmiSyntaxesPropertiesManager;

public class SneoSnmpRequest {

  public static void main(String[] args) {
    LogFactory.setLogFactory(new Log4jLogFactory());
    Log4jPropertiesLoader.getInstance()
      .loadPropertyOf(SneoSnmpRequest.class);

    SmiSyntaxesPropertiesManager.getInstance().useExtendedSmi();

    SNMP4JSettings.setVariableTextFormat(
      SnmpRequestPropertiesLoader.getInstance().getVariableTextFormat()
    );

    SnmpRequest.main(args);
  }

}
