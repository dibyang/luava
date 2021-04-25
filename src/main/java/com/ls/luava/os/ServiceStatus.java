package com.ls.luava.os;


public class ServiceStatus {
  private boolean exists = false;
  private int status = 0;
  private String out;


  public boolean isExists() {
    return exists;
  }

  public void setExists(boolean exists) {
    this.exists = exists;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getOut() {
    return out;
  }

  public void setOut(String out) {
    this.out = out;
  }


}
