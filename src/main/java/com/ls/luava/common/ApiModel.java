package com.ls.luava.common;

import com.google.common.base.Strings;

import java.util.List;
import java.util.Map;

public class ApiModel extends N3Map {

  public static final long serialVersionUID = 1L;
  public static final String ERROR = "error";
  public static final String ERROR_MESSAGE = "error_message";
  public static final String DATA = "data";


  public ApiModel() {
  }

  public String getError() {
    return (String) this.get(ERROR);
  }

  public void setErr(Throwable t) {
    if (t != null) {
      if (t instanceof N2Exception) {
        this.put(ERROR, ((N2Exception) t).getError().getCode());
      } if (t instanceof N2Error) {
        this.put(ERROR, ((N2Error) t).getCode());
      } else {
        this.put(ERROR, t.getClass().getSimpleName());
      }
      this.put(ERROR_MESSAGE, t.getMessage());
    } else {
      cleanError();
    }
  }

  public void setError(Throwable t) {
    this.setErr(t);
  }

  public void setError(N2Error err) {
    this.setErr(err.getCode(), err.getMessage());
  }

  public void setErr(String code) {
    this.put(ERROR, code);
  }

  public void setErr(String code, String message) {
    this.put(ERROR, code);
    this.put(ERROR_MESSAGE, message);
  }

  public String getErrorMessage() {
    return (String) this.get(ERROR_MESSAGE);
  }

  public void cleanError() {
    this.remove(ERROR);
    this.remove(ERROR_MESSAGE);
  }

  public boolean isSuccess() {
    return Strings.isNullOrEmpty(this.getError());
  }

  public void setData(Object value) {
    this.put(DATA, value);
  }

  public Object getData() {
    return this.get(DATA);
  }

  public <T> List<T> getDatas(Class<T> tClass) {
    return getValues(tClass, (NameMapping) null, DATA);
  }

  public <T> T getData(Class<T> tClass) {
    return getValue(tClass, DATA).orElse(null);
  }

  public static ApiModel c() {
    return new ApiModel();
  }

  public static ApiModel of(Map<String, Object> map) {
    ApiModel model = new ApiModel();
    model.putAll(map);
    return model;
  }
}
