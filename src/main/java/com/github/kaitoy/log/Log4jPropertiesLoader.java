/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/


package com.github.kaitoy.log;

import java.net.URL;

import org.apache.log4j.PropertyConfigurator;


public class Log4jPropertiesLoader {

  private static final Log4jPropertiesLoader INSTANCE
    = new Log4jPropertiesLoader();
  private static final String PROPERTIES_FILE_SUFFIX = ".log4j.properties";
  private static final String LOG4J_PROPERTIES_BASE_PATH_KEY
    = Log4jPropertiesLoader.class.getPackage().getName() + ".propertiesBase";

  private Log4jPropertiesLoader() {}

  public static Log4jPropertiesLoader getInstance() {
    return INSTANCE;
  }

  public void loadPropertyOf(Class<?> clazz) {
    String basePath
      = System.getProperty(
          LOG4J_PROPERTIES_BASE_PATH_KEY,
          Log4jPropertiesLoader.class.getPackage().getName().replace('.', '/')
        );
    URL url = this.getClass().getClassLoader().getResource(
                basePath  + "/"
                  + clazz.getSimpleName() + PROPERTIES_FILE_SUFFIX
              );
    PropertyConfigurator.configure(url);
  }

}
