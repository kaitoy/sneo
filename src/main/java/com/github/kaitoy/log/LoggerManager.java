/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.log;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogLevel;

public final class LoggerManager {

  private static final LoggerManager INSTANCE = new LoggerManager();

  private LoggerManager() {}

  public static LoggerManager getInstance() {
    return INSTANCE;
  }

  public SortedMap<String, String> getLoggers() {
    SortedMap<String, String> logLevelOfLogger
      = new TreeMap<String, String>();

    for (
      @SuppressWarnings("unchecked") Iterator<LogAdapter> it
        = LogFactory.getLogFactory().loggers();
      it.hasNext();
    ) {
      LogAdapter logger = it.next();
      logLevelOfLogger.put(
        logger.getName(),
        logger.getLogLevel() + "(" + logger.getEffectiveLogLevel() + ")"
      );
    }

    LogAdapter rootLogger = LogFactory.getLogFactory().getRootLogger();
    logLevelOfLogger.put(
      rootLogger.getName(),
      rootLogger.getLogLevel() + "(" + rootLogger.getEffectiveLogLevel() + ")"
    );

    return logLevelOfLogger;
  }

  public void setLogLevel(String logger, String level) {
    if (logger.equals("root")) {
      LogFactory.getLogFactory().getRootLogger().setLogLevel(new LogLevel(level));
    }
    else {
      LogFactory.getLogger(logger).setLogLevel(new LogLevel(level));
    }
  }

}
