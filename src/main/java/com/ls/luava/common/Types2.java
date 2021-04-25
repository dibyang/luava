package com.ls.luava.common;

import java.util.Optional;

/**
 * @author yangzj
 * @date 2021/4/25
 */
public abstract class Types2 {
  public static final Optional<String> castToString(Object value) {
    try {
      return Optional.ofNullable(Types.castToString(value));
    } catch (Exception e) {
      //
    }
    return Optional.empty();
  }

  public static final <T> Optional<T> cast(Object value, Class<T> targetClass, NameMapping mapping) {
    try {
      return Optional.ofNullable(Types.cast(value,targetClass,mapping));
    } catch (Exception e) {
      //
    }
    return Optional.empty();
  }


  public static final <T> Optional<T> cast(Object value, Class<T> targetClass) {
    return cast(value,targetClass,null);
  }

}
