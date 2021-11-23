package com.ls.luava.os;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MacProxy extends LinuxProxy {
  final static Logger LOG = LoggerFactory.getLogger(MacProxy.class);
  public final static MacProxy INSTANCE = new MacProxy();

  public MacProxy() {
  }


  @Override
  public OS getOS() {
    return OS.mac;
  }

}
