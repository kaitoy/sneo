/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2015  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.jmx;

public interface JmxAgent {

  public void start();

  public void stop(long delayMillis);

  public void stop();

  public void registerPojo(Object mo, String moName);

  public void registerMBean(Object mbean, String moName);

  public void unregisterMBean(String moName);

}
