/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.jmx;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.management.ManagementFactory;
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
import mx4j.log.Log4JLogger;
import mx4j.tools.adaptor.http.HttpAdaptor;
import mx4j.tools.adaptor.http.XSLTProcessor;
import mx4j.tools.config.ConfigurationLoader;
import org.snmp4j.SNMP4JSettings;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.util.ArgumentParser;
import com.github.kaitoy.agent.AgentPropertiesLoader;
import com.github.kaitoy.log.Log4jPropertiesLoader;
import com.github.kaitoy.log.LoggerManager;
import com.github.kaitoy.smi.SmiSyntaxesPropertiesManager;
import com.github.kaitoy.transport.TransportsPropertiesManager;
import com.github.kaitoy.util.ColonSeparatedOidTypeValueVariableTextFormat;
import com.github.kaitoy.util.ConsoleBlocker;


public class HttpJmxAgent implements JmxAgent {

  private static final LogAdapter logger
    = LogFactory.getLogger(HttpJmxAgent.class);

  private static final String LOADER_NAME = "Loader:name=ConfigurationLoader";
  private static final String LOG_CONTROLLER_NAME = "Tools:name=LogController";
//  private static final String AGENT_FACTORY_NAME = "Tools:name=AgentFactory";
  private static final String ADAPTOR_NAME = "Adaptor:name=HttpAdaptor";
  private static final String PROCESSOR_NAME = "Processor:name=XSLTProcessor";
//  private static final String XSL_PATH
//    = HttpJmxAgent.class.getPackage().getName().replace('.', '/') + "/xsl";
  private static final String XSL_PATH
    = XSLTProcessor.class.getPackage().getName().replace('.', '/') + "/xsl";
  private static final String JMX_CLIENT = "0.0.0.0"; // no restriction

  //private MBeanServer server = MBeanServerFactory.createMBeanServer();
  private MBeanServer server = ManagementFactory.getPlatformMBeanServer();
  private String configFilePath = null;

  public HttpJmxAgent(int jmxPort) {
    if (jmxPort < 0 || jmxPort > 65535) {
      throw new IllegalArgumentException();
    }

    try {
      DynamicMBean logController
        = DynamicMBeanFactory.newDynamicMBean(LoggerManager.getInstance());
      ObjectName logControllerName = new ObjectName(LOG_CONTROLLER_NAME);
      server.registerMBean(logController, logControllerName);

//      DynamicMBean agentFactoty
//        = DynamicMBeanFactory.getInstance()
//            .newDynamicMBean(AgentFactory.getInstance());
//      ObjectName agentFactoryObjectName = new ObjectName(AGENT_FACTORY_NAME);
//      server.registerMBean(agentFactoty, agentFactoryObjectName);
//
//      ((AgentFactoryDynamicMBean)agentFactoty).setServer(server);

      HttpAdaptor adaptor = new HttpAdaptor(jmxPort, JMX_CLIENT);
      ObjectName adaptorObjectName = new ObjectName(ADAPTOR_NAME);
      server.registerMBean(adaptor, adaptorObjectName);

      XSLTProcessor xsltProcessor = new XSLTProcessor();
      ObjectName processorObjectName = new ObjectName(PROCESSOR_NAME);
      server.registerMBean(xsltProcessor, processorObjectName);

      server.setAttribute(
        adaptorObjectName,
        new Attribute("ProcessorName", processorObjectName)
      );

      server.setAttribute(
        processorObjectName,
        new Attribute("PathInJar", XSL_PATH)
      );
    } catch (InstanceAlreadyExistsException e) {
      throw new IllegalStateException(e);
    } catch (MBeanRegistrationException e) {
      throw new IllegalStateException(e);
    } catch (NotCompliantMBeanException e) {
      throw new IllegalStateException(e);
    } catch (MalformedObjectNameException e) {
      throw new IllegalStateException(e);
    } catch (NullPointerException e) {
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
    }
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
      ObjectName loaderObjectName = new ObjectName(LOADER_NAME);
      server.registerMBean(loader, loaderObjectName);
    } catch (MalformedObjectNameException e) {
      throw new IllegalStateException(e);
    } catch (NullPointerException e) {
      throw new IllegalStateException(e);
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
          ((Integer)ArgumentParser.getValue(params, "p", 0)).intValue()
        );
    agent.setConfigFilePath((String)ArgumentParser.getValue(params, "f", 0));
    agent.start();

    ConsoleBlocker.block();

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
            + "-p[i{=8080}] "
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
          ObjectName loaderObjectName = new ObjectName(LOADER_NAME);
          server.invoke(
            loaderObjectName,
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

      ObjectName adaptorObjectName = new ObjectName(ADAPTOR_NAME);
      server.invoke(adaptorObjectName, "start", null, null);

//      addShutdownHook();
    } catch (MalformedObjectNameException e) {
      throw new IllegalStateException(e);
    } catch (NullPointerException e) {
      throw new IllegalStateException(e);
    } catch (InstanceNotFoundException e) {
      throw new IllegalStateException(e);
    } catch (ReflectionException e) {
      throw new IllegalStateException(e);
    } catch (MBeanException e) {
      throw new IllegalStateException(e);
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
      ObjectName adaptorObjectName = new ObjectName(ADAPTOR_NAME);
      server.invoke(adaptorObjectName, "stop", null, null);

      if (configFilePath != null) {
        ObjectName loaderObjectName = new ObjectName(LOADER_NAME);
        server.invoke(loaderObjectName, "shutdown", null, null);
      }
    } catch (MalformedObjectNameException e) {
      throw new IllegalStateException(e);
    } catch (NullPointerException e) {
      throw new IllegalStateException(e);
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
