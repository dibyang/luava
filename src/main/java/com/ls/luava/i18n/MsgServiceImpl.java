package com.ls.luava.i18n;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.ls.luava.common.N3Map;
import com.ls.luava.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
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
    String s = null;
    ResourceBundle messages = getResourceBundle(locale);
    if (messages != null && messages.containsKey(name)) {
      s = messages.getString(name);
      String ns = new String(s.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.ISO_8859_1);
      if (ns.equals(s)) {
        s = new String(s.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
      }
    }
    if (Strings.isNullOrEmpty(s)) {
      final Msg msg = this.getMsg(name);
      if (msg != null) {
        s = msg.getMessage();
      }
    }
    if(!Strings.isNullOrEmpty(s)){
      Mapx mapx = new Mapx(data, key -> {
        Object message = getMessage(key, data, locale);
        data.put(key, message);
        return message;
      });
      StrSubstitutor strSubstitutor = new StrSubstitutor(mapx);
      //strSubstitutor.setPreserveEscapes(false);
      strSubstitutor.setEnableSubstitutionInVariables(true);
      return strSubstitutor.replace(s);
    }
    return name;
  }

  private ResourceBundle getResourceBundle(Locale locale) {
    ResourceBundle messages = null;
    try {
      messages = ResourceBundle.getBundle("messages",locale);
    } catch (Exception e) {
      LOG.warn("getBundle fail",e);
    }
    return messages;
  }

  public static void main(String[] args) {
    MsgServiceImpl msgService = new MsgServiceImpl();
    msgService.addMsg(new Msg() {
      @Override
      public String name() {
        return "xd2";
      }

      @Override
      public String getMessage() {
        return "纠删码";
      }
    });
    msgService.addMsg(new Msg() {
      @Override
      public String name() {
        return "test";
      }

      @Override
      public String getMessage() {
        return "模式:${${layout}}";
      }
    });
    N3Map map = new N3Map();
    map.put("layout","xd2");
    final String test = msgService.getMessage("test", map);
    System.out.println("test = " + test);

  }

}
