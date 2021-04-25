package com.ls.luava.common;

import com.google.common.base.Strings;

/**
 * CfgLine
 *
 * @author yangzj
 * @version 1.0
 */
public class CfgLine {
  public static final String COMMENT_FLAG = "#";
  public static final String PREFIX = "\"";
  private String line;

  public String getLine() {
    return Strings.nullToEmpty(line);
  }

  public void setLine(String line) {
    this.line = Strings.nullToEmpty(line);
  }

  public boolean isComment(){
    return this.getLine().startsWith(COMMENT_FLAG);
  }
  public void comment(){
    if(!isComment()){
      line = COMMENT_FLAG +line;
    }
  }

  public void unComment(){
    while(isComment()){
      line = line.substring(1);
    }
  }

  public void setKey(String key){
    if(!isComment()){
      line = key +"="+ getValue();
    }
  }

  public String getKey() {
    String line = this.getLine();
    if(isComment()) {
      line = line.substring(1);
    }
    return Strings.nullToEmpty(Finder.c(line).head("=").getValue());
  }

  public void setValue(String value){
    if(!isComment()){
      line = getKey() +"="+ value;
    }
  }

  public String getValue() {
    if(!isComment()) {
      String s = this.getLine();
      return trim(Strings.nullToEmpty(Finder.c(s).tail("=").getValue()));
    }
    return "";
  }


  private String trim(String s) {
    if(s!=null) {
      s = s.trim();
      if (s.startsWith(PREFIX)) {
        s = s.substring(1);
      }
      if (s.endsWith(PREFIX)) {
        s = s.substring(0, s.length() - 1);
      }
    }
    return s;
  }

  public static  CfgLine of(String key,String value){
    CfgLine cfgLine = new CfgLine();
    cfgLine.setLine(key+"="+value);
    return cfgLine;
  }

  public static  CfgLine of(String line){
    CfgLine cfgLine = new CfgLine();
    cfgLine.setLine(line);
    return cfgLine;
  }


  public static void main(String[] args) {
    CfgLine cfgLine = CfgLine.of("BOOTPROTO=static");
    cfgLine.setValue("dhcp");
    System.out.println("cfgLine.getLine() = " + cfgLine.getLine());
  }

}
