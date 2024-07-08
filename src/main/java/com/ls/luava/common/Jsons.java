package com.ls.luava.common;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.ls.luava.spi.GsonSpi;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Json 工具类
 *
 * @author dib
 */
public enum Jsons {
  i;
  final Gson gson;
  Jsons() {
    GsonBuilder builder = new GsonBuilder();
    builder
        .registerTypeAdapter(Unit.class, UnitTypeAdapter.UNIT)
        .registerTypeAdapter(Size.class, SizeTypeAdapter.SIZE)
        .setObjectToNumberStrategy(ToNumberPolicy.BIG_DECIMAL)
        //.setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
        //.registerTypeAdapter(N3Map.class,new N3MapDeserializer())
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


  public void toJson(Object src, Appendable writer) {
    gson.toJson(src, writer);
  }


  public void toJson(Object src, Type typeOfSrc, Appendable writer) {
    gson.toJson(src, typeOfSrc, writer);
  }

  public <T> T fromJson(String json, Class<T> classOfT) {
    return gson.fromJson(json, classOfT);
  }


  public <T> T fromJson(String json, Type typeOfT) {
    return gson.fromJson(json, typeOfT);
  }

  public <T> T fromJson(Reader reader, Type typeOfT) {
    return gson.fromJson(reader, typeOfT);
  }

  public <T> T fromJson(Reader reader, Class<T> classOfT) {
    return gson.fromJson(reader, classOfT);
  }

  public <T> T fromJson(JsonElement reader, Type typeOfT) {
    return gson.fromJson(reader, typeOfT);
  }

  public <T> T fromJson(JsonElement reader, Class<T> classOfT) {
    return gson.fromJson(reader, classOfT);
  }

}
