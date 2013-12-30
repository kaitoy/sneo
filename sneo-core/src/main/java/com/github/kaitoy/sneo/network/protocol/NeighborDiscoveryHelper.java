/*_##########################################################################
  _##
  _##  Copyright (C) 2011-2013  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network.protocol;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.pcap4j.util.MacAddress;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import com.github.kaitoy.sneo.network.MacAddressManager;
import com.github.kaitoy.sneo.network.NetworkInterface;
import com.github.kaitoy.sneo.network.Node;
import com.github.kaitoy.sneo.util.FutureData;
import com.github.kaitoy.sneo.util.NamedThreadFactory;

abstract class NeighborDiscoveryHelper {

  private static final LogAdapter logger
    = LogFactory.getLogger(NeighborDiscoveryHelper.class.getPackage().getName());

  protected static NeighborCache newNdCache(long cacheLife) {
    return new NeighborCache(cacheLife);
  }

  protected static MacAddress resolveVirtualAddress(InetAddress ipAddress) {
    return MacAddressManager.getInstance().resolveVirtualAddress(ipAddress);
  }

  protected static MacAddress resolveRealAddress(
    RequestSender reqSender,
    InetAddress targetIpAddr,
    Node node,
    NetworkInterface nif,
    NeighborCache neighborCache,
    long resolveTimeout,
    TimeUnit unit
  ) {
    FutureData<MacAddress> fd;
    synchronized (neighborCache) {
      fd = neighborCache.getEntry(targetIpAddr);
      if (fd != null) {
        logger.debug("Hit Neighbor cache" + fd);
      }
      else {
        fd = neighborCache.newEntry(targetIpAddr);
      }
    }

    if (!fd.isReady() && !fd.isWorking()) {
      fd.setWorking(true);
      reqSender.sendRequest(
        targetIpAddr,
        node,
        nif
      );
    }

    MacAddress macAddress = null;
    try {
      macAddress = fd.get(resolveTimeout, unit);
      if (logger.isDebugEnabled()) {
        logger.debug(targetIpAddr + " was resolved to " + macAddress);
      }
    } catch (InterruptedException e) {
      logger.warn(e);
      fd.setWorking(false);
    } catch (TimeoutException e) {
      logger.warn(e);
      fd.setWorking(false);
    }

    return macAddress;
  }

  protected static void cache(
    NeighborCache neighborCache, InetAddress ipAddr, MacAddress macAddr
  ) {
    neighborCache.cache(ipAddr, macAddr);
    if (logger.isDebugEnabled()) {
      logger.debug(
        "Cached a neighbor: (" + ipAddr + ", " + macAddr + ")"
      );
    }
  }

  protected static void clearCache(NeighborCache neighborCache) {
    neighborCache.clearCache();
  }

  protected interface RequestSender {

    public void sendRequest(
      InetAddress targetIpAddr,
      Node node,
      NetworkInterface nif
    );

  }

  protected static class NeighborCache {

    private final long cacheLife; // [s]
    private final ScheduledExecutorService scheduler
      = Executors.newSingleThreadScheduledExecutor(
          new NamedThreadFactory(
            NeighborCacheInvalidater.class.getSimpleName(),
            true
          )
        ); // TODO shutdown
    private final Map<InetAddress, FutureData<MacAddress>> table
      = new HashMap<InetAddress, FutureData<MacAddress>>();
    private final Map<InetAddress, Future<?>> invalidateNeighborCacheFutures
      = new HashMap<InetAddress, Future<?>>();

    protected NeighborCache(long cacheLife) {
      this.cacheLife = cacheLife;
    }

    private FutureData<MacAddress> newEntry(InetAddress ipAddress) {
      synchronized (table) {
        FutureData<MacAddress> fd = new FutureData<MacAddress>();
        table.put(ipAddress, fd);
        return fd;
      }
    }

    private FutureData<MacAddress> getEntry(InetAddress ipAddr) {
      synchronized (table) {
        return table.get(ipAddr);
      }
    }

    private void cache(InetAddress ipAddr, MacAddress macAddr) {
      synchronized (table) {
        if (invalidateNeighborCacheFutures.containsKey(ipAddr)) {
          invalidateNeighborCacheFutures.get(ipAddr).cancel(true);
        }

        if (table.containsKey(ipAddr)) {
          FutureData<MacAddress> f = table.get(ipAddr);
          f.set(macAddr);
        }
        else {
          table.put(ipAddr, new FutureData<MacAddress>(macAddr));
        }

        invalidateNeighborCacheFutures.put(
          ipAddr,
          scheduler.schedule(
            new NeighborCacheInvalidater(ipAddr), cacheLife, TimeUnit.SECONDS
          )
        );
      }
    }

    private void clearCache() {
      synchronized (table) {
        table.clear();
        for (Future<?> f: invalidateNeighborCacheFutures.values()) {
          f.cancel(false);
        }
        invalidateNeighborCacheFutures.clear();
      }
    }

    private class NeighborCacheInvalidater implements Runnable {

      private InetAddress ipAddress;

      public NeighborCacheInvalidater(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
      }

      public void run() {
        synchronized (table) {
          if (!Thread.interrupted()) {
            table.remove(ipAddress);
          }
        }
      }

    }

  }

}
