/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.agent.mo;


import org.snmp4j.agent.mo.snmp.SysUpTime;
import org.snmp4j.smi.TimeTicks;


public class SysUpTimeImpl implements SysUpTime, VariableServer {

  private final Object thisLock = new Object();

  private volatile long startTime;
  private volatile boolean running = false;

  public SysUpTimeImpl() {}

  public void start() {
    synchronized (thisLock) {
      this.startTime = System.currentTimeMillis();
      this.running = true;
    }
  }

  public void stop() {
    this.running = false;
  }

  public TimeTicks get() {
    if (running) {
      long upTime = (System.currentTimeMillis() - startTime) / 10L;
      return new TimeTicks(upTime & 0xFFFFFFFFL);
    }
    else {
      return new TimeTicks(0L);
    }
  }

}
