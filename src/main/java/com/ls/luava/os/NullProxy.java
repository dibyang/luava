package com.ls.luava.os;

import java.io.IOException;

public class NullProxy implements OSProxy {
  public final static NullProxy INSTANCE = new NullProxy();

  @Override
  public OS getOS() {
    return OS.other;
  }

  @Override
  public Process process(String cmd, Object... args) {
    return null;
  }

  @Override
  public Process process(CmdBuilder cmdBuilde) {
    return null;
  }

  @Override
  public Process process(CmdBuilder cmdBuilde, PreExecute preExecute) {
    return null;
  }

  @Override
  public CmdResult exec(String cmd, Object... args) {
    return CmdResult.Null;
  }

  @Override
  public CmdResult exec(CmdBuilder cmdBuilde) {
    return CmdResult.Null;
  }

  @Override
  public CmdResult exec(CmdBuilder cmdBuilde, PreExecute preExecute) {
    return CmdResult.Null;
  }

  @Override
  public ProcessBuilder processBuilder(CmdBuilder cmdBuilde) throws IOException {
    return null;
  }

  @Override
  public CmdResult execShell(String cmd) {
    return CmdResult.Null;
  }

  @Override
  public CmdResult execShell(String cmd, Object... args) {
    return CmdResult.Null;
  }

  @Override
  public CmdResult stopService(String service) {
    return CmdResult.Null;
  }

  @Override
  public CmdResult startService(String service) {
    return CmdResult.Null;
  }

  @Override
  public CmdResult restartService(String service) {
    return CmdResult.Null;
  }

  @Override
  public CmdResult kill(String pid) {
    return null;
  }

  @Override
  public ServiceStatus statusService(String service) {
    return null;
  }


}
