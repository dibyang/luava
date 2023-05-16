package com.ls.luava.os;

@FunctionalInterface
public interface PreExecute {
  void preExecute(ProcessBuilder processBuilder);
}
