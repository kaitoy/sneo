/*_##########################################################################
  _##
  _##  Copyright (C) 2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.tools.console;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
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

  public static void main(String[] args) {
    mx4j.log.Log.redirectTo(new Log4JLogger());
    Log4jPropertiesLoader.getInstance().loadPropertyOf(JmxRemoteOperator.class);

    Map<?, ?> paramMap = parseArgs(args);

    int rmiPort = ((Integer)ArgumentParser.getValue(paramMap, "rmiPort", 0)).intValue();
    String host = (String)ArgumentParser.getValue(paramMap, "host", 0);
    StringBuffer urlBuff = new StringBuffer();
    urlBuff.append("service:jmx:rmi:///jndi/rmi://")
      .append(host).append(":").append(rmiPort).append("/JMXConnectorServer");

    JMXServiceURL url = null;
    try {
      url = new JMXServiceURL(urlBuff.toString());
    } catch (MalformedURLException e) {
      System.err.println("Invalid host and/or port: " + host + ", " + rmiPort);
      System.exit(1);
    }
    JmxRemoteOperator jro = null;
    try {
      jro = new JmxRemoteOperator(url);
    } catch (IOException e) {
      System.err.println("Failed to connect the JMX server: " + url);
      System.exit(1);
    }

    try {
      String command = (String)ArgumentParser.getValue(paramMap, "command", 0);
      if (command.equals("list")) {
        try {
          jro.list();
        } catch (IOException e) {
          System.err.println("Lost the connection to the JMX server: " + url);
          System.exit(1);
        }
        return;
      }

      if (command.equals("query")) {
        String query = (String)ArgumentParser.getValue(paramMap, "object_name_or_query", 0);
        if (query == null || query.length() == 0) {
          System.err.println("Specify a query.");
          System.exit(1);
        }
        try {
          jro.query(query);
          return;
        } catch (MalformedObjectNameException e) {
          System.err.println("Invalid query: " + query);
          System.exit(1);
        } catch (IOException e) {
          System.err.println("Lost the connection to the JMX server: " + url);
          System.exit(1);
        }
      }

      String objNameStr = (String)ArgumentParser.getValue(paramMap, "object_name_or_query", 0);
      if (objNameStr == null || objNameStr.length() == 0) {
        System.err.println("Specify a query.");
        System.exit(1);
      }
      ObjectName objName = null;
      try {
        objName = new ObjectName(objNameStr);
      } catch (MalformedObjectNameException e) {
        System.err.println("Invalid object name: " + objNameStr);
        System.exit(1);
      }

      if (command.equals("info")) {
        try {
          jro.printObjectInfo(objName);
        } catch (IOException e) {
          System.err.println("Lost the connection to the JMX server: " + url);
          System.exit(1);
        } catch (InstanceNotFoundException e) {
          System.err.println("No such mbean: " + objNameStr);
          System.exit(1);
        } catch (IntrospectionException e) {
          System.err.println("Failed to get the info due to an exception: ");
          e.printStackTrace();
          System.exit(1);
        } catch (ReflectionException e) {
          System.err.println("Failed to get the info due to an exception: ");
          e.printStackTrace();
          System.exit(1);
        }
        return;
      }
      else if (command.equals("get")) {
        String attrName = (String)ArgumentParser.getValue(paramMap, "command_args", 0);
        try {
          System.out.println(jro.getAttr(objName, attrName));
        } catch (AttributeNotFoundException e) {
          System.err.println("No such attribute: " + attrName);
          System.exit(1);
        } catch (InstanceNotFoundException e) {
          System.err.println("No such mbean: " + objNameStr);
          System.exit(1);
        } catch (MBeanException e) {
          System.err.println("Failed to get the attribute due to an exception: ");
          e.printStackTrace();
          System.exit(1);
        } catch (ReflectionException e) {
          System.err.println("Failed to get the attribute due to an exception: ");
          e.printStackTrace();
          System.exit(1);
        } catch (IOException e) {
          System.err.println("Lost the connection to the JMX server: " + url);
          System.exit(1);
        }
        return;
      }
      else if (command.equals("set")) {
        String attrName = (String)ArgumentParser.getValue(paramMap, "command_args", 0);
        String value = (String)ArgumentParser.getValue(paramMap, "command_args", 1);
        try {
          jro.setAttr(objName, attrName, value);
        } catch (InstanceNotFoundException e) {
          System.err.println("No such mbean: " + objNameStr);
          System.exit(1);
        } catch (AttributeNotFoundException e) {
          System.err.println("No such attribute: " + attrName);
          System.exit(1);
        } catch (InvalidAttributeValueException e) {
          System.err.println("Invalid value: " + value);
          System.exit(1);
        } catch (MBeanException e) {
          System.err.println("Failed to get the info due to an exception: ");
          e.printStackTrace();
          System.exit(1);
        } catch (ReflectionException e) {
          System.err.println("Failed to get the info due to an exception: ");
          e.printStackTrace();
          System.exit(1);
        } catch (IOException e) {
          System.err.println("Lost the connection to the JMX server: " + url);
          System.exit(1);
        }

        System.out.println("Success!");
        return;
      }
      else if (command.equals("invoke")) {
        @SuppressWarnings("unchecked")
        List<String> commandArgs = (List<String>)paramMap.get("command_args");
        String operName = commandArgs.remove(0);
        try {
          jro.invoke(objName, operName, commandArgs);
        } catch (IOException e) {
          System.err.println("Lost the connection to the JMX server: " + url);
          System.exit(1);
        } catch (InstanceNotFoundException e) {
          System.err.println("No such mbean: " + objNameStr);
          System.exit(1);
        } catch (MBeanException e) {
          System.err.println("Failed to get the info due to an exception: ");
          e.printStackTrace();
          System.exit(1);
        } catch (ReflectionException e) {
          System.err.println("Failed to get the info due to an exception: ");
          e.printStackTrace();
          System.exit(1);
        }
      }
      else {
        System.err.println("Unknown command: " + command);
        System.exit(1);
      }
    } finally {
      try {
        jro.close();
      } catch (IOException e) {}
    }
  }

  private static Map<?, ?> parseArgs(String[] args) {
    Map<?, ?> params = null;
    List<String> optList = new ArrayList<String>();
    List<String> paramList = new ArrayList<String>();

    try {
      optList.add("-host[s{=localhost] ");
      optList.add("-rmiPort[i{=" + Registry.REGISTRY_PORT + "}] ");

      paramList.add("-command[s<list|query|info|get|set|invoke>] ");
      paramList.add("+object_name_or_query[s] ");
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

  private void list() throws IOException {
    try {
      @SuppressWarnings("unchecked")
      Set<ObjectName> objNames = connection.queryNames(new ObjectName("*:*"), null);
      for (ObjectName objName: objNames) {
        System.out.println(objName);
      }
    } catch (NullPointerException e) {
      throw new AssertionError("Never get here.");
    } catch (MalformedObjectNameException e) {
      throw new AssertionError("Never get here.");
    }
  }

  private void query(
    String query
  ) throws MalformedObjectNameException, NullPointerException, IOException {
    @SuppressWarnings("unchecked")
    Set<ObjectName> objNames = connection.queryNames(new ObjectName(query), null);
    for (ObjectName objName: objNames) {
      System.out.println(objName);
    }
  }

  private void printObjectInfo(
    ObjectName objName
  ) throws IOException, InstanceNotFoundException, IntrospectionException, ReflectionException {
    MBeanInfo info = connection.getMBeanInfo(objName);

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
        throw new AssertionError("Never get here.");
      } catch (MBeanException e) {
        val = "Filed to get this attribute due to MBeanException";
      } catch (ReflectionException e) {
        val = "Filed to get this attribute due to ReflectionException";
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

  private Object getAttr(ObjectName objName, String attrName)
    throws IOException, AttributeNotFoundException,
      InstanceNotFoundException, MBeanException, ReflectionException
  {
    return connection.getAttribute(objName, attrName);
  }

  private void setAttr(
    ObjectName objName, String attrName, Object value
  ) throws IOException, InstanceNotFoundException, AttributeNotFoundException,
      InvalidAttributeValueException, MBeanException, ReflectionException
  {
    Attribute attr = new Attribute(attrName, value);
    connection.setAttribute(objName, attr);
  }

  private void invoke(
    ObjectName objName, String operName, List<String> operArgs
  ) throws IOException, InstanceNotFoundException, MBeanException, ReflectionException {
    List<Object> paramList = new ArrayList<Object>();
    List<String> signature = new ArrayList<String>();

    for (int i = 0; i < operArgs.size(); i += 2) {
      String type = operArgs.get(i);
      String value = operArgs.get(i + 1);
      if (type.equals("int")) {
        signature.add(type);
        paramList.add(Integer.valueOf(value));
      }
      else if (type.equalsIgnoreCase("String") || type.equals(String.class.getName())) {
        signature.add(String.class.getName());
        paramList.add(value);
      }
      else {
        Class<?> cls = null;
        try {
          cls = Class.forName(type);
        } catch (ClassNotFoundException e1) {
          System.err.println("No such type: " + type);
          System.exit(1);
        }

        Method valueOf = null;
        try {
          valueOf = cls.getMethod("valueOf", String.class);
        } catch (SecurityException e) {
          System.err.println("Unsupported type: " + type);
          System.exit(1);
        } catch (NoSuchMethodException e) {
          System.err.println("Unsupported type: " + type);
          System.exit(1);
        }

        Object param = null;
        try {
          param = valueOf.invoke(null, value);
        } catch (IllegalArgumentException e) {
          System.err.println("Invalid valud and/or type: " + type + ", " + value);
          System.exit(1);
        } catch (IllegalAccessException e) {
          System.err.println("Unsupported type: " + type);
          System.exit(1);
        } catch (InvocationTargetException e) {
          System.err.println("Invalid valud and/or type: " + type + ", " + value);
          System.exit(1);
        }
        signature.add(type);
        paramList.add(param);
      }
    }

    Object result
      = connection.invoke(
          objName, operName,
          paramList.toArray(), signature.toArray(new String[signature.size()])
        );
    System.out.println(result);
  }

  private void close() throws IOException {
    if (connector != null) {
      connector.close();
    }
  }

}
