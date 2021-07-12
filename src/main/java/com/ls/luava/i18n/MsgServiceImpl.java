package com.ls.luava.i18n;

import com.google.common.collect.Maps;
import com.ls.luava.common.N3Map;
import com.ls.luava.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author yangzj
 * @date 2021/7/7
 */
public class MsgServiceImpl implements MsgService{
  static Logger LOG = LoggerFactory.getLogger(MsgService.class);

  private final Map<String,Msg> msgs = Maps.newConcurrentMap();

  @Override
  public Msg getMsg(String name) {
    return msgs.get(name);
  }

  @Override
  public void addMsg(Msg msg) {
    msgs.put(msg.name(),msg);
  }

  @Override
  public void addMsgs(Class<? extends Msg> clazz) {
    final Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      if(Modifier.isStatic(field.getModifiers())){
        if(Msg.class.isAssignableFrom(field.getType())){
          field.setAccessible(true);
          try {
            final Msg msg = (Msg)field.get(null);
            addMsg(msg);
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  @Override
  public String getMessage(String name, Map<String, Object> data) {
    return getMessage(name,data,Locale.getDefault());
  }

  @Override
  public String getMessage(String name, Map<String, Object> data, Locale locale) {
    final Msg msg = this.getMsg(name);
    if(msg!=null) {
      String s = msg.getMessage();
      ResourceBundle messages = getResourceBundle();
      if(messages!=null&&messages.containsKey(msg.name())) {
        s = messages.getString(msg.name());
      }
      Mapx mapx = new Mapx(data, key -> {
        Object message = getMessage(key, data, locale);
        data.put(key, message);
        return message;
      });

      return StrSubstitutor.replace(s,mapx);
    }
    return null;
  }

  private ResourceBundle getResourceBundle() {
    ResourceBundle messages = null;
    try {
      messages = ResourceBundle.getBundle("messages");
    } catch (Exception e) {
      LOG.warn("getBundle fail",e);
    }
    return messages;
  }

}
