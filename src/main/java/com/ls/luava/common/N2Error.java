package com.ls.luava.common;

/**
 * @author yangzj
 * @date 2021/7/1
 */
public interface N2Error {
  String getCode();

  /**
   * 未来将会移除
   * @return
   */
  @Deprecated
  String getMessage();
}
