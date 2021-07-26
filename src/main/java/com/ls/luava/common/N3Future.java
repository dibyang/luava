package com.ls.luava.common;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  static final Logger LOG = LoggerFactory.getLogger(N3Future.class);

  private volatile boolean completed;
  private volatile T result;
  private volatile Throwable t;
  private volatile long startTime;


  private final N3Map context = new N3Map();

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

  public Throwable getThrowable(){
    return this.t;
  }

  public synchronized T tryGet() {
    T t = null;
    try {
      t = get();
    } catch (InterruptedException | ExecutionException e) {
      LOG.warn("ignore exception",e);
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

  public synchronized T tryGet(final long timeout, final TimeUnit unit) {
    T t = null;
    try {
      t = get(timeout, unit);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      LOG.warn("ignore exception",e);
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


  public N3Map getContext() {
    return context;
  }
}
