package com.ls.luava.common.io;


import com.ls.luava.common.TimeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Locale;


public class FS {
  final static Logger LOG = LoggerFactory.getLogger(FS.class);

  public static boolean canReadDirectory(Path path) {
    return Files.exists(path) && Files.isDirectory(path) && Files.isReadable(path);
  }

  public static boolean canReadFile(Path path) {
    return Files.exists(path) && Files.isRegularFile(path) && Files.isReadable(path);
  }

  public static boolean canWrite(Path path) {
    return Files.isWritable(path);
  }

  public static void close(Closeable c) {
    if (c == null) {
      return;
    }

    try {
      c.close();
    } catch (IOException ignore) {
      /* ignore */
    }
  }

  public static boolean createNewFile(Path path) throws IOException {
    Path ret = Files.createFile(path);
    return Files.exists(ret);
  }

  public static void ensureDirectoryExists(Path dir) throws IOException {
    if (exists(dir)) {
      // exists already, nothing to do
      return;
    }
    Files.createDirectories(dir);
  }

  public static void ensureDirectoryWritable(Path dir) throws IOException {
    if (!Files.exists(dir)) {
      throw new IOException("Path does not exist: " + dir.toAbsolutePath());
    }
    if (!Files.isDirectory(dir)) {
      throw new IOException("Directory does not exist: " + dir.toAbsolutePath());
    }
    if (!Files.isWritable(dir)) {
      throw new IOException("Unable to write to directory: " + dir.toAbsolutePath());
    }
  }

  public static boolean exists(Path path) {
    return Files.exists(path);
  }

  public static boolean isValidDirectory(Path path) {
    if (!Files.exists(path)) {
      // doesn't exist, not a valid directory
      return false;
    }

    if (!Files.isDirectory(path)) {
      // not a directory (as expected)
      LOG.warn("Not a directory: " + path);
      return false;
    }

    return true;
  }

  public static boolean isXml(String filename) {
    return filename.toLowerCase(Locale.ENGLISH).endsWith(".xml");
  }

  public static String toRelativePath(File baseDir, File path) {
    return baseDir.toURI().relativize(path.toURI()).toASCIIString();
  }

  public static boolean isPropertyFile(String filename) {
    return filename.toLowerCase(Locale.ENGLISH).endsWith(".properties");
  }

  public static String separators(String path) {
    StringBuilder ret = new StringBuilder();
    for (char c : path.toCharArray()) {
      if ((c == '/') || (c == '\\')) {
        ret.append(File.separatorChar);
      } else {
        ret.append(c);
      }
    }
    return ret.toString();
  }

  public static Path toPath(String path) {
    if (path == null)
      return null;
    return FileSystems.getDefault().getPath(FS.separators(path));
  }

  public static void touch(Path path) throws IOException {
    FileTime now = FileTime.fromMillis(TimeHelper.i.getTime());
    Files.setLastModifiedTime(path, now);
  }

  public static Path toRealPath(Path path) throws IOException {
    return path.toRealPath();
  }

  public static Path getPath(Class<?> cls) {
    // 检查用户传入的参数是否为空
    if (cls == null)
      throw new IllegalArgumentException("参数不能为空！");
    ClassLoader loader = cls.getClassLoader();
    // 获得类的全名，包括包名
    String clsName = cls.getName() + ".class";
    // 获得传入参数所在的包
    Package pack = cls.getPackage();
    String path = "";
    // 如果不是匿名包，将包名转化为路径
    if (pack != null) {
      String packName = pack.getName();
      // 在类的名称中，去掉包名的部分，获得类的文件名
      clsName = clsName.substring(packName.length() + 1);
      // 判定包名是否是简单包名，如果是，则直接将包名转换为路径，
      if (packName.indexOf(".") < 0)
        path = packName + "/";
      else {// 否则按照包名的组成部分，将包名转换为路径
        int start = 0, end = 0;
        end = packName.indexOf(".");
        while (end != -1) {
          path = path + packName.substring(start, end) + "/";
          start = end + 1;
          end = packName.indexOf(".", start);
        }
        path = path + packName.substring(start) + "/";
      }
    }

    // 调用ClassLoader的getResource方法，传入包含路径信息的类文件名
    java.net.URL url = loader.getResource(path + clsName);
    // 从URL对象中获取路径信息

    String realPath = url.getPath();
    // 去掉路径信息中的协议名"file:"
    int pos = realPath.indexOf("file:");
    if (pos > -1)
      realPath = realPath.substring(pos + 5);

    // 去掉路径信息最后包含类文件信息的部分，得到类所在的路径
    pos = realPath.indexOf(path + clsName);
    realPath = realPath.substring(0, pos - 1);

    // 如果类文件被打包到JAR等文件中时，去掉对应的JAR等打包文件名
    if (realPath.endsWith("!"))
      realPath = realPath.substring(0, realPath.lastIndexOf("/"));

    // 结果字符串可能因平台默认编码不同而不同。因此，改用 decode(String,String) 方法指定编码。
    try {
      realPath = java.net.URLDecoder.decode(realPath.substring(1), "utf-8");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    System.out.println(realPath);
    return toPath(realPath);
  }
  
  public static boolean delete(File file){
    if(file.exists()){
      if(file.isDirectory()){
        for(File f:file.listFiles()){
          delete(f);
        }
      }
      return file.delete();
    }
    return false;
  }
}
