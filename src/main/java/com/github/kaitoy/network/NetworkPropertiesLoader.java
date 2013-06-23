/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.network;

import java.net.InetAddress;
import org.pcap4j.util.PropertiesLoader;

public final class NetworkPropertiesLoader {

  private static final String KEY_PREFIX
    = NetworkPropertiesLoader.class.getPackage().getName();
  public static final String AGENT_PROPERTIES_PATH_KEY
    = KEY_PREFIX + ".properties";

  private static final PropertiesLoader loader
    = new PropertiesLoader(
        System.getProperty(
          AGENT_PROPERTIES_PATH_KEY,
          KEY_PREFIX.replace('.', '/') + "/network.properties"
        ),
        true,
        true
      );

  private NetworkPropertiesLoader() { throw new AssertionError(); }

  public static int getPacketQueueSize() {
    return loader.getInteger(
             KEY_PREFIX + ".packetQueueSize",
             300
           );
  }

  public static int getArpCacheLife() {
    return loader.getInteger(
             KEY_PREFIX + ".arpCacheLife",
             30000
           );
  }

  public static int getArpResolveTimeout() {
    return loader.getInteger(
             KEY_PREFIX + ".arpResolveTimeout",
             500
           );
  }

  public static int getMtu() {
    return loader.getInteger(
             KEY_PREFIX + ".mtu",
             1400
           );
  }

  public static InetAddress getRealNetworkInterfaceIpAddress() {
    return loader.getInetAddress(
             KEY_PREFIX + ".RealNetworkInterface.getNifByipAddress",
             null
           );
  }

}
