package com.ls.luava.os;

import com.google.common.base.Strings;


public enum OS {
  other(NullProxy.INSTANCE),
  linux(LinuxProxy.INSTANCE),
  windows(WindowsProxy.INSTANCE),
  mac(MacProxy.INSTANCE);
  private final OSProxy proxy;

  OS(OSProxy proxy) {
    this.proxy = proxy;
  }

  public OSProxy getOSProxy() {
    return proxy;
  }

  public static OS parse(String name) {
    OS os = OS.other;
    if (!Strings.isNullOrEmpty(name)) {
      name = name.toLowerCase();
      if (name.contains("mac")) {
        os = OS.mac;
      } else if (name.contains("linux")) {
        os = OS.linux;
      } else if (name.contains("windows")) {
        os = OS.windows;
      } else {
        os = OS.other;
      }
    }
    return os;
  }

}
