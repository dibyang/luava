package com.ls.luava.utils;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class FileDebugSupport extends BaseDebugSupport {
  static final Logger LOG = LoggerFactory.getLogger(DebugHelper.class);
  public static final Splitter SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();

  private final File file;
  private volatile long lastModified;

  public FileDebugSupport(File file) {
    this.file = file;
  }

  @Override
  protected synchronized void reload() {
    if(file.exists()&&file.lastModified()!=lastModified){
      lastModified = file.lastModified();

      Properties properties = new Properties();
      try(FileInputStream in = new FileInputStream(file)) {
        properties.load(in);
      } catch (Exception e) {
        LOG.warn("",e);
      }
      this.clear();
      for (String key : properties.stringPropertyNames()) {
        String val = properties.getProperty(key);
        List<String> values = SPLITTER.splitToList(Strings.nullToEmpty(val));
        this.addValue(key, values);
      }
    }
  }



}
