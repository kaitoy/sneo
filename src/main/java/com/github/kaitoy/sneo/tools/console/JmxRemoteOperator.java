/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.tools.console;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import mx4j.log.Log4JLogger;
import org.snmp4j.util.ArgumentParser;
import com.github.kaitoy.sneo.log.Log4jPropertiesLoader;

public class JmxRemoteOperator {

  private final JMXConnector connector;
  private final MBeanServerConnection connection;

  public JmxRemoteOperator(JMXServiceURL url) throws IOException {
    try {
      connector = JMXConnectorFactory.connect(url);
      connection = connector.getMBeanServerConnection();
    } catch (IOException e) {
      close();
      throw e;
    }
  }

  public void close() throws IOException {
    if (connector != null) {
      connector.close();
    }
  }

  public void listNodes() throws IOException {
    try {
      @SuppressWarnings("unchecked")
      Set<ObjectName> nodeNames = connection.queryNames(new ObjectName("Nodes:*"), null);
      for (ObjectName nodeName: nodeNames) {
        System.out.println(ObjectName.unquote(nodeName.getKeyProperty("address")));
      }
    } catch (MalformedObjectNameException e) {
      throw new AssertionError("Never get here.");
    }
  }

  public ObjectName getNodeName(String address) throws IOException {
    try {
      Set<?> nodeNames
        = connection.queryNames(
            new ObjectName("Nodes:address=" + ObjectName.quote(address) + ",*"),
            null
          );
      if (nodeNames.size() != 1) {
        System.out.println(nodeNames.size());
        throw new IllegalArgumentException("Invalid address: " + address);
      }
      return (ObjectName)nodeNames.iterator().next();
    } catch (MalformedObjectNameException e) {
      throw new IllegalArgumentException("Invalid address: " + address, e);
    }
  }

  public MBeanInfo getMBeanInfo(ObjectName name) throws IOException {
    try {
      return connection.getMBeanInfo(name);
    } catch (InstanceNotFoundException e) {
      throw new IllegalArgumentException(e);
    } catch (IntrospectionException e) {
      throw new IllegalStateException(e);
    } catch (ReflectionException e) {
      throw new IllegalStateException(e);
    }
  }

  public void printObjectInfo(ObjectName objName) throws IOException {
    MBeanInfo info = getMBeanInfo(objName);

    System.out.println("Attributes:");
    for (MBeanAttributeInfo attr: info.getAttributes()) {
      StringBuilder sb = new StringBuilder(50);
      sb.append("  ")
        .append(attr.isReadable() ? "r" : " ")
        .append(attr.isWritable() ? "w " : "  ")
        .append(attr.getType())
        .append(" ")
        .append(attr.getName());

      Object val;
      try {
        val = connection.getAttribute(objName, attr.getName());
      } catch (AttributeNotFoundException e) {
        e.printStackTrace();
        val = "?";
      } catch (InstanceNotFoundException e) {
        e.printStackTrace();
        val = "?";
      } catch (MBeanException e) {
        e.printStackTrace();
        val = "?";
      } catch (ReflectionException e) {
        e.printStackTrace();
        val = "?";
      }
      sb.append(": ")
        .append(val);

      System.out.println(sb.toString());
    }

    System.out.println();
    System.out.println("Operations:");
    for (MBeanOperationInfo oper: info.getOperations()) {
      StringBuilder sb = new StringBuilder(50);
      sb.append("  ")
        .append(oper.getReturnType())
        .append(" ")
        .append(oper.getName())
        .append("(");

      MBeanParameterInfo[] params = oper.getSignature();
      for (MBeanParameterInfo param: params) {
        sb.append(param.getType())
          .append(" ")
          .append(param.getName())
          .append(", ");
      }
      if (params.length != 0) {
        sb.delete(sb.length() - 2, sb.length());
      }

      sb.append(")");
      System.out.println(sb.toString());
    }
  }

