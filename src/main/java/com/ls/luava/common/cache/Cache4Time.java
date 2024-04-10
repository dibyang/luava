package com.ls.luava.common.cache;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Cache4Time<T> {
  /**
   * 默认缓存周期，单位毫秒
   */
  static final long DEFAULT_LIFE_TIME = 60*60*1000;
  private volatile long lastTime = 0;
  private final Supplier<T> supplier;
  /**
   * 缓存周期，单位毫秒
   */
  private volatile long lifeTime = DEFAULT_LIFE_TIME;
  private T data = null;

  /**
   * 是否使用访问时间模式
   */
  private boolean accessTime = false;
  
  public Cache4Time(Supplier<T> supplier) {
    this(supplier,DEFAULT_LIFE_TIME);
  }
  
  public Cache4Time(Supplier<T> supplier, long lifeTime) {
    this.supplier = supplier;
    this.lifeTime = lifeTime;
  }

  /**
   * 缓存周期，单位毫秒
   * @return
   */
  public long getLifeTime() {
    return lifeTime;
  }


  /**
   * 缓存周期，单位毫秒
   * @param ms
   */
  public void setLifeTime(long ms) {
    this.lifeTime = ms;
  }

  
  public synchronized T getData(){
    if(isExpired())
    {
      data = this.supplier.get();
      lastTime = System.nanoTime();
    }else if(accessTime){
      lastTime = System.nanoTime();
    }
    return data;
  }

  public synchronized CompletableFuture<T> getAsyncData(){
    CompletableFuture<T> future = CompletableFuture.supplyAsync(()->getData());
    return future;
  }

  public synchronized Cache4Time<T> expired() {
    lastTime = 0;
    return this;
  }

  public boolean isExpired() {
    if(lastTime >0) {
      long now = System.nanoTime();
      long offset = TimeUnit.NANOSECONDS.toMillis(now - lastTime);
      return offset < 0 || offset > lifeTime;
    }
    return false;
  }
  
  public static <T> Cache4Time<T> of(Supplier<T> supplier){
    return of(supplier, DEFAULT_LIFE_TIME);
  }
  
  
  public static <T> Cache4Time<T> of(Supplier<T> supplier, long lifeTime){
    return new Cache4Time<T>(supplier, lifeTime);
  }
  
}
