package com.ls.luava.common;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.util.function.Function;

public class DynamicExclusionStrategy implements ExclusionStrategy {
  private final ThreadLocal<ExclusionStrategy>  exclusionStrategyThreadLocal = new ThreadLocal<>();
  @Override
  public boolean shouldSkipField(FieldAttributes fieldAttributes) {
    ExclusionStrategy exclusionStrategy = exclusionStrategyThreadLocal.get();
    if(exclusionStrategy!=null){
      return exclusionStrategy.shouldSkipField(fieldAttributes);
    }
    return false;
  }

  @Override
  public boolean shouldSkipClass(Class<?> aClass) {
    ExclusionStrategy exclusionStrategy = exclusionStrategyThreadLocal.get();
    if(exclusionStrategy!=null){
      return exclusionStrategy.shouldSkipClass(aClass);
    }
    return false;
  }

  public void setExclusionStrategy(ExclusionStrategy exclusionStrategy){
    this.exclusionStrategyThreadLocal.set(exclusionStrategy);
  }


}
