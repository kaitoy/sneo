/*_##########################################################################
  _##
  _##  Copyright (C) 2012  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.util.WorkerPool;
import org.snmp4j.util.WorkerTask;

public class ContextfulWorkerPool<T> implements WorkerPool {

  private static final LogAdapter logger
    = LogFactory.getLogger(ContextfulWorkerPool.class.getPackage().getName());

  private static final long SHUTDOWN_TIMEOUT = 2000;
  private static final TimeUnit SHUTDOWN_TIMEOUT_UNIT = TimeUnit.MILLISECONDS;

  private final Map<Thread, T> contextHolder = new HashMap<Thread, T>();
  private final ExecutorService executer;

  public ContextfulWorkerPool(String prefix, int numWorker) {
    executer = Executors.newFixedThreadPool(
                 numWorker,
                 new NamedThreadFactory(prefix, true)
               );
  }

  public void registerContext(T context) {
    contextHolder.put(Thread.currentThread(), context);
  }

  public T unregisterContext() {
    return contextHolder.remove(Thread.currentThread());
  }

  public void execute(final WorkerTask task) {
    T context = contextHolder.remove(Thread.currentThread());
    executer.submit(
      new ContextfulWorkerTask(task, context)
    );
  }

  public boolean tryToExecute(WorkerTask task) {
    execute(task);
    return true;
  }

  public void stop() {
    executer.shutdown();
    try {
      boolean terminated
        = executer.awaitTermination(SHUTDOWN_TIMEOUT, SHUTDOWN_TIMEOUT_UNIT);
      if (!terminated) {
        logger.warn("Termination timeout occured.");
      }
    } catch (InterruptedException e) {
      logger.warn("Termination was interrupted.");
    }
  }

  public void cancel() {
    executer.shutdownNow();
  }

  public boolean isIdle() {
    return true;
  }

  private class ContextfulWorkerTask implements Runnable {

    private final WorkerTask task;
    private final T context;

    private ContextfulWorkerTask(WorkerTask task, T context) {
      this.task = task;
      this.context = context;
    }

    public void run() {
      if (context != null) {
        contextHolder.put(
          Thread.currentThread(),
          context
        );
      }
      task.run();
    }

  }

}
