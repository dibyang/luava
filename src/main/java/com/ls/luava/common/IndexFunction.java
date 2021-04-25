package com.ls.luava.common;

/**
 * IndexFunction
 *
 * @author yangzj
 * @version 1.0
 */
@FunctionalInterface
public interface IndexFunction {
  int index(boolean last,String value, Integer fromIndex);
}
