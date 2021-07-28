package com.ls.luava.common;

/**
 * @author yangzj
 * @date 2021/7/28
 */
public class User {
  private String name;
  private Double p;

  public User(String name, Double p) {
    this.name = name;
    this.p = p;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getP() {
    return p;
  }

  public void setP(Double p) {
    this.p = p;
  }

  @Override
  public String toString() {
    return "User{" +
      "name='" + name + '\'' +
      ", p=" + p +
      '}';
  }
}
