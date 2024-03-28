package com.ls.luava.utils;


import com.google.common.base.Preconditions;
import com.ls.luava.common.Types2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Function;

/**
 * @ClassName Reader4Config
 * @Description 文件自动读取器
 */
public class Reader4Config<T> {
  final Logger logger = LoggerFactory.getLogger(this.getClass());

  public static final Function<byte[], Optional<String>> READER4STRING = bytes -> {
    if(bytes!=null) {
      String val = new String(bytes);
      return Optional.ofNullable(val.trim());
    }
    return Optional.empty();
  };
  public static final Function<byte[], Optional<Integer>> READER4INTEGER = bytes -> {
    return READER4STRING.apply(bytes).map(s-> {
      return Integer.parseInt(s);
    });
  };

  public static final Function<byte[], Optional<Long>> READER4LONG = bytes -> {
    return READER4STRING.apply(bytes).map(s->Long.parseLong(s));
  };

  public static final Function<byte[], Optional<Double>> READER4DOUBLE = bytes -> {
    return READER4STRING.apply(bytes).map(s->Double.parseDouble(s));
  };

  public static final Function<byte[], Optional<Boolean>> READER4BOOLEAN = bytes -> {
    return READER4STRING.apply(bytes).map(s-> {
      return Types2.cast(s, Boolean.class).orElse(null);
    });
  };

  /**
   * 要看守的文件
   */
  private final File file;

  /**
   * 文件最后的修改时间
   */
  private long modified = -1;

  private T data;

  /**
   * 文件的读取器
   */
  private final Function<byte[],Optional<T>> reader;

  private Reader4Config(String path, Function<byte[], Optional<T>> reader) {
    Preconditions.checkNotNull(path);
    Preconditions.checkNotNull(reader);
    this.file = Paths.get(path).toFile();
    this.reader = reader;
  }

  /**
   * 获取配置数据
   * @param defValue 默认值
   * @return
   */
  public synchronized T getData(T defValue) {
    return getData(defValue, defValue);
  }

  /**
   * 获取配置数据
   * @param defValue 默认值
   * @param noSetting 未设置时的默认值
   * @return
   */
  public synchronized T getData(T defValue, T noSetting) {
    boolean readed = false;
    if (this.file.exists()) {
      if(modified!=this.file.lastModified()) {
        this.modified = this.file.lastModified();
        try {
          byte[] bytes = Files.readAllBytes(this.file.toPath());
          if (bytes != null) {
            this.data = this.reader.apply(bytes).orElse(noSetting);
          }
        } catch (Exception e) {
          //ignore e
          this.data = defValue;
        }
        readed = true;
      }
    }else{
      if(this.modified!=0) {
        this.data = noSetting;
        this.modified = 0;
        readed = true;
      }
    }
    if(readed){
      logger.info("read config: {}={}", this.file.getPath(), this.data);
    }
    return data;
  }

  public static <T> Reader4Config<T> of(String path, Function<byte[],Optional<T>> reader){
    return new Reader4Config(path, reader);
  }

  public static Reader4Config<String> of(String path){
    return of(path, READER4STRING);
  }

  public static Reader4Config<Integer> of4int(String path){
    return of(path, READER4INTEGER);
  }
  public static Reader4Config<Long> of4long(String path){
    return of(path, READER4LONG);
  }

  public static Reader4Config<Double> of4double(String path){
    return of(path, READER4DOUBLE);
  }

  public static Reader4Config<Boolean> of4boolean(String path){
    return of(path, READER4BOOLEAN);
  }

  public static <T> Reader4Config<T> of2(String path, Function<String,T> reader){
    return of(path, bytes -> {
      return READER4STRING.apply(bytes).map(s->reader.apply(s));
    });
  }

  public static <E extends Enum<E>> Reader4Config<E> of(String path, Class<E> clazz){
    return of2(path, s -> Enum.valueOf(clazz, s));
  }

}
