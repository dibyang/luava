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
      Path bak = getTmpPath();
      File bakFile = bak.toFile();
      file.delete();
      if(bakFile.exists()){
        bakFile.delete();
      }
      return true;
    }
    return false;
  }

  private Path getTmpPath() {
    return file.toPath().resolveSibling(".tmp." + file.getName());
  }

  public byte[] readAllBytes() throws IOException {
    Path path = file.toPath();
    if(file.exists()) {
      return Files.readAllBytes(path);
    }
    return null;
  }

  public void write(byte[] bytes) throws IOException {
    Path path = file.toPath();
    if(!file.getParentFile().exists()){
      file.getParentFile().mkdirs();
    }
    Path tmp = getTmpPath();
    if(tmp.toFile().exists()){
      tmp.toFile().delete();
    }
    Files.write(tmp,bytes, StandardOpenOption.CREATE,StandardOpenOption.WRITE,StandardOpenOption.SYNC);
    Files.move(tmp,path, StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.ATOMIC_MOVE);
  }

  public static DataFile of(File file){
    return  new DataFile(file);
  }

}
