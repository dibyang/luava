package com.ls.luava.common;

import java.util.Comparator;

/**
 * @author yangzj
 * @date 2021/7/28
 */
public class ValueComparator<T> implements Comparator<T> {
  @Override
  public int compare(T o1, T o2) {
    if(o1 != null){
      return ((Comparable)o1).compareTo(o2);
    }else if(o2!=null){
      return -((Comparable)o2).compareTo(o1);
    }
    return 0;
  }
}
