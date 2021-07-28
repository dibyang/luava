package com.ls.luava.common;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * @author yangzj
 * @date 2021/7/28
 */
public class PojoComparator<T> implements Comparator<T> {
  private final String fieldName;
  private final Field field;

  public PojoComparator(Class<T> clazz,String fieldName) throws NoSuchFieldException {
    this.fieldName = fieldName;
    field = clazz.getField(fieldName);
    field.setAccessible(true);
  }


  @Override
  public int compare(T o1, T o2) {
    Object v1 = getValue(o1);
    Object v2 = getValue(o2);
    if(v1 != null){
      return ((Comparable)v1).compareTo(v2);
    }else if(v2!=null){
      return -((Comparable)v2).compareTo(v1);
    }
    return 0;
  }

  private Object getValue(T obj) {
    Object value = null;
    if(obj != null){
      try {
        value = field.get(obj);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return value;
  }


}
