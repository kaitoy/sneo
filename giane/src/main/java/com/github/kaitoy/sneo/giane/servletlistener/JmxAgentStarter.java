/*_##########################################################################
  _##
  _##  Copyright (C) 2013 Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.giane.servletlistener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.github.kaitoy.sneo.jmx.HttpJmxAgent;
import com.github.kaitoy.sneo.util.Constants;

public class JmxAgentStarter implements ServletContextListener {

  private static HttpJmxAgent jmxAgent;
  private static Object lock = new Object();

  public static HttpJmxAgent getJmxAgent() {
    return jmxAgent;
  }

  public void contextInitialized(ServletContextEvent servletContextEvent) {
    synchronized (lock) {
      if (jmxAgent == null) {
        int httpPort = getPortFromSystemProperty(Constants.JMX_HTTP_PORT_KEY, 8090);
        int rmiPort = getPortFromSystemProperty(Constants.JMX_RMI_PORT_KEY, 10099);
        jmxAgent = new HttpJmxAgent(httpPort, rmiPort);
        jmxAgent.start();
      }
    }
  }

  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    synchronized (lock) {
      if (jmxAgent != null) {
        jmxAgent.stop();
        jmxAgent = null;
      }
    }
  }

  private int getPortFromSystemProperty(String key, int dflt) {
    int port;
    try {
      port = Integer.parseInt(System.getProperty(key));
      if (port < 1 || port > 65535) {
        port = dflt;
      }
    } catch (Exception e) {
      port = dflt;
    }
    return port;
  }

}
