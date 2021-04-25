package com.ls.luava.common;

/**
 * IndexBeginMark
 *
 * @author yangzj
 * @version 1.0
 */
class IndexBeginMark implements IndexFunction {
  private String mark;
  private int skip;

  IndexBeginMark(String mark,int skip) {
    this.skip = skip<0?0:skip;
    this.mark = mark;
  }

  @Override
  public int index(boolean last, String value, Integer fromIndex) {
    if (fromIndex == null) fromIndex = 0;
    if(last){
      int lastIndex = value.length();
      for(int i=0;i<=skip;i++){
        lastIndex = value.lastIndexOf(mark,lastIndex-1);
        if(lastIndex<fromIndex){
          break;
        }
      }
      if(lastIndex>=fromIndex){
        lastIndex = lastIndex + mark.length();
      }
      return lastIndex;
    }else {
      for (int i = 0; i <= skip; i++) {
        fromIndex = value.indexOf(mark, fromIndex);
        if (fromIndex < 0) {
          break;
        }
        fromIndex += mark.length();
      }
      return fromIndex;
    }
  }

  static int head(boolean last, String value, Integer fromIndex){
    return 0;
  }

  public static IndexBeginMark c(String mark){
    return c(mark,0);
  }
  public static IndexBeginMark c(String mark,int skip){
    return new IndexBeginMark(mark,skip);
  }



}
