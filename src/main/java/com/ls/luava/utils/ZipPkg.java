package com.ls.luava.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ZipPkg
 *
 * @author yangzj
 * @since 0.3.0
 */
public class ZipPkg implements Closeable {
  private final ZipOutputStream zos;
  private final ByteArrayOutputStream bos;

  public ZipPkg() {
    this.bos = new ByteArrayOutputStream();
    this.zos = new ZipOutputStream(bos);
  }

  public void finish(){
    try {
      zos.finish();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public byte[] getBytes() throws IOException {
    finish();
    return bos.toByteArray();
  }

  public void addFile(String path,byte[] data) throws IOException {
    ZipEntry entry = new ZipEntry(path);// zip压缩对象
    zos.putNextEntry(entry);
    if(data!=null&&data.length>0){
      zos.write(data);
    }
    zos.closeEntry();
  }

  @Override
  public void close() throws IOException {
    zos.close();
    bos.close();
  }
}
