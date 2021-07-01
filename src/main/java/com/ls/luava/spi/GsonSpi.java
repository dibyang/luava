package com.ls.luava.spi;

import com.google.gson.GsonBuilder;

public interface GsonSpi {
  /**
   * build Gson 前调用
   * @param builder
   */
  void build(GsonBuilder builder);
}
