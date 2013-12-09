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

  public static void main(String[] args) throws IOException, MalformedObjectNameException, NullPointerException {
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
      if (command.equals("list")) {
        jro.list();
        return;
      }

      if (command.equals("query")) {
        String query = (String)ArgumentParser.getValue(paramMap, "object_name_or_query", 0);
        jro.query(query);
        return;
      }

      String objNameStr = (String)ArgumentParser.getValue(paramMap, "object_name_or_query", 0);
      ObjectName objName = new ObjectName(objNameStr);

      if (command.equals("info")) {
        jro.printObjectInfo(objName);
        return;
      }
      else if (command.equals("get")) {
        String attrName = (String)ArgumentParser.getValue(paramMap, "command_args", 0);
        System.out.println(jro.getAttr(objName, attrName));
        return;
      }
      else if (command.equals("set")) {
        String attrName = (String)ArgumentParser.getValue(paramMap, "command_args", 0);
        String value = (String)ArgumentParser.getValue(paramMap, "command_args", 1);
        Attribute result = jro.setAttr(objName, attrName, value);
        if (result != null) {
          System.out.println("Success!");
        }
        return;
      }
      else { // invoke
        @SuppressWarnings("unchecked")
        List<String> commandArgs = (List<String>)paramMap.get("command_args");
        String operName = commandArgs.remove(0);
        jro.invoke(objName, operName, commandArgs);
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

  private void list() throws MalformedObjectNameException, NullPointerException, IOException {
    @SuppressWarnings("unchecked")
    Set<ObjectName> objNames = connection.queryNames(new ObjectName("*:*"), null);
    for (ObjectName objName: objNames) {
      System.out.println(objName);
    }
  }

  private void query(String query) throws MalformedObjectNameException, NullPointerException, IOException {
    @SuppressWarnings("unchecked")
    Set<ObjectName> objNames = connection.queryNames(new ObjectName(query), null);
    for (ObjectName objName: objNames) {
      System.out.println(objName);
    }
  }

  private MBeanInfo getMBeanInfo(ObjectName name) throws IOException {
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

  private void printObjectInfo(ObjectName objName) throws IOException {
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

  private Object getAttr(ObjectName objName, String attrName) throws IOException {
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

  private Attribute setAttr(
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

  private void invoke(
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
      else if (type.equalsIgnoreCase("String") || type.equals(String.class.getName())) {
        signature.add(String.class.getName());
        paramList.add(value);
      }
      else {
        try {
          Class<?> cls = Class.forName(type);
          Method valueOf = cls.getMethod("valueOf", String.class);
          Object param = valueOf.invoke(null, value);
          signature.add(type);
          paramList.add(param);
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
          return;
        } catch (SecurityException e) {
          e.printStackTrace();
          return;
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
          return;
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
          return;
        } catch (IllegalAccessException e) {
          e.printStackTrace();
          return;
        } catch (InvocationTargetException e) {
          e.printStackTrace();
          return;
        }
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

  private void close() throws IOException {
    if (connector != null) {
      connector.close();
    }
  }

}
