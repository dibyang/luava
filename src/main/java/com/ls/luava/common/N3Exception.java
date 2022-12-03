package com.ls.luava.common;

import com.google.common.base.Joiner;

public class N3Exception extends Exception implements N2Error{
  private final N2Error error;

  private Object[] data;

  public N2Error getError() {
    return error;
  }

  public String getCode() {
    return error.getCode();
  }

  public N3Exception(N2Error err, Object... args) {
    this(err, null, args);
  }

  public N3Exception(N2Error err, Throwable cause, Object... args) {
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

  public static N3Exception c(N2Error err, Throwable cause, Object... args) {
    return new N3Exception(err, cause, args);
  }

  public static N3Exception c(N2Error err, Object... args) {
    return new N3Exception(err, args);
  }

}
