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

  public <T> T call(Callable<T> callable, final long timeout, final TimeUnit unit) throws ExecutionException, InterruptedException {
    final long startTime = System.nanoTime();
    taskId.set(idCreator.incrementAndGet());
    final FutureCall<T> timeoutCall = new FutureCall<>(callable);
    try {
      scheduled.schedule(() -> {
        long waitTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        TimeoutValueException e = TimeoutValueException.fromMilliseconds(unit.toMillis(timeout), waitTime);
        timeoutCall.timeout(e);
      }, timeout, unit);
      timeoutCall.run();

      return timeoutCall.get();
    } catch (ExecutionException|InterruptedException e){
      throw e;
    }
  }

  public <T> T callNoThrow(Callable<T> callable, final long timeout, final TimeUnit unit) {
    try {
      return call(callable, timeout, unit);
    }catch (Exception e){
      LOG.warn(String.format("Task %s call exception", getTaskId()), e);
    }
    return null;
  }

  public void shutdown(){
    scheduled.shutdown();
  }

  // 耗时的方法
  static int longRunningMethod(long tid) throws InterruptedException, ExecutionException, TimeoutException {
//    for (int i = 1; i <= 10; i++) {
//      System.out.println("这是第"+tid+"任务第" + i + "次打印");
//      //等待1秒
//      Thread.sleep(1000);
//    }
    Future future = new N3Future();
    try {
      future.get(10, TimeUnit.SECONDS);
    }catch (TimeoutException e){
      //
    }
    System.out.println("这是第"+tid+"任务任务结束");
    return 10;
  }

  public static void main(String[] args) throws InterruptedException {
    CallHelper callHelper = new CallHelper();
    ExecutorService service = Executors.newFixedThreadPool(5);
    for(int i=1;i<=10;i++) {
      int finalI = i;
      service.submit(() -> {
        Integer call = callHelper.callNoThrow(() -> {
          return longRunningMethod(callHelper.getTaskId());
        }, 5+ finalI, TimeUnit.SECONDS);
      });
    }
    Thread.sleep(20000);
    service.shutdown();
    callHelper.shutdown();
  }

}
