package com.ls.luava.jvm;


import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Iterator;
import java.util.List;

public enum JVMHelper {
  helper;
  RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

  private JVMHelper() {

  }

  public String getBootClassPath() {
    return runtimeMXBean.getBootClassPath();
  }

  public String getClassPath() {
    return runtimeMXBean.getClassPath();
  }

  public String getLibraryPath() {
    return runtimeMXBean.getLibraryPath();
  }

  public List<String> getInputArguments() {
    return runtimeMXBean.getInputArguments();
  }

  public Long getStartTime() {
    return runtimeMXBean.getStartTime();
  }

  public Long getUptime() {
    return runtimeMXBean.getUptime();
  }

  public boolean isDebug() {
    Iterator<String> iterator = getInputArguments().iterator();
    while (iterator.hasNext()) {
      String next = iterator.next();
      if (next.contains("jdwp:") || next.contains("jdwp=")) {
        return true;
      }
    }

    return false;
  }

  public int getProcessId() {
    int pid = 0;
    String name = runtimeMXBean.getName();
    int index = name.indexOf("@");
    if (index > 0) {
      pid = Integer.parseInt(name.substring(0, index));
    }
    return pid;
  }

}
