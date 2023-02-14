package com.ls.luava.utils;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ls.luava.spi.DebugSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class FileDebugSupport implements DebugSupport {
  static final Logger LOG = LoggerFactory.getLogger(DebugHelper.class);
  public static final Splitter SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();

  private final File file;
  private volatile long lastModified;
  private final Map<String, Set<String>> map = Maps.newConcurrentMap();

  public FileDebugSupport(File file) {
    this.file = file;
  }

  public boolean isDebug(String name){
    reload();
    Boolean value = map.containsKey(name);
    return value;
  }
  public boolean isDebug(String name,String value){
    reload();
    Set<String> values = map.get(name);
    return values!=null&&values.contains(value);
  }

  private synchronized void reload() {
    if(file.exists()&&file.lastModified()!=lastModified){
      lastModified = file.lastModified();

      Properties properties = new Properties();
      try(FileInputStream in = new FileInputStream(file)) {
        properties.load(in);
      } catch (Exception e) {
        LOG.warn("",e);
      }
      this.map.clear();
      for (String key : properties.stringPropertyNames()) {
        String val = properties.getProperty(key);
        List<String> values = SPLITTER.splitToList(Strings.nullToEmpty(val));
        map.put(key, Sets.newHashSet(values));
      }
    }
  }

}
