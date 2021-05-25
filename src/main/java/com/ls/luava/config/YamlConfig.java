package com.ls.luava.config;

import com.ls.luava.common.DataFile;
import com.ls.luava.common.N3Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.representer.Representer2;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author yangzj
 */
public class YamlConfig  {
  final static Logger LOG = LoggerFactory.getLogger(YamlConfig.class);
  final DataFile dataFile;
  private long lastModified = 0;
  private final Yaml yaml;
  private final N3Map map = new N3Map();

  public long getLastModified() {
    return lastModified;
  }

  public void setLastModified(long lastModified) {
    this.lastModified = lastModified;
  }

  public YamlConfig(String path) {
    this.dataFile = DataFile.of(new File(path));
    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setAllowReadOnlyProperties(true);
    Representer2 representer = new Representer2(dumperOptions);
    representer.getPropertyUtils().setSkipMissingProperties(true);

    yaml = new Yaml(new Constructor(),representer,dumperOptions);
    yaml.setBeanAccess(BeanAccess.FIELD);
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
        String s = new String(dataFile.readAllBytes(), StandardCharsets.UTF_8);
        clear();
        Map map = yaml.load(s);
        this.map.putAll(map);
        this.lastModified = dataFile.getFile().lastModified();
      }
    } catch (IOException e) {
      LOG.error(null, e);
    }
  }

  public void clear() {
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
      String s = yaml.dump(map);
      dataFile.write(s.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      LOG.error(null, e);
    }
  }

}
