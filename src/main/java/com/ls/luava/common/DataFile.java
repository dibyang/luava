package com.ls.luava.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * @author yangzj
 * @date 2021/2/27
 */
public class DataFile {
  static final Logger LOG = LoggerFactory.getLogger(DataFile.class);
  private final File file;

  public DataFile(File file) {
    Objects.requireNonNull(file);
    this.file = file;
  }

  public File getFile() {
    return file;
  }

  public boolean delete(){
    if(file.exists()){
      Path bak = getBakPath();
      File bakFile = bak.toFile();
      file.delete();
      if(bakFile.exists()){
        bakFile.delete();
      }
      return true;
    }
    return false;
  }

  private Path getBakPath() {
    return file.toPath().resolveSibling(".bak." + file.getName());
  }

  public byte[] readAllBytes() throws IOException {
    Path path = file.toPath();
    Path bak = getBakPath();
    File bakFile = bak.toFile();
    if(!file.exists()||file.length()==0){
      if(bakFile.exists()){
        LOG.warn("DataFile read bak file: {} size: {}",bakFile.getPath(),bakFile.length());
        return Files.readAllBytes(bak);
      }
    }
    return Files.readAllBytes(path);
  }

  public void write(byte[] bytes) throws IOException {
    Path path = file.toPath();
    Path bak = null;
    if(file.exists()){
      bak = getBakPath();
      Files.copy(path, bak, StandardCopyOption.REPLACE_EXISTING);
    }else if(!file.getParentFile().exists()){
      file.getParentFile().mkdirs();
    }
    Files.write(path,bytes, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.WRITE,StandardOpenOption.SYNC);
    if(bak!=null&&bak.toFile().exists()){
      bak.toFile().delete();
    }
  }

  public static DataFile of(File file){
    return  new DataFile(file);
  }

}