  public Object getAttr(ObjectName objName, String attrName) throws IOException {
    try {
      return connection.getAttribute(objName, attrName);
    } catch (AttributeNotFoundException e) {
      e.printStackTrace();
    } catch (InstanceNotFoundException e) {
      e.printStackTrace();
    } catch (MBeanException e) {
      e.printStackTrace();
    } catch (ReflectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  public Attribute setAttr(
    ObjectName objName, String attrName, Object value
  ) throws IOException {
    Attribute attr = new Attribute(attrName, value);
    try {
      connection.setAttribute(objName, attr);
      return attr;
    } catch (InstanceNotFoundException e) {
      e.printStackTrace();
    } catch (AttributeNotFoundException e) {
      e.printStackTrace();
    } catch (InvalidAttributeValueException e) {
      e.printStackTrace();
    } catch (MBeanException e) {
      e.printStackTrace();
    } catch (ReflectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  public void invoke(
    ObjectName objName, String operName, List<String> operArgs
  ) throws IOException {
    List<Object> paramList = new ArrayList<Object>();
    List<String> signature = new ArrayList<String>();

    for (int i = 0; i < operArgs.size(); i += 2) {
      String type = operArgs.get(i);
      String value = operArgs.get(i + 1);
      if (type.equals("int")) {
        signature.add(type);
        paramList.add(Integer.valueOf(value));
      }
      else if (type.equalsIgnoreCase("String") || type.equals(" java.lang.String")) {
        signature.add(String.class.getName());
        paramList.add(value);
      }
    }

    try {
      Object result
        = connection.invoke(
            objName, operName,
            paramList.toArray(), signature.toArray(new String[signature.size()])
          );
      System.out.println(result);
    } catch (InstanceNotFoundException e) {
      e.printStackTrace();
    } catch (MBeanException e) {
      e.printStackTrace();
    } catch (ReflectionException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws IOException {
    mx4j.log.Log.redirectTo(new Log4JLogger());
    Log4jPropertiesLoader.getInstance().loadPropertyOf(JmxRemoteOperator.class);

    Map<?, ?> paramMap = parseArgs(args);

    int rmiPort = ((Integer)ArgumentParser.getValue(paramMap, "rmiPort", 0)).intValue();
    JMXServiceURL url
      = new JMXServiceURL(
          "service:jmx:rmi:///jndi/rmi://localhost:" + rmiPort + "/JMXConnectorServer"
        );
    JmxRemoteOperator jro = new JmxRemoteOperator(url);

    try {
      String command = (String)ArgumentParser.getValue(paramMap, "command", 0);
      if (command.equals("listNodes")) {
        jro.listNodes();
        return;
      }

      String node = (String)ArgumentParser.getValue(paramMap, "node", 0);
      ObjectName nodeName = jro.getNodeName(node);

      if (command.equals("info")) {
        jro.printObjectInfo(nodeName);
        return;
      }
      else if (command.equals("get")) {
        String attrName = (String)ArgumentParser.getValue(paramMap, "command_args", 0);
        System.out.println(jro.getAttr(nodeName, attrName));
        return;
      }
      else if (command.equals("set")) {
        String attrName = (String)ArgumentParser.getValue(paramMap, "command_args", 0);
        String value = (String)ArgumentParser.getValue(paramMap, "command_args", 1);
        Attribute result = jro.setAttr(nodeName, attrName, value);
        if (result != null) {
          System.out.println("Success!");
        }
        return;
      }
      else { // invoke
        @SuppressWarnings("unchecked")
        List<String> commandArgs = (List<String>)paramMap.get("command_args");
        String operName = commandArgs.remove(0);
        jro.invoke(nodeName, operName, commandArgs);
      }
    } finally {
      jro.close();
    }
  }

  private static Map<?, ?> parseArgs(String[] args) {
    Map<?, ?> params = null;
    List<String> optList = new ArrayList<String>();
    List<String> paramList = new ArrayList<String>();

    try {
      optList.add("-rmiPort[i{=" + Registry.REGISTRY_PORT + "}] ");

      paramList.add("-command[s<listNodes|info|get|set|invoke>] ");
      paramList.add("+node[s] ");
      paramList.add("+command_args[s] ..");

      for (String arg: args) {
        if (
             arg.equals("-h")
          || arg.equals("-help")
          || arg.equals("--help")
          || arg.equals("-?")
        ) {
          prHelp(optList, paramList);
          System.exit(0);
        }
      }

      StringBuilder optsBuilder = new StringBuilder();
      for (String opt: optList) {
        optsBuilder.append(opt);
      }
      StringBuilder paramsBuilder = new StringBuilder();
      for (String param: paramList) {
        paramsBuilder.append(param);
      }

      ArgumentParser parser
        = new ArgumentParser(optsBuilder.toString(), paramsBuilder.toString());
      params = parser.parse(args);
    }
    catch (ParseException e) {
      prHelp(optList, paramList);
      System.exit(1);
    }

    return params;
  }

  private static void prHelp(List<String> optList, List<String> paramList) {
    System.out.println("Usage: " + JmxRemoteOperator.class.getName() + " <Options> <Params>");
    System.out.println("Options: ");
    for (String opt: optList) {
      System.out.println("  " + opt);
    }
    System.out.println("Params: ");
    for (String param: paramList) {
      System.out.println("  " + param);
    }
  }

}
