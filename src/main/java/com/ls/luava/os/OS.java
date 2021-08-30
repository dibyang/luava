package com.ls.luava.os;

import com.google.common.base.Strings;


/**
 * @author yangzj
 */
public enum OS {
  other(NullProxy.INSTANCE),
  linux(LinuxProxy.INSTANCE),
  windows(WindowsProxy.INSTANCE),
  mac(MacProxy.INSTANCE);
  public static final String LINUX = "Linux";
  public static final String WINDOWS = "Windows";
  public static final String MAC_OS = "Mac OS";

  private final OSProxy proxy;

  OS(OSProxy proxy) {
    this.proxy = proxy;
  }

  public OSProxy getOSProxy() {
    return proxy;
  }

  public static OS getOS(){
    String os_name = System.getProperty("os.name");
    return parse(os_name);
  }

  public static OS parse(String name) {
    OS os = OS.other;
    if (!Strings.isNullOrEmpty(name)) {
      if (name.startsWith(MAC_OS)) {
        os = OS.mac;
      } else if (name.startsWith(LINUX)) {
        os = OS.linux;
      } else if (name.startsWith(WINDOWS)) {
        os = OS.windows;
      } {
        os = OS.other;
      }
    }
    return os;
  }

}
