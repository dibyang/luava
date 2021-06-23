package com.ls.luava.common;

import com.google.common.base.Preconditions;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author admin
 * @param <T>
 */
public class N3Future<T> implements Future<T> {

  private volatile boolean completed;
  private volatile T result;
  private volatile Throwable t;
  private volatile long startTime;

  public N3Future() {
    resetStartTime();
  }

  public void resetStartTime() {
    startTime = TimeHelper.i.getTime();
  }

  @Override
  public boolean isDone() {
    return this.completed;
  }

  private T getResult() throws ExecutionException {
    if (this.t != null) {
        throw new ExecutionException(this.t);
    }
    return this.result;
  }

  public synchronized T get(boolean throwException) throws InterruptedException, ExecutionException {
    T t = null;
    try {
      t = get();
    } catch (InterruptedException | ExecutionException e) {
      if (throwException) {
        throw e;
      }
    }
    return t;
  }

  @Override
  public T get() throws InterruptedException, ExecutionException {
    while (!this.completed) {
      synchronized (this) {
        wait();
      }
    }
    return getResult();
  }

  public synchronized T get(boolean throwException, final long timeout, final TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    T t = null;
    try {
      t = get(timeout, unit);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      if (throwException) {
        throw e;
      }
    }
    return t;
  }

  @Override
  public synchronized T get(final long timeout, final TimeUnit unit) throws InterruptedException,
          ExecutionException, TimeoutException {
    Preconditions.checkNotNull(unit, "Time unit");
    final long msecs = unit.toMillis(timeout);
    long waitTime = msecs;
    if (this.completed) {
      return getResult();
    } else if (waitTime <= 0) {
      throw new TimeoutException();
    } else {
      for (;;) {
        wait(waitTime);
        if (this.completed) {
          return getResult();
        } else {
          waitTime = msecs - (TimeHelper.i.getTime() - startTime);
          if (waitTime <= 0) {
            throw new TimeoutException();
          }
        }
      }
    }
  }

  public boolean completed(final T result) {
    synchronized (this) {
      if (this.completed) {
        return false;
      }
      this.completed = true;
      this.result = result;
      notifyAll();
      return true;
    }
  }

  
  public boolean failed(final Throwable exception) {
    synchronized (this) {
      if (this.completed) {
        return false;
      }
      this.completed = true;
      this.t = exception;
      notifyAll();
    }

    return true;
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    return false;
  }

  @Override
  public boolean isCancelled() {
    return false;
  }

}
