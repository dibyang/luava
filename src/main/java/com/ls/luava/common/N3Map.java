package com.ls.luava.common;

import com.google.common.collect.Lists;
import com.google.gson.JsonPrimitive;

import java.util.*;

public class N3Map extends HashMap<String, Object> {

  private static final long serialVersionUID = 1L;

  public N3Map() {
    super();
  }

  public N3Map(Map<String, ?> m) {
    super(m);
  }

  public Optional<Object> getValue(String... keys) {
    Object value = this;
    for (String key : keys) {
      if (value instanceof Map) {
        value = ((Map<?, ?>) value).get(key);
      } else {
        value = null;
      }
    }
    return Optional.ofNullable(value);
  }

  public <T> Optional<T> getValue(Class<T> tClass, String... keys) {
    return getValue(tClass, (NameMapping) null, keys);
  }

  public <T> Optional<T> getValue(Class<T> tClass, NameMapping mapping, String... keys) {
    Optional<Object> optional = this.getValue(keys);
    return optional.map(value->Types.cast(value, tClass, mapping));
  }

  public  List<Object> getValues(String... keys){
    return getValues(Object.class,keys);
  }

  public <T> List<T> getValues(Class<T> tClass, String... keys) {
    return getValues(tClass, null, keys);
  }

  public <T> List<T> getValues(Class<T> tClass, NameMapping mapping, String... keys) {
    List<T> list = Lists.newArrayList();
    Optional<Object> optional = this.getValue(keys);
    optional.ifPresent(value-> {
      if (value instanceof Collection) {
        for (Object v : ((Collection<?>) value)) {
          list.add(Types.cast(v, tClass, mapping));
        }
      } else {
        if (value != null) {
          list.add(Types.cast(value, tClass, mapping));
        }
      }
    });
    return list;
  }

  public Optional<String> getString(String... keys) {
    return this.getValue(String.class, keys);
  }

  public List<String> getStrings(String... keys) {
    return this.getValues(String.class, keys);
  }

  public Optional<Long> getLong(String... keys) {
    return this.getValue(Long.class, keys);
  }

  public Optional<Double> getDouble(String... keys) {
    return this.getValue(Double.class, keys);
  }

  public Optional<Integer> getInt(String... keys) {
    return this.getValue(Integer.class, keys);
  }

  public Optional<Boolean> getBoolean(String... keys) {
    return this.getValue(Boolean.class, keys);
  }

  public Optional<Date> getDate(String... keys) {
    return this.getValue(Date.class, keys);
  }

  public Optional<UUID> getUUID(String... keys) {
    return this.getValue(UUID.class, keys);
  }

  public static N3Map of(Map<String, Object> map) {
    N3Map n2Map = new N3Map();
    n2Map.putAll(map);
    return n2Map;
  }

  public static N3Map of2(Map<String, String> map) {
    N3Map n2Map = new N3Map();
    n2Map.putAll(map);
    return n2Map;
  }

}