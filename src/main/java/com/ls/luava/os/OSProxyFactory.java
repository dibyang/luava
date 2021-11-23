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

  public static void main(String[] args) {
    final boolean process = OSProxyFactory.factory.getOSProxy().existProcess("20872");
    System.out.println("process = " + process);
  }
}
