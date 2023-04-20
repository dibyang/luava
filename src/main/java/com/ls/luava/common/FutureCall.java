package com.ls.luava.common;

import java.util.concurrent.*;

public class FutureCall<V> extends FutureTask<V> {
  private TimeoutValueException timeoutValueException;
  private InterruptedException e;
  public FutureCall(Callable<V> callable) {
    super(callable);
  }

  public FutureCall(Runnable runnable, V result) {
    super(runnable, result);
  }

  public void timeout(TimeoutValueException e){
    timeoutValueException = e;
    cancel(true);
  }

  @Override
  public V get() throws InterruptedException, ExecutionException {
    try {
      return super.get();
    } catch (CancellationException e) {
      if(this.e !=null){
        throw this.e;
      }else {
        throw e;
      }
    }
  }

  @Override
  public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    try {
      return super.get(timeout, unit);
    } catch (CancellationException e) {
      if(this.e !=null){
        throw this.e;
      }else {
        throw e;
      }
    }
  }

  @Override
  protected void setException(Throwable t) {
    super.setException(t);
    if(t instanceof InterruptedException){
      if(timeoutValueException!=null){
        t.initCause(timeoutValueException);
        timeoutValueException = null;
        this.e = (InterruptedException)t;
      }
    }
  }

}
