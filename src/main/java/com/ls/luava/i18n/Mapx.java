package com.ls.luava.i18n;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.ls.luava.common.Finder;
import com.ls.luava.common.N3Map;
import com.ls.luava.common.Size;
import com.ls.luava.common.Types;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.function.Function;

/**
 *
 *
 *
 * @author yangzj
 * @date 2021/7/6
 */
public class Mapx extends N3Map {

  public static final String MARK = "|";
  volatile Map<String,SimpleDateFormat> dateFormatCache = Maps.newHashMap();
  volatile Map<String,DecimalFormat> decimalFormatCache = Maps.newHashMap();
  private Function<String,Object> parent = null;
  public Mapx(){
  }

  public Mapx(Map<String, ?> m) {
    super(m);
  }

  public Mapx(Function<String,Object> parent) {
    this.parent = parent;
  }

  public Mapx(Map<String, ?> m, Function<String,Object> parent) {
    super(m);
    this.parent = parent;
  }

  DecimalFormat getDecimalFormat(String pattern){
    synchronized (decimalFormatCache){
      DecimalFormat format = decimalFormatCache.get(pattern);
      if(format==null){
        format = (DecimalFormat)NumberFormat.getInstance();
        format.applyPattern(pattern);
        decimalFormatCache.put(pattern,format);
      }
      return format;
    }
  }

  SimpleDateFormat getDateFormat(String pattern){
    synchronized (dateFormatCache) {
      SimpleDateFormat dateFormat = dateFormatCache.get(pattern);
      if(dateFormat==null){
        dateFormat = new SimpleDateFormat(pattern);
        dateFormatCache.put(pattern, dateFormat);
      }
      return dateFormat;
    }
  }

  @Override
  public Object get(Object key) {
    String _key = (String) key;
    final Finder finder = Finder.c(_key);
    String name = finder.head(MARK, Finder.nullToParent).getValue().trim();
    Object value = super.get(name);
    if(value==null&&parent!=null){
      value = parent.apply(name);
    }
    if(value==null) {
      value = finder.tail(MARK).head(MARK, Finder.nullToParent).getValue();
    }

    String type = finder.tail(MARK,1).head(MARK, Finder.nullToParent).getNullableValue().orElse("");
    if(!type.isEmpty()) {
      String pattern = finder.tail(MARK,2).getValue();
      if ("date".equals(type)) {
        value = Types.castToDate(value);
        if(!Strings.isNullOrEmpty(pattern)){
          value = getDateFormat(pattern).format(value);
        }
      }else if ("size".equals(type)) {
        if(value!=null){
          value = Size.parse(value.toString());
        }else{
          value = Size.toSize(0);
        }

      } else if("int".equals(type)||"double".equals(type)){
        if ("int".equals(type)) {
          value = Types.castToLong(value);
        }
        if ("double".equals(type)) {
          value = Types.castToDouble(value);
        }
        if(!Strings.isNullOrEmpty(pattern)){
          value = getDecimalFormat(pattern).format(value);
        }
      }
    }
    return value;
  }
}
