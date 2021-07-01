package com.ls.luava.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.ls.luava.spi.GsonSpi;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

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
      //.setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
      //.registerTypeAdapter(N3Map.class,new N3MapDeserializer())
      .setPrettyPrinting();
    ServiceLoader<GsonSpi> serviceLoader = ServiceLoader.load(GsonSpi.class);
    Iterator<GsonSpi> sels = serviceLoader.iterator();
    while (sels.hasNext()) {
      sels.next().build(builder);
    }
    gson = builder.create();
    try {
      Field factories = Gson.class.getDeclaredField("factories");
      factories.setAccessible(true);
      Object o = factories.get(gson);
      Class<?>[] declaredClasses = Collections.class.getDeclaredClasses();
      for (Class c : declaredClasses) {
        if ("java.util.Collections$UnmodifiableList".equals(c.getName())) {
          Field listField = c.getDeclaredField("list");
          listField.setAccessible(true);
          List<TypeAdapterFactory> list = (List<TypeAdapterFactory>) listField.get(o);
          int i = list.indexOf(ObjectTypeAdapter.FACTORY);
          list.set(i, ObjectTypeAdapter2.FACTORY);
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
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

  public static void main(String[] args) {
    String s = "{\\\"runner\\\": {\\\"name\\\": \\\"loadbalance\\\"}}";
    s = s.replaceAll("\\\\\"","\"");
    System.out.println("s = " + s);
    N3Map map = Jsons.i.fromJson(s, N3Map.class);
    System.out.println("map = " + map);

  }

}
