package com.ls.luava.common;


import java.util.Optional;
import java.util.function.Function;

/**
 * Finder
 *
 * @author yangzj
 * @version 1.0
 */
public class Finder {
  public static final Finder NULL = c(null);
  private final String value;
  private volatile Finder parent = null;
  private volatile boolean last = false;

  public Finder(String value,boolean last) {
    this.value = value;
    this.last = last;
  }

  public boolean isLast() {
    return last;
  }

  public Finder last(){
    last = true;
    return this;
  }

  public Finder before(){
    last = false;
    return this;
  }

  public Finder setParent(Finder parent) {
    this.parent = parent;
    return this;
  }

  public Finder nullToParent() {
    if(isNull()){
      return parent;
    }
    return this;
  }

  public boolean isNull(){
    return value==null;
  }

  public String getValue() {
    return value;
  }

  public Finder find(String beginMark, IndexFunction end){
    return find(IndexBeginMark.c(beginMark),end);
  }

  public Finder find(IndexFunction begin, String endMark){
    return find(begin, IndexEndMark.c(endMark));
  }

  public Finder find(String beginMark, String endMark){
    return find(IndexBeginMark.c(beginMark), IndexEndMark.c(endMark));
  }

  public Finder find(IndexFunction begin, IndexFunction end){
    if(value!=null) {
      int beginIndex = begin.index(last, value, 0);
      int endIndex = end.index(last, value, beginIndex);
      if (beginIndex >= 0 && endIndex >= beginIndex) {
        return c(value.substring(beginIndex, endIndex)).setParent(this);
      }
    }
    return NULL;
  }

  public Finder head(String endMark, int skip){
    return head(IndexEndMark.c(endMark,skip));
  }

  public Finder head(String endMark){
    return head(IndexEndMark.c(endMark));
  }

  public Finder head(IndexFunction end){
    return find(IndexBeginMark::head,end);
  }

  public Finder tail(String beginMark, int skip){
    return tail(IndexBeginMark.c(beginMark,skip));
  }

  public Finder tail(String beginMark){
    return tail(IndexBeginMark.c(beginMark));
  }

  public Finder tail(IndexFunction begin){
    return find(begin, IndexEndMark::tail);
  }

  public <T> Optional<T> getNullableValue(Class<T> clazz)
  {
    return Optional.ofNullable(getValue(clazz));
  }

  public Optional<String> getNullableValue()
  {
    return Optional.ofNullable(getValue());
  }

  public <T> T getValue(Class<T> clazz)
  {
    return Types.cast(value, clazz);
  }

  public static  Finder c(String value){
    return c(value,false);
  }

  public static  Finder c(String value, boolean last){
    return new Finder(value,last);
  }

  public static void main(String[] args) {
    String s="user2:$6$/5C7Cg06$sMjvN8/dPSCgdng32oeJ8TqsQmkgxGeqA7qHX3Eurw0EN6lam7GSVcP8M9TM0/t80WiV9jDDRHltpuEY.CfMM/:18522:0:99999:7:::";
    Finder finder = Finder.c(s);
    String username = finder.head(":").getValue();
    System.out.println("username = " + username);
    String pwd = finder.tail(":").head(":").getValue();
    System.out.println("pwd = " + pwd);
    String pwd2 = finder.head(":",1).tail(":").getValue();
    System.out.println("pwd2 = " + pwd2);
    String s2="tuser:x:10001:10004::/home/tuser:/bin/bash";
    Finder finder2 = Finder.c(s2);
    String uid = finder2.tail(":", 1).head(":").getValue();
    String gid = finder2.tail(":", 2).head(":").getValue();
    System.out.println("uid = " + uid);
    System.out.println("gid = " + gid);
    finder2.last();
    String s21 = finder2.head(":").getValue();
    String s22 = finder2.head(":",2).getValue();
    String s23 = finder2.tail(":").getValue();
    String s24 = finder2.tail(":",2).getValue();
    System.out.println("s21 = " + s21);
    System.out.println("s22 = " + s22);
    System.out.println("s23 = " + s23);
    System.out.println("s24 = " + s24);
    Finder finder3 = Finder.c("#IPADDR=");
    System.out.println(finder3.head("=").getValue());
    String route1 = "default via 13.13.13.253 dev br0 proto static";
    String route2 = "default via 13.13.13.253 dev br0 ";
    String dev1 = Finder.c(route1).tail("dev ").head(" ").nullToParent().getValue().trim();
    String dev2 = Finder.c(route2).tail("dev ").head(" ").nullToParent().getValue().trim();
    System.out.println("dev1 =" + dev1+ " dev2 =" + dev2);
  }
}
