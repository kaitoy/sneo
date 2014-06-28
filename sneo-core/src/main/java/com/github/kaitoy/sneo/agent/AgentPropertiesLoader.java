/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.agent;


import java.lang.reflect.InvocationTargetException;
import org.apache.log4j.Logger;
import org.pcap4j.util.PropertiesLoader;
import org.snmp4j.util.VariableTextFormat;
import com.github.kaitoy.sneo.util.ColonSeparatedOidTypeValueVariableTextFormat;
import com.github.kaitoy.sneo.util.SneoVariableTextFormat;

public class AgentPropertiesLoader {

  private static final Logger logger
   = Logger.getLogger(PropertiesLoader.class);

  private static final String KEY_PREFIX
    = AgentPropertiesLoader.class.getPackage().getName();
  public static final String AGENT_PROPERTIES_PATH_KEY
    = KEY_PREFIX + ".properties";

  private static final AgentPropertiesLoader INSTANCE
    = new AgentPropertiesLoader();

  private PropertiesLoader loader
    = new PropertiesLoader(
        System.getProperty(
          AGENT_PROPERTIES_PATH_KEY,
          KEY_PREFIX.replace('.', '/') + "/agent.properties"
        ),
        true,
        true
      );

  private AgentPropertiesLoader() {}

  public static AgentPropertiesLoader getInstance() {
    return INSTANCE;
  }

  public int getWorkerPoolSize() {
    return loader.getInteger(
             KEY_PREFIX + ".workerPoolSize",
             2
           );
  }

  public boolean extendSmiSyntaxes() {
    return loader.getBoolean(
             KEY_PREFIX + ".extendSmiSyntaxes",
             Boolean.FALSE
           );
  }

  public boolean extendTransportMappings() {
    return loader.getBoolean(
             KEY_PREFIX + ".extendTransportMappings",
             Boolean.FALSE
           );
  }

  public SneoVariableTextFormat getVariableTextFormat() {
    Class<? extends VariableTextFormat> vtfClass
      = loader.<VariableTextFormat>getClass(
          KEY_PREFIX + ".VariableTextFormat",
          ColonSeparatedOidTypeValueVariableTextFormat.class
        );
    try {
      return (SneoVariableTextFormat)vtfClass
               .getMethod("getInstance").invoke(null);
    } catch (SecurityException e) {
      logger.warn(
        new StringBuilder()
          .append("[").append(loader.getResourceName())
          .append("] Failed to get instance because of ")
          .append(e.getMessage())
          .append(", use defalut value: ")
          .append(ColonSeparatedOidTypeValueVariableTextFormat.class.getName())
      );
      return ColonSeparatedOidTypeValueVariableTextFormat.getInstance();
    } catch (NoSuchMethodException e) {
      logger.warn(
        new StringBuilder()
          .append("[").append(loader.getResourceName())
          .append("] Failed to get instance because of ")
          .append(e.getMessage())
          .append(", use defalut value: ")
          .append(ColonSeparatedOidTypeValueVariableTextFormat.class.getName())
      );
      return ColonSeparatedOidTypeValueVariableTextFormat.getInstance();
    } catch (IllegalArgumentException e) {
      logger.warn(
        new StringBuilder()
          .append("[").append(loader.getResourceName())
          .append("] Failed to get instance because of ")
          .append(e.getMessage())
          .append(", use defalut value: ")
          .append(ColonSeparatedOidTypeValueVariableTextFormat.class.getName())
      );
      return ColonSeparatedOidTypeValueVariableTextFormat.getInstance();
    } catch (IllegalAccessException e) {
      logger.warn(
        new StringBuilder()
          .append("[").append(loader.getResourceName())
          .append("] Failed to get instance because of ")
          .append(e.getMessage())
          .append(", use defalut value: ")
          .append(ColonSeparatedOidTypeValueVariableTextFormat.class.getName())
      );
      return ColonSeparatedOidTypeValueVariableTextFormat.getInstance();
    } catch (InvocationTargetException e) {
      logger.warn(
        new StringBuilder()
          .append("[").append(loader.getResourceName())
          .append("] Failed to get instance because of ")
          .append(e.getMessage())
          .append(", use defalut value: ")
          .append(ColonSeparatedOidTypeValueVariableTextFormat.class.getName())
      );
      return ColonSeparatedOidTypeValueVariableTextFormat.getInstance();
    }
  }

  public boolean emulateSysUpTime() {
    return loader.getBoolean(
             KEY_PREFIX + ".emulateSysUpTime",
             Boolean.FALSE
           );
  }
}
