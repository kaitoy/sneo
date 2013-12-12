/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.jmx;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.util.Map;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import mx4j.log.Log4JLogger;
import mx4j.tools.adaptor.http.HttpAdaptor;
import mx4j.tools.adaptor.http.XSLTProcessor;
import mx4j.tools.config.ConfigurationLoader;
import org.snmp4j.SNMP4JSettings;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.util.ArgumentParser;
import com.github.kaitoy.sneo.agent.AgentPropertiesLoader;
import com.github.kaitoy.sneo.log.Log4jPropertiesLoader;
import com.github.kaitoy.sneo.log.LoggerManager;
import com.github.kaitoy.sneo.smi.SmiSyntaxesPropertiesManager;
import com.github.kaitoy.sneo.transport.TransportsPropertiesManager;
import com.github.kaitoy.sneo.util.ColonSeparatedOidTypeValueVariableTextFormat;
import com.github.kaitoy.sneo.util.ConsoleBlocker;


public class HttpJmxAgent implements JmxAgent {

  private static final LogAdapter logger
    = LogFactory.getLogger(HttpJmxAgent.class);

  private static final ObjectName LOADER_NAME;
  private static final ObjectName LOG_CONTROLLER_NAME;
//  private static final String AGENT_FACTORY_NAME = "Tools:name=AgentFactory";
  private static final ObjectName ADAPTOR_NAME;
  private static final ObjectName PROCESSOR_NAME;
  private static final ObjectName CONNECTOR_NAME;
//  private static final String XSL_PATH
//    = HttpJmxAgent.class.getPackage().getName().replace('.', '/') + "/xsl";
  private static final String XSL_PATH
    = XSLTProcessor.class.getPackage().getName().replace('.', '/') + "/xsl";
  private static final String JMX_CLIENT = "0.0.0.0"; // no restriction

  //private MBeanServer server = MBeanServerFactory.createMBeanServer();
  private final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
  private String configFilePath = null;
  private final int jmxPort;
  private final int rmiPort;

  static {
    try {
      LOADER_NAME = new ObjectName("Loader:name=ConfigurationLoader");
      LOG_CONTROLLER_NAME = new ObjectName("Tools:name=LogController");
      ADAPTOR_NAME = new ObjectName("Adaptor:name=HttpAdaptor");
      PROCESSOR_NAME = new ObjectName("Processor:name=XSLTProcessor");
      CONNECTOR_NAME = new ObjectName("Connector:protocol=rmi");
    } catch (MalformedObjectNameException e) {
      throw new AssertionError("Never get here.");
    } catch (NullPointerException e) {
      throw new AssertionError("Never get here.");
    }
  }

