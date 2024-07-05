package com.ls.luava.common;

import com.google.gson.*;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.ls.luava.spi.GsonSpi;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Json 工具类
 *
 * @author dib
 */
public enum Jsons {
  i;
  final Gson gson;
  private final DynamicExclusionStrategy dynamicExclusionStrategy = new DynamicExclusionStrategy();

  Jsons() {
    GsonBuilder builder = new GsonBuilder();
    builder
        .registerTypeAdapter(Unit.class, UnitTypeAdapter.UNIT)
        .registerTypeAdapter(Size.class, SizeTypeAdapter.SIZE)
        .setObjectToNumberStrategy(ToNumberPolicy.BIG_DECIMAL)
        //.setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
        //.registerTypeAdapter(N3Map.class,new N3MapDeserializer())
        .setExclusionStrategies(dynamicExclusionStrategy)
        .setPrettyPrinting();
    ServiceLoader<GsonSpi> serviceLoader = ServiceLoader.load(GsonSpi.class);
    Iterator<GsonSpi> sels = serviceLoader.iterator();
    while (sels.hasNext()) {
      sels.next().build(builder);
    }
    gson = builder.create();
  }

  public Gson getGson() {
    return gson;
  }

  public String toJson(Object src) {
    return gson.toJson(src);
  }

  public String toJson(Object src, ExclusionStrategy exclusionStrategy) {
    try {
      dynamicExclusionStrategy.setExclusionStrategy(exclusionStrategy);
      return toJson(src);
    }finally {
      dynamicExclusionStrategy.setExclusionStrategy(null);
    }
  }


  void doAction(ExclusionStrategy exclusionStrategy, Runnable runnable){
    try {
      dynamicExclusionStrategy.setExclusionStrategy(exclusionStrategy);
      runnable.run();
    }finally {
      dynamicExclusionStrategy.setExclusionStrategy(null);
    }
  }

  public void toJson(Object src, Appendable writer) {
    gson.toJson(src, writer);
  }

  public void toJson(Object src, Appendable writer, ExclusionStrategy exclusionStrategy) {
    doAction(exclusionStrategy, ()->toJson(src, writer));
  }

  public void toJson(Object src, Type typeOfSrc, Appendable writer) {
    gson.toJson(src, typeOfSrc, writer);
  }

  public void toJson(Object src, Type typeOfSrc, Appendable writer, ExclusionStrategy exclusionStrategy) {
    doAction(exclusionStrategy, ()->toJson(src, typeOfSrc, writer));
  }

  public <T> T fromJson(String json, Class<T> classOfT) {
    return gson.fromJson(json, classOfT);
  }

  <T> T doAction(ExclusionStrategy exclusionStrategy, Supplier<T> supplier){
    try {
      dynamicExclusionStrategy.setExclusionStrategy(exclusionStrategy);
      return supplier.get();
    }finally {
      dynamicExclusionStrategy.setExclusionStrategy(null);
    }
  }

  public <T> T fromJson(String json, Class<T> classOfT, ExclusionStrategy exclusionStrategy) {
    return doAction(exclusionStrategy, ()->fromJson(json, classOfT));
  }

  public <T> T fromJson(String json, Type typeOfT) {
    return gson.fromJson(json, typeOfT);
  }

  public <T> T fromJson(String json, Type typeOfT, ExclusionStrategy exclusionStrategy) {
    return doAction(exclusionStrategy, ()->fromJson(json, typeOfT));
  }

  public <T> T fromJson(Reader reader, Type typeOfT) {
    return gson.fromJson(reader, typeOfT);
  }

  public <T> T fromJson(Reader reader, Type typeOfT, ExclusionStrategy exclusionStrategy) {
    return doAction(exclusionStrategy, ()->fromJson(reader, typeOfT));
  }

  public <T> T fromJson(Reader reader, Class<T> classOfT) {
    return gson.fromJson(reader, classOfT);
  }

  public <T> T fromJson(Reader reader, Class<T> classOfT, ExclusionStrategy exclusionStrategy) {
    return doAction(exclusionStrategy, ()->fromJson(reader, classOfT));
  }

  public <T> T fromJson(JsonElement reader, Type typeOfT) {
    return gson.fromJson(reader, typeOfT);
  }

  public <T> T fromJson(JsonElement reader, Type typeOfT, ExclusionStrategy exclusionStrategy) {
    return doAction(exclusionStrategy, ()->fromJson(reader, typeOfT));
  }

  public <T> T fromJson(JsonElement reader, Class<T> classOfT) {
    return gson.fromJson(reader, classOfT);
  }

  public <T> T fromJson(JsonElement reader, Class<T> classOfT, ExclusionStrategy exclusionStrategy) {
    return doAction(exclusionStrategy, ()->fromJson(reader, classOfT));
  }

  public static void main(String[] args) {
    //String s = "{\\\"runner\\\": {\\\"name\\\": \\\"loadbalance\\\"}}";
    //s = s.replaceAll("\\\\\"","\"");
    //System.out.println("s = " + s);

    String s = null;
    try {
      s = new String(Files.readAllBytes(Paths.get("d:/test/test.txt")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("s = " + s);
    N3Map map = Jsons.i.fromJson(s, N3Map.class);
    System.out.println("map = " + map);
    Optional<byte[]> metaId = map.getValue(byte[].class, "data","meta_id");
    System.out.println("metaId = " + metaId);

  }

}
