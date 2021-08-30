package com.ls.luava.os;


/**
 * @author yzj
 */
public enum OSProxyFactory {
  factory;


  public OSProxy getOSProxy() {
    OS os = OS.getOS();
    return os.getOSProxy();
  }

}
