package com.ls.luava.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;
import java.util.UUID;

/**
 * @author yangzj
 * @date 2021/4/25
 */
public abstract class Types2 {

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

  public static void main(String[] args) {
    UUID uid = UUID.randomUUID();
    System.out.println("uid = " + uid);
    int id = 12345;

    N3Map map = new N3Map();
    map.put("tid",uid);
    map.put("id",id);
    String s = Jsons.i.toJson(map);
    System.out.println("s = " + s);
    N3Map map2 = Jsons.i.fromJson(s,N3Map.class);
    System.out.println("map2 = " + map2);
    final UUID uuid = map2.getUUID("tid").orElse(null);
    System.out.println("uuid = " + uuid);
    System.out.println("uid.equals(uuid) = " + uid.equals(uuid));

  }
}
