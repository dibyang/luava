package com.ls.luava.common;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangzj
 * @date 2021/7/1
 */
public class TextReader {
  static Logger LOG = LoggerFactory.getLogger(TextReader.class);
  private final File file;
  private Charset charset;
  private Splitter splitter = Splitter.on("\n").trimResults();
  private Joiner joiner = joiner = Joiner.on("\n");;

  public TextReader(File file, Charset charset){
    Preconditions.checkNotNull(file);
    this.file = file;
    this.charset =charset;
  }

  public TextReader(File file, String charsetName){
    this(file,Charset.forName(charsetName));
  }

  public TextReader(File file){
    this(file, Charset.defaultCharset());
  }

  public TextReader(String file, Charset charset){
    this(new File(file), charset);
  }

  public TextReader(String file, String charsetName){
    this(new File(file), charsetName);
  }

  public TextReader(String file){
    this(file,Charset.defaultCharset());
  }

  public boolean exists(){
    return file.exists();
  }

  public String tail(int n) {
    StringBuilder result = new StringBuilder();
    try(RandomAccessFile accessFile = new RandomAccessFile(file, "r")){
      long total = accessFile.length();
      int readSize = 1024;
      int curN = 0;
      long pos = total - 1;
      while (curN < n && pos > 0) {
        byte[] bytes = new byte[readSize];
        pos = pos - readSize;
        pos = pos < 0 ? 0 : pos;
        accessFile.seek(pos);
        accessFile.read(bytes, 0, readSize);

        String lines = new String(bytes, charset);
        List<String> split = splitter.splitToList(lines);
        if (!lines.endsWith("\n") && curN > 0) {
          curN--;
        }
        int accept = n - curN;
        int size = split.size();
        if (accept < size) {
          for (int i = size - 1; i >= size - accept; i--) {
            if (i < size - 1) {
              result.insert(0, "\n");
            }
            result.insert(0, split.get(i));
          }
          curN = n;
        } else {
          curN += split.size();
          result.insert(0, lines);
        }
      }
    } catch (IOException e){
      LOG.warn("tail fail", e);
    }
    return result.toString();
  }

  public List<String> tail2(int n){
    return splitter.splitToList(tail(n));
  }

  public String head(int n) {
    List<String> lines = head2(n);
    return joiner.join(lines);
  }

  private List<String> head2(int n) {
    List<String> lines = Lists.newArrayList();
    try(FileReader fileReader = new FileReader(file)) {
      BufferedReader reader = new BufferedReader(fileReader);
      lines.addAll(reader.lines().limit(n).collect(Collectors.toList()));
    } catch (IOException e){
      LOG.warn("head2 fail", e);
    }
    return lines;
  }

}
