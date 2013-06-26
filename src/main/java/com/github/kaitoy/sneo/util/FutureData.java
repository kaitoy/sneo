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
      if (!isReady()) {
        thisLock.wait();
      }
      return data.get();
    }
  }

  public V get(long timeout, TimeUnit unit)
  throws InterruptedException, TimeoutException {
    synchronized (thisLock) {
      if (!isReady()) {
        unit.timedWait(thisLock, timeout);
      }

      try {
        return data.get();
      } catch (NullPointerException e) {
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

