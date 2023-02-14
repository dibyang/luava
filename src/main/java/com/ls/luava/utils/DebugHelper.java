package com.ls.luava.utils;

import com.ls.luava.spi.DebugSupport;
import java.util.*;

public enum DebugHelper {
  helper;
  DebugSupport debugSupport = null;

  public boolean isDebug(String name){
    return getDebugSupport().isDebug(name);
  }

  private synchronized DebugSupport getDebugSupport() {
    if(debugSupport==null) {
      ServiceLoader<DebugSupport> load = ServiceLoader.load(DebugSupport.class);
      for (DebugSupport support : load) {
        debugSupport = support;
        break;
      }
    }
    if(debugSupport==null){
      debugSupport = new NoneDebugSupport();
    }
    return debugSupport;
  }

  public boolean isDebug(String name,String value){
    return getDebugSupport().isDebug(name,value);
  }


}
