package com.ls.luava.utils;

import com.ls.luava.spi.DebugSupport;

public class NoneDebugSupport implements DebugSupport {
  @Override
  public boolean isDebug(String name) {
    return false;
  }

  @Override
  public boolean isDebug(String name, String value) {
    return false;
  }
}
