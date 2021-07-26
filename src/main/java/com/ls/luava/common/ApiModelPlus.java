package com.ls.luava.common;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 目的为了支持把method里面数据传递到日志中和支持自定义数据
 * 是通过aop切面获取数据，然后清理数据
 *
 * @ClassName ApiModelPro
 * @Description todo
 * @Author 我是郑同学呀
 * @Date 2021/7/26 10:13
 */
public class ApiModelPlus extends ApiModel {
  public static final String MESSAGE = "log";

  public Map<String, Object> initLog() {
    Object log = this.get(MESSAGE);
    Map<String, Object> message = null;
    if (log == null) {
      message = Maps.newHashMap();
      this.put(MESSAGE, message);
    } else {
      if (log instanceof Map<?, ?>) {
        message = ((Map<String, Object>) log);
      } else {
        message = Maps.newHashMap();
      }
    }
    return message;
  }

  public void destroyLog() {
    this.remove(MESSAGE);
  }

}
