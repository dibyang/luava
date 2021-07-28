package com.ls.luava.common;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    if(v1 != null&&v2!=null){
      return ((Comparable)v1).compareTo(v2);
    }else if(v1==null){
      return -1;
    }else if(v2==null){
      return 1;
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
    Comparator<User> comparator = new PojoComparator<>(User.class,"p").reversed();
    List<User> users = Lists.newArrayList();
    users.add(new User("a1",1.0));
    users.add(new User("a2",2.0));
    users.add(new User("a3",3.0));
    users.add(new User("a4",null));
    users.add(new User("a5",null));
    final List<User> users1 = users.stream().sorted(comparator).collect(Collectors.toList());
    System.out.println("users1 = " + users1);
  }

}
