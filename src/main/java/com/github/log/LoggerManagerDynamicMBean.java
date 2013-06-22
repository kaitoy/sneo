/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.log;

import java.util.SortedMap;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import mx4j.AbstractDynamicMBean;

public class LoggerManagerDynamicMBean extends AbstractDynamicMBean {

  private final String mbeanClassName;
  private final String moClassName;

  public LoggerManagerDynamicMBean(LoggerManager mo) {
    this.mbeanClassName = this.getClass().getName();
    this.moClassName = mo.getClass().getName();
    setResource(mo);
  }

  protected String getMBeanDescription() {
    return "DynamicMBean of " + moClassName;
  }

  protected String getMBeanClassName() {
    return mbeanClassName;
  }

  protected MBeanOperationInfo[] createMBeanOperationInfo() {
    return new MBeanOperationInfo[] {
      new MBeanOperationInfo(
        "getLoggers",
        "Gets all loggers' names and their log levels.",
        new MBeanParameterInfo[0],
        SortedMap.class.getName(),
        MBeanOperationInfo.INFO
      ),
      new MBeanOperationInfo(
        "setLogLevel",
        "Sets log level to specified level for specified logger.",
        new MBeanParameterInfo[] {
          new MBeanParameterInfo(
            "logger",
            String.class.getName(),
            "The full name of target logger"
          ),
          new MBeanParameterInfo(
            "level",
            String.class.getName(),
            "The target log level: FATAL, ERROR, WARN, INFO, or DEBUG."
          ),
        },
        Void.class.getName(),
        MBeanOperationInfo.ACTION
      ),
    };
  }
}
