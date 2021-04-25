package com.ls.luava.common;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class NameMapping {
  private final BiMap<String, String> mapping = HashBiMap.create();

  public NameMapping map(String source, String target) {
    mapping.put(source, target);
    return this;
  }

  public NameMapping remove(String source) {
    mapping.remove(source);
    return this;
  }

  public NameMapping clear() {
    mapping.clear();
    return this;
  }

  public String getTarget(String source) {
    String target = mapping.get(source);
    if (target == null) {
      target = source;
    }
    return target;
  }
  
  public String getSource(String target) {
    String source = mapping.inverse().get(target);
    if (source == null) {
      source = target;
    }
    return source;
  }

  public static NameMapping c() {
    return new NameMapping();
  }

}
