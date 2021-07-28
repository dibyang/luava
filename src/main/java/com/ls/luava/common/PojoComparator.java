package com.ls.luava.common;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.Map;

/**
 * @author yangzj
 * @date 2021/7/28
 */
public class PojoComparator<T> implements Comparator<T> {
  private final String fieldName;
  private Field field;

  public PojoComparator(Class<T> clazz, String fieldName) {
    this.fieldName = fieldName;
    if(Map.class.isAssignableFrom(clazz)){
      field = null;
    }else {
      for (Field declaredField : clazz.getDeclaredFields()) {
        if(declaredField.getName().equalsIgnoreCase(fieldName)){
          field = declaredField;
          field.setAccessible(true);
          break;
        }
      }
    }
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
      if(field!=null){
        try {
          value = field.get(obj);
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }else{
        if(obj instanceof Map){
          value = ((Map)obj).get(fieldName);
        }
      }
    }
    return value;
  }

  public static void main(String[] args) {
    PojoComparator<User> comparator = new PojoComparator<>(User.class,"name");

  }

}
