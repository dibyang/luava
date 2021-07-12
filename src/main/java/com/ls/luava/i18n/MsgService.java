package com.ls.luava.i18n;

import java.util.Locale;
import java.util.Map;

/**
 * @author yangzj
 * @date 2021/7/7
 */
public interface MsgService {
  Msg getMsg(String name);
  void addMsg(Msg msg);
  void addMsgs(Class<? extends Msg> clazz);
  String getMessage(String name, Map<String,Object> data);
  String getMessage(String name, Map<String,Object> data, Locale locale);
}
