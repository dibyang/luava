package com.ls.luava.common;


/**
 * IndexEndMark
 *
 * @author yangzj
 * @version 1.0
 */
class IndexEndMark implements IndexFunction {
  private String mark;
  private int skip;

  IndexEndMark(String mark,int skip) {
    this.skip = skip<0?0:skip;
    this.mark = mark;
  }

  @Override
  public int index(boolean last, String value, Integer fromIndex) {
    if (fromIndex == null) fromIndex = 0;
    if(last){
      int lastIndex = value.length();
      for (int i = 0; i <= skip; i++) {
        lastIndex = value.lastIndexOf(mark, lastIndex-1);
        if (lastIndex < fromIndex) {
          break;
        }
      }
      return lastIndex;
    }else {
      int index = 0;
      for (int i = 0; i <= skip; i++) {
        index = value.indexOf(mark, fromIndex);
        if (index < 0) {
          break;
        }
        fromIndex = index + mark.length();
      }
      return index;
    }
  }

  static int tail(boolean last, String value, Integer fromIndex){
    return value.length();
  }

  public static IndexEndMark c(String mark){
    return c(mark,0);
  }

  public static IndexEndMark c(String mark,int skip){
    return new IndexEndMark(mark,skip);
  }


}
