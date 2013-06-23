/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.util;

import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {

  private volatile String name;
  private volatile boolean createDaemon;

  public NamedThreadFactory() {
    this(null, false);
  }

  public NamedThreadFactory(String prefix) {
    this(prefix, false);
  }

  public NamedThreadFactory(boolean createDaemon) {
    this(null, createDaemon);
  }

  public NamedThreadFactory(String prefix, boolean createDaemon) {
    this.name = prefix;
    this.createDaemon = createDaemon;
  }

  public String getPrefix() {
    return name;
  }

  public void setPrefix(String prefix) {
    this.name = prefix;
  }

  public boolean getDaemon() {
    return createDaemon;
  }

  public void setDaemon(boolean createDaemon) {
    this.createDaemon = createDaemon;
  }

  public Thread newThread(Runnable r) {
    Thread t;
    if (name != null) {
      t = new Thread(r, name);
    }
    else {
      t = new Thread(r);
    }

    t.setDaemon(createDaemon);

    return t;
  }

}
