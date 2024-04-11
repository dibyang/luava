package com.ls.luava.common.cache;


import com.google.common.base.Preconditions;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Cache4Time<T> {
  /**
   * 默认缓存周期，单位毫秒
   */
  static final long DEFAULT_LIFE_TIME = 60*60*1000;
  private volatile long lastTime = 0;
  private final Supplier<CompletableFuture<T>> supplier;
  /**
   * 缓存时间，单位毫秒
   */
  private volatile long lifeTime = DEFAULT_LIFE_TIME;
  private T data = null;

  /**
   * 是否使用访问时间模式
   */
  private boolean accessTime = false;

  /**
   *
   * @param supplier 数据获取接口
   */
  public Cache4Time(Supplier<CompletableFuture<T>> supplier) {
    this(supplier,DEFAULT_LIFE_TIME);
  }

  /**
   *
   * @param supplier 数据获取接口
   * @param lifeTime 缓存时间，单位毫秒
   */
  public Cache4Time(Supplier<CompletableFuture<T>> supplier, long lifeTime) {
    Preconditions.checkNotNull(supplier);
    this.supplier = supplier;
    this.lifeTime = lifeTime;
  }

  /**
   * 是否使用访问时间模式
   */
  public boolean isAccessTime() {
    return accessTime;
  }

  /**
   * 设置是否使用访问时间模式
   * @param accessTime 是否使用访问时间模式
   */
  public Cache4Time setAccessTime(boolean accessTime) {
    this.accessTime = accessTime;
    return this;
  }

  /**
   * 缓存时间，单位毫秒
   * @return
   */
  public long getLifeTime() {
    return lifeTime;
  }


  /**
   * 缓存时间，单位毫秒
   * @param ms 缓存时间，单位毫秒
   */
  public void setLifeTime(long ms) {
    this.lifeTime = ms;
  }

  
  public synchronized T getData(){
    CompletableFuture<T> future = this.supplier.get();
    try {
      future.get();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e.getCause());
    }
    return data;
  }

  public synchronized CompletableFuture<T> getAsyncData(){
    if(isExpired()) {
      CompletableFuture<T> future = this.supplier.get();
      future.thenAccept(r->{
        this.data = r;
        this.lastTime = System.nanoTime();
      });
      return future;
    }else{
      CompletableFuture<T> future = new CompletableFuture<>();
      future.complete(data);
      if(accessTime){
        lastTime = System.nanoTime();
      }
      return future;
    }
  }

  public synchronized Cache4Time<T> expired() {
    lastTime = 0;
    return this;
  }

  /**
   * 获取数据获取或访问的nano时间戳
   * @return
   */
  public long getLastTime() {
    return lastTime;
  }

  public boolean isExpired() {
    if(lastTime >0) {
      long now = System.nanoTime();
      long offset = TimeUnit.NANOSECONDS.toMillis(now - lastTime);
      return offset < 0 || offset > lifeTime;
    }
    return false;
  }


  /**
   *
   * @param supplier 异步获取数据接口
   */
  public static <T> Cache4Time<T> of(Supplier<CompletableFuture<T>> supplier){
    return of(supplier, DEFAULT_LIFE_TIME);
  }

  /**
   *
   * @param supplier 异步获取数据接口
   * @param lifeTime 缓存时间，单位毫秒
   */
  public static <T> Cache4Time<T> of(Supplier<CompletableFuture<T>> supplier, long lifeTime){
    return new Cache4Time<T>(supplier, lifeTime);
  }


}
