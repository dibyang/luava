package com.ls.luava.common;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CfgFile
 *
 * @author yangzj
 * @version 1.0
 */
public class CfgFile {
  static final Logger LOG = LoggerFactory.getLogger(CfgFile.class);

  private final List<CfgLine> lines = Lists.newArrayList();

  public List<CfgLine> getLines() {
    return lines;
  }

  public List<String> getKeys(){
    List<String> keys = Lists.newArrayList();
    lines.forEach(c->{
      if(!c.isComment()){
        keys.add(c.getKey());
      }
    });
    return keys;
  }

  public void remove(String key){
    lines.removeIf(c->!c.isComment() && c.getKey().equalsIgnoreCase(key));
  }

  public Optional<String> getValue(String key){
    Optional<CfgLine> first = lines.stream()
        .filter(c -> !c.isComment() && c.getKey().equalsIgnoreCase(key))
        .findFirst();
    return first.map(c->c.getValue());
  }

  public void setValue(String key,String value){
    Optional<CfgLine> first = lines.stream()
        .filter(c -> c.getKey().equalsIgnoreCase(key))
        .findFirst();
    if(first.isPresent()) {
      first.ifPresent(c -> {
        if(c.isComment()){
          c.unComment();
        }
        c.setValue(value);
      });
    }else {
      lines.add(CfgLine.of(key,value));
    }
  }

  public void load(File file){
    if(file.exists()){
      try {
        List<String> ss = Files.readAllLines(file.toPath());
        lines.clear();
        for (String s : ss) {
          lines.add(CfgLine.of(s));
        }
      } catch (IOException e) {
        LOG.warn(null,e);
      }
    }
  }

  public void save(File file){
    try {
      List<String> lines = this.lines.stream().map(c -> c.getLine()).collect(Collectors.toList());
      Files.write(file.toPath(),lines);
    } catch (IOException e) {
      LOG.warn(null,e);
    }
  }

  public static void main(String[] args) {
    CfgFile ifcfg = new CfgFile();
    File file = new File("d:/ifcfg-eno1");
    ifcfg.load(file);
    Optional<String> bootproto = ifcfg.getValue("BOOTPROTO");
    System.out.println("bootproto = " + bootproto);
    ifcfg.setValue("BOOTPROTO","dhcp");
    ifcfg.save(file);
  }
}
