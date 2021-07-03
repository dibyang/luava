package com.ls.luava.common;

import com.google.common.base.Joiner;

/**
 * @ClassName N2Exception
 * @Description TODO
 * @Author 我是郑同学呀
 * @Date 2021/7/3 13:55
 */
public class N2Exception extends RuntimeException {
  private final N2Error error;

  private Object[] data;

  public N2Error getError() {
    return error;
  }

  public String getCode() {
    return error.getCode();
  }

  public N2Exception(N2Error err, Object... args) {
    this(err, null, args);
  }

  public N2Exception(N2Error err, Throwable cause, Object... args) {
    super(Joiner.on(",").join(args), cause);
    this.error = err;
  }

  public Object[] getData() {
    return data;
  }

  public void setData(Object... data) {
    this.data = data;
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }

  public static N2Exception c(N2Error err, Throwable cause, Object... args) {
    return new N2Exception(err, cause, args);
  }

  public static N2Exception c(N2Error err, Object... args) {
    return new N2Exception(err, args);
  }

}
