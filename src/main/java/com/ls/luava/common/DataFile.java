package com.ls.luava.common;

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
      Path bak = file.toPath().resolveSibling(".bak." + file.getName());
      File bakFile = bak.toFile();
      file.delete();
      if(bakFile.exists()){
        bakFile.delete();
      }
      return true;
    }
    return false;
  }

  public byte[] readAllBytes() throws IOException {
    Path path = file.toPath();
    Path bak = path.resolveSibling(".bak." + file.getName());
    File bakFile = bak.toFile();
    if(file.exists()&&file.length()==0){
      if(bakFile.exists()){
        Files.copy(bak,path, StandardCopyOption.REPLACE_EXISTING);
      }
    }
    return Files.readAllBytes(path);
  }

  public void write(byte[] bytes) throws IOException {
    Path path = file.toPath();
    Path bak = null;
    if(file.exists()){
      bak = path.resolveSibling(".bak." + path.toFile().getName());
      Files.copy(path, bak, StandardCopyOption.REPLACE_EXISTING);
    }
    if(!file.getParentFile().exists()){
      file.getParentFile().mkdirs();
    }
    Files.write(path,bytes, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.WRITE,StandardOpenOption.SYNC);
    if(bak!=null){
      File bakFile = bak.toFile();
      if(bakFile.exists()){
        bakFile.delete();
      }
    }
  }

  public static DataFile of(File file){
    return  new DataFile(file);
  }

}
