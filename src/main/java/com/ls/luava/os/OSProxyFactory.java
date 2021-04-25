package com.ls.luava.os;


public enum OSProxyFactory {
  factory;

  private static OS os = null;

  public OSProxy getOSProxy() {
    OS os = getOS();
    return os.getOSProxy();
  }

  public synchronized static OS getOS() {
    if (os == null) {
      String os_name = System.getProperty("os.name");
      if (os_name != null && os_name.startsWith("Linux")) {
        os = OS.linux;
      } else {
        os = OS.other;
      }
    }
    return os;
  }

}