  public HttpJmxAgent(int jmxPort, int rmiPort) {
    if (jmxPort < 0 || jmxPort > 65535) {
      throw new IllegalArgumentException("Invalid jmxPort: " + jmxPort);
    }
    if (rmiPort < 0 || rmiPort > 65535) {
      throw new IllegalArgumentException("Invalid jmxPort: " + rmiPort);
    }
    if (jmxPort == rmiPort) {
      throw new IllegalArgumentException("jmxPort and rmiPort must be different.");
    }
    this.jmxPort = jmxPort;
    this.rmiPort = rmiPort;

    try {
      DynamicMBean logController
        = DynamicMBeanFactory.newDynamicMBean(LoggerManager.getInstance());
      server.registerMBean(logController, LOG_CONTROLLER_NAME);

//      DynamicMBean agentFactoty
//        = DynamicMBeanFactory.getInstance()
//            .newDynamicMBean(AgentFactory.getInstance());
//      ObjectName agentFactoryObjectName = new ObjectName(AGENT_FACTORY_NAME);
//      server.registerMBean(agentFactoty, agentFactoryObjectName);
//
//      ((AgentFactoryDynamicMBean)agentFactoty).setServer(server);

      HttpAdaptor adaptor = new HttpAdaptor(jmxPort, JMX_CLIENT);
      server.registerMBean(adaptor, ADAPTOR_NAME);

      XSLTProcessor xsltProcessor = new XSLTProcessor();
      server.registerMBean(xsltProcessor, PROCESSOR_NAME);

      server.setAttribute(
        ADAPTOR_NAME,
        new Attribute("ProcessorName", PROCESSOR_NAME)
      );

      server.setAttribute(
        PROCESSOR_NAME,
        new Attribute("PathInJar", XSL_PATH)
      );

      try {
        JMXServiceURL url
          = new JMXServiceURL(
              "service:jmx:rmi:///jndi/rmi://localhost:" + rmiPort + "/JMXConnectorServer"
            );
        JMXConnectorServer jmxConnectorServer
          = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
        server.registerMBean(jmxConnectorServer, CONNECTOR_NAME);
      } catch (MalformedURLException e) {
        throw new AssertionError("Never get here.");
      }
    } catch (InstanceAlreadyExistsException e) {
      throw new IllegalStateException(e);
    } catch (MBeanRegistrationException e) {
      throw new IllegalStateException(e);
    } catch (NotCompliantMBeanException e) {
      throw new IllegalStateException(e);
    } catch (InstanceNotFoundException e) {
      throw new IllegalStateException(e);
    } catch (InvalidAttributeValueException e) {
      throw new IllegalStateException(e);
    } catch (AttributeNotFoundException e) {
      throw new IllegalStateException(e);
    } catch (ReflectionException e) {
      throw new IllegalStateException(e);
    } catch (MBeanException e) {
      throw new IllegalStateException(e);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public int getJmxPort() {
    return jmxPort;
  }

  public int getRmiPort() {
    return rmiPort;
  }

  public void registerPojo(Object mo, String moName) {
    try {
      DynamicMBean moMBean = DynamicMBeanFactory.newDynamicMBean(mo);
      ObjectName moObjectName = new ObjectName(moName);
      server.registerMBean(moMBean, moObjectName);
    } catch (MalformedObjectNameException e) {
      throw new IllegalArgumentException(e);
    } catch (NullPointerException e) {
      throw new IllegalArgumentException(e);
    } catch (MBeanRegistrationException e) {
      throw new IllegalArgumentException(e);
    } catch (NotCompliantMBeanException e) {
      throw new IllegalStateException(e);
    } catch (InstanceAlreadyExistsException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void registerMBean(Object mbean, String moName) {
    try {
      ObjectName moObjectName = new ObjectName(moName);
      server.registerMBean(mbean, moObjectName);
    } catch (MalformedObjectNameException e) {
      throw new IllegalArgumentException(e);
    } catch (NullPointerException e) {
      throw new IllegalArgumentException(e);
    } catch (MBeanRegistrationException e) {
      throw new IllegalArgumentException(e);
    } catch (NotCompliantMBeanException e) {
      throw new IllegalArgumentException(e);
    } catch (InstanceAlreadyExistsException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void unregisterMBean(String moName) {
    try {
      server.unregisterMBean(new ObjectName(moName));
    } catch (MBeanRegistrationException e) {
      throw new IllegalArgumentException(e);
    } catch (InstanceNotFoundException e) {
      throw new IllegalArgumentException(e);
    } catch (MalformedObjectNameException e) {
      throw new IllegalArgumentException(e);
    } catch (NullPointerException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void setConfigFilePath(String configFilePath) {
    if (this.configFilePath != null) {
      throw new IllegalStateException("configFilePath is already set.");
    }

    this.configFilePath = configFilePath;

    try {
      ConfigurationLoader loader = new ConfigurationLoader();
      server.registerMBean(loader, LOADER_NAME);
    } catch (InstanceAlreadyExistsException e) {
      throw new IllegalStateException(e);
    } catch (MBeanRegistrationException e) {
      throw new IllegalStateException(e);
    } catch (NotCompliantMBeanException e) {
      throw new IllegalStateException(e);
    }
  }

  public static void main (String[] args) {
    LogFactory.setLogFactory(new Log4jLogFactory());
    mx4j.log.Log.redirectTo(new Log4JLogger());
    Log4jPropertiesLoader.getInstance().loadPropertyOf(HttpJmxAgent.class);

    if (AgentPropertiesLoader.getInstance().extendSmiSyntaxes()) {
      SmiSyntaxesPropertiesManager.getInstance().useExtendedSmi();
    }
    if (AgentPropertiesLoader.getInstance().extendTransportMappings()) {
      TransportsPropertiesManager.getInstance().extendTransportMappings();
    }

    SNMP4JSettings.setVariableTextFormat(
      ColonSeparatedOidTypeValueVariableTextFormat.getInstance()
    );

    Map<?, ?> params = parseArgs(args);

    HttpJmxAgent agent
      = new HttpJmxAgent(
          ((Integer)ArgumentParser.getValue(params, "jmxPort", 0)).intValue(),
          ((Integer)ArgumentParser.getValue(params, "rmiPort", 0)).intValue()
        );
    agent.setConfigFilePath((String)ArgumentParser.getValue(params, "f", 0));
    agent.start();

    ConsoleBlocker.block("** Hit Enter key to stop simulation **");

    agent.stop();

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e1) {}
  }

  private static Map<?, ?> parseArgs(String[] args) {
    Map<?, ?> params = null;
    try {
      ArgumentParser parser
        = new ArgumentParser(
              "-f[s{=HttpJmxAgent.xml}] "
            + "-jmxPort[i{=8080}] "
            + "-rmiPort[i{=" + Registry.REGISTRY_PORT + "}]"
            ,
            ""
          );

      params = parser.parse(args);
    }
    catch (ParseException e) {
      throw new IllegalArgumentException(e);
    }

    return params;
  }

  public void start() {
    Reader reader = null;

    try {
      if (configFilePath != null) {
        try {
          reader = new BufferedReader(new FileReader(configFilePath));
          server.invoke(
            LOADER_NAME,
            "startup",
            new Object[] {
              reader
            },
            new String[] {
              java.io.Reader.class.getName()
            }
          );
        } catch (FileNotFoundException e) {
          logger.error(e);
        }
      }

      server.invoke(ADAPTOR_NAME, "start", null, null);

      LocateRegistry.createRegistry(rmiPort);
      server.invoke(CONNECTOR_NAME, "start", null, null);

//      addShutdownHook();
    } catch (InstanceNotFoundException e) {
      throw new IllegalStateException(e);
    } catch (ReflectionException e) {
      throw new IllegalStateException(e);
    } catch (MBeanException e) {
      throw new IllegalStateException(e);
    } catch (RemoteException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {}
      }
    }
  }

  public void stop() {
    try {
      server.invoke(CONNECTOR_NAME, "stop", null, null);
      server.invoke(ADAPTOR_NAME, "stop", null, null);
      if (configFilePath != null) {
        server.invoke(LOADER_NAME, "shutdown", null, null);
      }
    } catch (InstanceNotFoundException e) {
      throw new IllegalStateException(e);
    } catch (ReflectionException e) {
      throw new IllegalStateException(e);
    } catch (MBeanException e) {
      throw new IllegalStateException(e);
    }
  }

//  private void addShutdownHook() {
//    Runtime.getRuntime().addShutdownHook(new Thread() {
//      public void run() {
//        try {
//          ObjectName adaptorObjectName = new ObjectName(ADAPTOR_NAME);
//          server.invoke(adaptorObjectName, "stop", null, null);
//
//          if (configFilePath != null) {
//            ObjectName loaderObjectName = new ObjectName(LOADER_NAME);
//            server.invoke(loaderObjectName, "shutdown", null, null);
//          }
//        } catch (MalformedObjectNameException e) {
//          throw new IllegalStateException(e);
//        } catch (NullPointerException e) {
//          throw new IllegalStateException(e);
//        } catch (InstanceNotFoundException e) {
//          throw new IllegalStateException(e);
//        } catch (ReflectionException e) {
//          throw new IllegalStateException(e);
//        } catch (MBeanException e) {
//          throw new IllegalStateException(e);
//        }
//      }
//    });
//  }

}
