/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.transport;

import org.snmp4j.SNMP4JSettings;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.transport.TransportMappings;

public class TransportsPropertiesManager {

  private static final LogAdapter logger
    = LogFactory.getLogger(TransportsPropertiesManager.class);

  private static final String PROPERTIES_FILE_PATH
    = "/" + TransportsPropertiesManager.class
              .getPackage().getName().replace('.', '/')
        + "/transports.properties";
  private static final TransportsPropertiesManager INSTANCE
    = new TransportsPropertiesManager();

  private boolean extendingTransportMappings = false;

  private TransportsPropertiesManager() {}

  public static TransportsPropertiesManager getInstance() {
    return INSTANCE;
  }

  public synchronized void extendTransportMappings() {
    logger.info("Extend TransportMappings.");

    SNMP4JSettings.setExtensibilityEnabled(true);
    System.setProperty(
      TransportMappings.TRANSPORT_MAPPINGS,
      PROPERTIES_FILE_PATH
    );
    extendingTransportMappings = true;
  }

  public synchronized boolean isExtendingTransportMappings() {
    return extendingTransportMappings;
  }

}
