/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.smi;

import org.snmp4j.SNMP4JSettings;


public class SmiSyntaxesPropertiesManager {

  private static final SmiSyntaxesPropertiesManager INSTANCE
    = new SmiSyntaxesPropertiesManager();
  private static final String PROPERTIES_FILE_PATH
    = "/" + SmiSyntaxesPropertiesManager.class
              .getPackage().getName().replace('.', '/')
        + "/smisyntaxes.properties";

  private SmiSyntaxesPropertiesManager() {}

  public static SmiSyntaxesPropertiesManager getInstance() {
    return INSTANCE;
  }

  public void useExtendedSmi() {
    SNMP4JSettings.setExtensibilityEnabled(true);
    System.setProperty(
        org.snmp4j.smi.AbstractVariable.SMISYNTAXES_PROPERTIES,
        PROPERTIES_FILE_PATH
    );
  }

}
