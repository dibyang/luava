package com.ls.luava.os;


public enum OSProxyFactory {
  factory;


  public OSProxy getOSProxy() {
    OS os = getOS();
    return os.getOSProxy();
  }

  public synchronized static OS getOS() {
    String os_name = System.getProperty("os.name");
    return OS.parse(os_name);
  }

}
