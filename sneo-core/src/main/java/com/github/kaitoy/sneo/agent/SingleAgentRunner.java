/*_##########################################################################
  _##
  _##  Copyright (C) 2012-2015  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.agent;

import java.io.File;
import java.io.IOException;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.snmp4j.SNMP4JSettings;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.log.LogFactory;
import org.snmp4j.util.ArgumentParser;

import com.github.kaitoy.sneo.jmx.HttpJmxAgent;
import com.github.kaitoy.sneo.jmx.JmxAgent;
import com.github.kaitoy.sneo.jmx.SimStopper;
import com.github.kaitoy.sneo.jmx.SimStopper.StopProcedure;
import com.github.kaitoy.sneo.log.Log4jPropertiesLoader;
import com.github.kaitoy.sneo.smi.SmiSyntaxesPropertiesManager;
import com.github.kaitoy.sneo.transport.TransportsPropertiesManager;
import com.github.kaitoy.sneo.util.ColonSeparatedOidTypeValueVariableTextFormat;
import com.github.kaitoy.sneo.util.ConsoleBlocker;
import com.github.kaitoy.sneo.util.NetSnmpVariableTextFormat;

import mx4j.log.Log4JLogger;

public class SingleAgentRunner {

  public static void main(String[] args) {
    LogFactory.setLogFactory(new Log4jLogFactory());
    mx4j.log.Log.redirectTo(new Log4JLogger());
    Log4jPropertiesLoader.getInstance()
      .loadPropertyOf(SingleAgentRunner.class);

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

    try {
      FileMibAgent.Builder agentBuilder;

      String communityStringIndexDelimiter = (String)ArgumentParser.getValue(params, "csid", 0);
      List<String> communityStringIndexes;
      if (params.get("allcsis") != null) {
        communityStringIndexes = new ArrayList<String>();
        File fileMib
          = new File((String)ArgumentParser.getValue(params, "f", 0));
        Pattern p
          = Pattern.compile(
              fileMib.getName() + Pattern.quote(communityStringIndexDelimiter) + ".+"
            );

        for (String f: fileMib.getParentFile().list()) {
          Matcher m = p.matcher(f);
          if (m.matches()) {
            communityStringIndexes.add(f.substring(f.indexOf(communityStringIndexDelimiter) + 1));
          }
        }
      }
      else {
        @SuppressWarnings("unchecked")
        List<String> tmp = (List<String>)params.get("csi");
        communityStringIndexes = tmp;
      }

      if (communityStringIndexes == null || communityStringIndexes.size() == 0) {
        agentBuilder = new FileMibAgent.Builder();
      }
      else {
        agentBuilder
          = new FileMibCiscoAgent.Builder()
              .communityStringIndexes(communityStringIndexes)
              .communityStringIndexDelimiter(communityStringIndexDelimiter);
      }

      String address = (String)ArgumentParser.getValue(params, "a", 0);
      agentBuilder
        .address(
           (String)ArgumentParser.getValue(params, "proto", 0) + ":"
             + address + "/"
             + ArgumentParser.getValue(params, "p", 0)
         )
        .bcConfigFilePath((String)ArgumentParser.getValue(params, "bcfg", 0))
        .configFilePath((String)ArgumentParser.getValue(params, "cfg", 0))
        .communityName((String)ArgumentParser.getValue(params, "c", 0))
        .securityName((String)ArgumentParser.getValue(params, "s", 0))
        .fileMibPath((String)ArgumentParser.getValue(params, "f", 0))
        .trapTarget((String)ArgumentParser.getValue(params, "t", 0));

      String format = (String)ArgumentParser.getValue(params, "format", 0);
      if (format.equals("default")) {
        agentBuilder.format(ColonSeparatedOidTypeValueVariableTextFormat.getInstance());
      }
      else if (format.equals("net-snmp")) {
        agentBuilder.format(NetSnmpVariableTextFormat.getInstance());
      }
      else {
        throw new IllegalArgumentException("Invalid format: " + format);
      }

      final FileMibAgent agent = agentBuilder.build();

      final JmxAgent jmxAgent
        = new HttpJmxAgent(
            (Integer)ArgumentParser.getValue(params, "jmxPort", 0),
            ((Integer)ArgumentParser.getValue(params, "rmiPort", 0)).intValue()
          );
      jmxAgent.registerPojo(
        agent,
        "Nodes:address=" + address.replace(':', '-')
      );

      agent.init();
      agent.start();
      jmxAgent.start();

      if (params.get("d") == null) {
        ConsoleBlocker.block("** Hit Enter key to stop simulation **");
        jmxAgent.stop();
        agent.stop();
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e1) {}
      }
      else {
        jmxAgent.registerPojo(
          new SimStopper(
            new StopProcedure() {
              public void stop() {
                jmxAgent.stop(3000L);
                agent.stop();
              }
            }
          ),
          "Tools:name=SimStopper"
        );
      }
    }
    catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static Map<?, ?> parseArgs(String[] args) {
    Map<?, ?> params = null;
    List<String> optList = new ArrayList<String>();

    try {
      optList.add("-a[s<[0-9.]+|[0-9a-fA-F:]+>] ");
      optList.add("-p[i{=161}] ");
      optList.add("-proto[s{=udp}<udp>] ");
      optList.add("-bcfg[s{=cfg/SingleAgentRunner_bc.cfg}] ");
      optList.add("-cfg[s{=cfg/SingleAgentRunner.cfg}] ");
      optList.add("-c[s{=public}] ");
      optList.add("-s[s{=public}] ");
      optList.add("-f[s] ");
      optList.add("+d[s] ");
      optList.add("+t[s<([0-9.]+|[0-9a-fA-F:]+)/[0-9]+>] ");
      optList.add("-format[s{=default}<(default|net-snmp)>] ");
      optList.add("+csi[s] ");
      optList.add("-csid[s{=@}] ");
      optList.add("+allcsis[s] ");
      optList.add("-jmxPort[i{=8080}] ");
      optList.add("-rmiPort[i{=" + Registry.REGISTRY_PORT + "}] ");

      for (String arg: args) {
        if (
             arg.equals("-h")
          || arg.equals("-help")
          || arg.equals("--help")
          || arg.equals("-?")
        ) {
          prHelp(optList);
          System.exit(0);
        }
      }

      StringBuilder optsBuilder = new StringBuilder();
      for (String opt: optList) {
        optsBuilder.append(opt);
      }

      ArgumentParser parser = new ArgumentParser(optsBuilder.toString(), "");
      params = parser.parse(args);
    }
    catch (ParseException e) {
      prHelp(optList);
      System.exit(1);
    }

    return params;
  }

  private static void prHelp(List<String> optList) { // TODO more information
    System.out.println("Usage: " + SingleAgentRunner.class.getName() + " <Params>");
    System.out.println("Params: ");
    for (String opt: optList) {
      System.out.println("  " + opt);
    }
  }

}
