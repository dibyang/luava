package com.ls.luava.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 日志跟踪开关
 */
public class Tracer {
  final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final File traceFile;
  private Boolean trace;

  Tracer(Path path) {
    this.traceFile = path.toFile();
  }

  public boolean isTrace(){
    if(trace==null||!trace.equals(this.traceFile.exists())){
      trace = this.traceFile.exists();
      logger.info("trace {}={}", traceFile.getPath(), trace);
    }
    return trace;
  }

  public static Tracer c(String path){
    return c(Paths.get(path));
  }

  public static Tracer c(String path, String name){
    return c(Paths.get(path,name));
  }

  public static Tracer c(Path path){
    return new Tracer(path);
  }
}
