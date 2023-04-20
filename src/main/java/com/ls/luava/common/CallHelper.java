package com.ls.luava.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 方法调用支持超时控制
 *
 */
public class CallHelper {
  static final Logger LOG = LoggerFactory.getLogger(CallHelper.class);

  private final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1, NamedThreadFactory.create("CallHelper"));
  private final AtomicLong idCreator = new AtomicLong();
  private final ThreadLocal<Long> taskId = new ThreadLocal<>();

  public Long getTaskId() {
    return Optional.ofNullable(taskId.get()).orElse(0L);
  }

  public <T> T call(Callable<T> callable, final long timeout, final TimeUnit unit) throws TimeoutValueException, ExecutionException, InterruptedException {
    final long startTime = System.nanoTime();
    taskId.set(idCreator.incrementAndGet());
    try {
      final FutureTask<T> futureTask = new FutureTask<>(callable);
      scheduled.schedule(() -> {
        if(!futureTask.isDone()){
          futureTask.cancel(true);
        }
      }, timeout, unit);
      futureTask.run();
      return futureTask.get();
    }catch (CancellationException e){
      long waitTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
      TimeoutValueException exception = TimeoutValueException.fromMilliseconds(unit.toMillis(timeout), waitTime);
      throw exception;
    } catch (ExecutionException|InterruptedException e){
      throw e;
    }
  }

  public <T> T callNoThrow(Callable<T> callable, final long timeout, final TimeUnit unit) {
    try {
      return call(callable, timeout, unit);
    }catch (Exception e){
      //e.printStackTrace();
      System.out.println(String.format("Task %s call exception, ", getTaskId())+e.getMessage());
      LOG.warn(String.format("Task %s call exception", getTaskId()), e);
    }
    return null;
  }

  public void shutdown(){
    scheduled.shutdown();
  }



}
