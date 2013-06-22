/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.tools.console;


import java.lang.reflect.InvocationTargetException;
import org.apache.log4j.Logger;
import org.pcap4j.util.PropertiesLoader;
import org.snmp4j.util.VariableTextFormat;
import com.github.util.ColonSeparatedOidTypeValueVariableTextFormat;


public class SnmpRequestPropertiesLoader {

  private static final Logger logger
   = Logger.getLogger(PropertiesLoader.class);

  private static final String KEY_PREFIX
    = SnmpRequestPropertiesLoader.class.getPackage().getName()
        + ".snmpRequest";
  public static final String AGENT_PROPERTIES_PATH_KEY
    = KEY_PREFIX + ".properties";

  private static final SnmpRequestPropertiesLoader INSTANCE
    = new SnmpRequestPropertiesLoader();

  private PropertiesLoader loader
  = new PropertiesLoader(
      System.getProperty(
        AGENT_PROPERTIES_PATH_KEY,
        KEY_PREFIX.replace('.', '/') + ".properties"
      ),
      true,
      true
    );

  private SnmpRequestPropertiesLoader() {}

  public static SnmpRequestPropertiesLoader getInstance() {
    return INSTANCE;
  }

  public VariableTextFormat getVariableTextFormat() {
    Class<? extends VariableTextFormat> vtfClass
      = loader.<VariableTextFormat>getClass(
          KEY_PREFIX + ".VariableTextFormat",
          ColonSeparatedOidTypeValueVariableTextFormat.class
        );
    try {
      return (VariableTextFormat)vtfClass
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

}
