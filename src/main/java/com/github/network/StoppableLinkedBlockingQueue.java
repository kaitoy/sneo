/*_##########################################################################
  _##
  _##  Copyright (C) 2011  Kaito Yamada
  _##
  _##########################################################################
*/

package com.github.network;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class StoppableLinkedBlockingQueue<E> implements BlockingQueue<E> {

  private final BlockingQueue<E> q;
  private volatile boolean running = false;
  private Object lock = new Object();

  public StoppableLinkedBlockingQueue() {
    q = new LinkedBlockingQueue<E>();
  }

  public StoppableLinkedBlockingQueue(Collection<? extends E> c) {
    q = new LinkedBlockingQueue<E>(c);
  }

  public StoppableLinkedBlockingQueue(int capacity) {
    q = new LinkedBlockingQueue<E>(capacity);
  }

  public void start() {
    synchronized (lock) {
      q.clear();
      running = true;
      lock.notifyAll();
    }
  }

  public void stop() {
    synchronized (lock) {
      q.clear();
      running = false;
    }
  }

  public boolean isRunning() {
    synchronized (lock) {
      return running;
    }
  }

  public E element() {
    if (isRunning()) {
      return q.element();
    }
    else {
      throw new NoSuchElementException();
    }
  }

  public E peek() {
    if (isRunning()) {
      return q.peek();
    }
    else {
      return null;
    }
  }

  public E poll() {
    if (isRunning()) {
      return q.poll();
    }
    else {
      return null;
    }
  }

  public E remove() {
    if (isRunning()) {
      return q.remove();
    }
    else {
      throw new NoSuchElementException();
    }
  }

  public boolean addAll(Collection<? extends E> c) {
    throw new UnsupportedOperationException();
  }

  public void clear() {
    q.clear();
  }

  public boolean contains(Object o) {
    if (isRunning()) {
      return q.contains(o);
    }
    else {
      return false;
    }
  }

  public boolean containsAll(Collection<?> c) {
    if (isRunning()) {
      return q.containsAll(c);
    }
    else {
      return false;
    }
  }

  public boolean isEmpty() {
    if (isRunning()) {
      return q.isEmpty();
    }
    else {
      return true;
    }
  }

  public Iterator<E> iterator() {
    return q.iterator();
  }

  public boolean remove(Object o) {
    if (isRunning()) {
      return q.remove(o);
    }
    else {
      return false;
    }
  }

  public boolean removeAll(Collection<?> c) {
    if (isRunning()) {
      return q.removeAll(c);
    }
    else {
      return false;
    }
  }

  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException ();
  }

  public int size() {
    if (isRunning()) {
      return q.size();
    }
    else {
      return 0;
    }
  }

  public Object[] toArray() {
    if (isRunning()) {
      return q.toArray();
    }
    else {
      return new Object[0];
    }
  }

  public <T> T[] toArray(T[] a) {
    if (isRunning()) {
      return q.toArray(a);
    }
    else {
      return new LinkedBlockingQueue<T>(0).toArray(a);
    }
  }

  public boolean add(E o) {
    if (isRunning()) {
      return q.add(o);
    }
    else {
      return false;
    }
  }

  public int drainTo(Collection<? super E> c) {
    if (isRunning()) {
      return q.drainTo(c);
    }
    else {
      return 0;
    }
  }

  public int drainTo(Collection<? super E> c, int maxElements) {
    if (isRunning()) {
      return q.drainTo(c, maxElements);
    }
    else {
      return 0;
    }
  }

  public boolean offer(E o) {
    if (isRunning()) {
      return q.offer(o);
    }
    else {
      return false;
    }
  }

  public boolean offer(E o, long timeout, TimeUnit unit)
  throws InterruptedException {
    if (isRunning()) {
      return q.offer(o, timeout, unit);
    }
    else {
      return false;
    }
  }

  public E poll(long timeout, TimeUnit unit) throws InterruptedException {
    if (isRunning()) {
      return q.poll(timeout, unit);
    }
    else {
      return null;
    }
  }

  public void put(E o) throws InterruptedException {
    synchronized (lock) {
      while (!isRunning()) {
        lock.wait();
      }
    }
    q.put(o);
  }

  public int remainingCapacity() {
    if (isRunning()) {
      return q.remainingCapacity();
    }
    else {
      return 0;
    }
  }

  public E take() throws InterruptedException {
    synchronized (lock) {
      while (!isRunning()) {
        lock.wait();
      }
    }
    return q.take();
  }

}

