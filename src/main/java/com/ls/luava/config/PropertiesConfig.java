package com.ls.luava.config;

import com.ls.luava.common.DataFile;
import com.ls.luava.common.N3Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * @author yangzj
 */
public class PropertiesConfig {
  final static Logger LOG = LoggerFactory.getLogger(PropertiesConfig.class);
  final DataFile dataFile;
  private long lastModified = 0;
  private final N3Map config = new N3Map();

  public long getLastModified() {
    return lastModified;
  }

  public void setLastModified(long lastModified) {
    this.lastModified = lastModified;
  }

  public PropertiesConfig(String path) {
    this.dataFile = DataFile.of(new File(path));

  }

  public boolean isModified() {
    return lastModified != dataFile.getFile().lastModified();
  }

  public void loadIfModified() {
    if(isModified()) {
      load();
    }
  }

  public void load() {
    try {
      if(dataFile.getFile().exists()) {
        clear();
        ByteArrayInputStream bis = new ByteArrayInputStream(dataFile.readAllBytes());
        Properties properties = new Properties();
        properties.load(bis);
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
          config.put(entry.getKey().toString(),entry.getValue());
        }
        this.lastModified = dataFile.getFile().lastModified();
      }
    } catch (IOException e) {
      LOG.error(null, e);
    }
  }

  public void clear() {
    config.clear();
  }

  public <T> Optional<T> getValue(Class<T> clazz, String... keys){
    return config.getValue(clazz, keys);
  }

  public <T> List<T> getValues(Class<T> clazz, String... keys){
    return config.getValues(clazz, keys);
  }

  public Optional<Integer> getInt(String... keys){
    return getValue(Integer.class,keys);
  }

  public Optional<Long> getLong(String... keys){
    return getValue(Long.class,keys);
  }

  public Optional<Double> getDouble(String... keys){
    return getValue(Double.class,keys);
  }

  public Optional<Boolean> getBoolean(String... keys){
    return getValue(Boolean.class,keys);
  }

  public void setValue(String key, Object value){
    config.put(key, value);
  }

  public Object remove(String key){
    return config.remove(key);
  }

  public void store() {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      Properties properties = new Properties();
      properties.putAll(config);
      properties.store(bos,"");
      dataFile.write(bos.toByteArray());
    } catch (IOException e) {
      LOG.error(null, e);
    }
  }

}
