/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.jmx;

public interface JmxAgent {

  public void start();

  public void stop();

  public void registerPojo(Object mo, String moName);

  public void registerMBean(Object mbean, String moName);

  public void unregisterMBean(String moName);

}
