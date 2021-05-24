package com.ls.luava.config;

import com.ls.luava.common.DataFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class IniConfig extends IniEditor {
  final static Logger LOG = LoggerFactory.getLogger(IniConfig.class);
  final DataFile dataFile;
  private long lastModified = 0;

  public long getLastModified() {
    return lastModified;
  }

  public void setLastModified(long lastModified) {
    this.lastModified = lastModified;
  }

  public IniConfig(String path) {
    this.dataFile = DataFile.of(new File(path));
  }

  public boolean isModified() {
    return lastModified != dataFile.getFile().lastModified();
  }

  public void load() {
    try {
      this.clear();
      ByteArrayInputStream bis = new ByteArrayInputStream(dataFile.readAllBytes());
      this.load(bis);
      this.lastModified = dataFile.getFile().lastModified();
    } catch (IOException e) {
      LOG.error(null, e);
    }
  }

  public void store() {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      this.store(bos);
      dataFile.write(bos.toByteArray());
    } catch (IOException e) {
      LOG.error(null, e);
    }

  }

  public void clear() {
    for (String section : this.sectionNames()) {
      this.removeSection(section);
    }
  }
}
