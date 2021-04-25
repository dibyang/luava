package com.ls.luava.common.io;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

public class Path2 {
  public static final String LINUX_SEPARATOR = "/";
  public static final String WINDOWS_SEPARATOR = "\\";
  String separator = LINUX_SEPARATOR;
  final List<String> nodes = Lists.newLinkedList();

  public String getSeparator() {
    return separator;
  }

  public Path2 setSeparator(String separator) {
    this.separator = separator;
    return this;
  }

  public List<String> getNodes() {
    return nodes;
  }

  public Path2 append(String path) {
    Iterator<String> iterator = Splitter.onPattern("[\\\\|/]").split(path).iterator();
    while (iterator.hasNext()) {
      String s = iterator.next();
      if (nodes.isEmpty()) {
        if (s != null) {
          nodes.add(s);
        }
      } else {
        if (!Strings.isNullOrEmpty(s)) {
          nodes.add(s);
        }
      }
    }

    return this;
  }

  public String toString() {
    return Joiner.on(separator).join(nodes);
  }

  public static Path2 get(String first, String... more) {
    return of(first, more);
  }

  public static Path2 of(String first, String... more) {
    Path2 path = new Path2().append(first);
    for (String s : more) {
      path.append(s);
    }
    return path;
  }

  public static void main(String[] args) {
    System.out.println(Path2.of("/datapool","\\"));
  }

}
