/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureData<V> {

  private final Object thisLock = new Object();

  private volatile Data data;

  public FutureData() {
    data = null;
  }

  public FutureData(V value) {
    data = new Data(value);
  }

  public boolean isReady() {
    return data != null;
  }

  public void set(V val) {
    synchronized (thisLock) {
      if (isReady()) {
        data = new Data(val);
      }
      else {
        data = new Data(val);
        thisLock.notifyAll();
      }
    }
  }

  public V get() throws InterruptedException {
    synchronized (thisLock) {
      while (!isReady()) {
        thisLock.wait();
      }
      return data.get();
    }
  }

  public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
    long endTime = System.currentTimeMillis() + unit.toMillis(timeout);
    synchronized (thisLock) {
      while (!isReady()) {
        long sleepTime = endTime - System.currentTimeMillis();
        if (sleepTime <= 0) {
          break;
        }
        unit.timedWait(thisLock, unit.convert(sleepTime, TimeUnit.MILLISECONDS));
      }

      if (data != null) {
        return data.get();
      }
      else {
        throw new TimeoutException();
      }
    }
  }

  private class Data {
    private final V value;
    public Data(V value) { this.value = value; }
    public V get() { return value; }
  }

}

