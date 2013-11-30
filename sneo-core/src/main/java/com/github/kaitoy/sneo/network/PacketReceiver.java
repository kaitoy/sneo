/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.kaitoy.sneo.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.pcap4j.packet.Packet;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import com.github.kaitoy.sneo.util.NamedThreadFactory;

public abstract class PacketReceiver {

  protected static final LogAdapter logger
    = LogFactory.getLogger(PacketReceiver.class);

  private static final long AWAIT_TERMINATION_TIMEOUT = 2000;
  private static final TimeUnit AWAIT_TERMINATION_TIMEOUT_UNIT
    = TimeUnit.MILLISECONDS;

  private final String name;
  private final StoppableLinkedBlockingQueue<PacketContainer> recvPacketQueue
    = new StoppableLinkedBlockingQueue<PacketContainer>(
        NetworkPropertiesLoader.getPacketQueueSize()
      );
  private final ExecutorService packetTakerExecutor;
  private final ExecutorService packetProcessorThreadPool;
  private final Object thisLock = new Object();

  private Future<?> packetTakerFuture;
  private volatile boolean running = false;

  public PacketReceiver(String name) {
    this.name = name;
    this.packetTakerExecutor
      = Executors.newSingleThreadExecutor(
          new NamedThreadFactory(
              name + "_" + PacketTaker.class.getSimpleName(),
            true
          )
        );
    this.packetProcessorThreadPool
      = Executors.newCachedThreadPool(
          new NamedThreadFactory(name + "_packetProcessor", true)
        );
  }

  public String getName() {
    return name;
  }

  public BlockingQueue<PacketContainer> getRecvPacketQueue() {
    return recvPacketQueue;
  }

  public void start() {
    synchronized (thisLock) {
      if (isRunning()) {
        logger.warn("Already started");
        return;
      }

      recvPacketQueue.start();
      packetTakerFuture = packetTakerExecutor.submit(new PacketTaker());
      running = true;
    }
  }

  public void stop() {
    synchronized (thisLock) {
      if (!isRunning()) {
        logger.warn("Already stopped");
        return;
      }

      packetTakerFuture.cancel(true);

      recvPacketQueue.stop();
      running = false;
    }
  }

  public void shutdown() {
    synchronized (thisLock) {
      if (running) {
        stop();
      }

      packetTakerExecutor.shutdown();
      packetProcessorThreadPool.shutdown();
      try {
        boolean terminated
          = packetTakerExecutor.awaitTermination(
              AWAIT_TERMINATION_TIMEOUT,
              AWAIT_TERMINATION_TIMEOUT_UNIT
            );
        if (!terminated) {
          logger.warn("Couldn't terminate packetTakerExecutor.");
        }

        terminated
          = packetProcessorThreadPool.awaitTermination(
              AWAIT_TERMINATION_TIMEOUT,
              AWAIT_TERMINATION_TIMEOUT_UNIT
            );
        if (!terminated) {
          logger.warn("Couldn't terminate packetProcessorThreadPool.");
        }
      } catch (InterruptedException e) {
        logger.warn(e);
      }
    }

    logger.info("A packet receiver has been shutdown.");
  }

  public boolean isRunning() {
    return running;
  }

  protected abstract void process(PacketContainer pc);

  private class PacketTaker implements Runnable {

    public void run() {
      logger.info("start.");
      while (isRunning()) {
        try {
          final PacketContainer packet = recvPacketQueue.take();
          packetProcessorThreadPool.execute(
            new Runnable() {
              public void run() {
                process(packet);
              }
            }
          );
        } catch (InterruptedException e) {
          break;
        }
      }
      logger.info("stopped.");
    }

  }

  public static class PacketContainer {

    private final Packet packet;
    private final NetworkInterface src;

    public PacketContainer(Packet packet, NetworkInterface src) {
      this.packet = packet;
      this.src = src;
    }

    public Packet getPacket() {
      return packet;
    }

    public NetworkInterface getSrc() {
      return src;
    }

  }

}
