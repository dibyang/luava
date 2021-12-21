package com.ls.luava.config;

import com.ls.luava.common.DataFile;
import com.ls.luava.common.N3Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author yangzj
 */
public class PropertiesConfig {
  final static Logger LOG = LoggerFactory.getLogger(PropertiesConfig.class);
  final DataFile dataFile;
  private long lastModified = 0;
  private final SafeProperties properties = new SafeProperties();
  private final N3Map map = new N3Map();

  public long getLastModified() {
    return lastModified;
  }

  public void setLastModified(long lastModified) {
    this.lastModified = lastModified;
  }

  public PropertiesConfig(String path) {
    this.dataFile = DataFile.of(new File(path));
    loadIfModified();
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
        properties.load(bis);
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
          map.put(entry.getKey().toString(),entry.getValue());
        }
        this.lastModified = dataFile.getFile().lastModified();
      }
    } catch (IOException e) {
      LOG.error(null, e);
    }
  }

  public void clear() {
    properties.clear();
    map.clear();
  }

  public <T> Optional<T> getValue(Class<T> clazz, String... keys){
    return map.getValue(clazz, keys);
  }

  public <T> List<T> getValues(Class<T> clazz, String... keys){
    return map.getValues(clazz, keys);
  }

  public List<String> getStrings(String... keys){
    return map.getValues(String.class, keys);
  }

  public Optional<String> getString(String... keys){
    return getValue(String.class,keys);
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
    map.put(key, value);
  }

  public Object remove(String key){
    return map.remove(key);
  }

  public void store() {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      properties.putAll(map);
      properties.store(bos,null);
      dataFile.write(bos.toByteArray());
    } catch (IOException e) {
      LOG.error(null, e);
    }
  }

  public static void main(String[] args) {
    PropertiesConfig config = new PropertiesConfig("/etc/client.conf");
    config.setValue("client.user","client1");
    config.setValue("client.pwd","123456");
    config.setValue("client.port",6688);
    config.store();
    System.out.println("config.map = " + config.map);
  }

}
