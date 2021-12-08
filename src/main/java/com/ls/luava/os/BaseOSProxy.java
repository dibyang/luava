package com.ls.luava.os;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class BaseOSProxy implements OSProxy {
  final static Logger LOG = LoggerFactory.getLogger(BaseOSProxy.class);
  static final String line_separator = "\n";


  @Override
  public ProcessBuilder processBuilder(CmdBuilder cmdBuilde) throws IOException {
    String[] cmdarray = getCmdarray(cmdBuilde);

    String cmdline = CmdBuilder.toString(cmdarray);
    LOG.debug(cmdline);
    ProcessBuilder processBuilder = new ProcessBuilder(cmdarray);
    processBuilder.environment().putAll(cmdBuilde.getEnvironment());
    return processBuilder;
  }

  protected String[] getCmdarray(CmdBuilder cmdBuilde) {
    String[] cmdarray = cmdBuilde.shell ? new String[]{"/bin/sh", "-c", cmdBuilde.toString()} : cmdBuilde.getCmdArray();
    return cmdarray;
  }

  @Override
  public Process process(String cmd, Object... args) {
    return process(CmdBuilder.create(cmd, args));
  }

  @Override
  public Process process(CmdBuilder cmdBuilde) {
    return process(cmdBuilde,null);
  }

  @Override
  public Process process(CmdBuilder cmdBuilde, PreExecute preExecute) {
    Process process = null;
    try {
      ProcessBuilder processBuilder = processBuilder(cmdBuilde);
      if (preExecute != null) {
        preExecute.preExecute(processBuilder);
      }
      process = processBuilder.start();
    } catch (IOException e) {
      LOG.warn("", e);
    }
    return process;
  }

  @Override
  public CmdResult exec(String cmd, Object... args) {
    return exec(CmdBuilder.create(cmd, args));
  }

  @Override
  public CmdResult exec(CmdBuilder cmdBuilde) {
    return exec(cmdBuilde, null);
  }

  @Override
  public CmdResult exec(CmdBuilder cmdBuilde, PreExecute preExecute) {
    CmdResult r = new CmdResult();
    Process process = null;
    try {
      ProcessBuilder processBuilder = processBuilder(cmdBuilde);
      if (preExecute != null) {
        preExecute.preExecute(processBuilder);
      }
      process = processBuilder.start();
      if (process != null) {

        StringBuilder buf = new StringBuilder();


        if (cmdBuilde.isReadErr()) {
          StringBuilder err = new StringBuilder();
          try (BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line = null;
            while ((line = error.readLine()) != null) {
              err.append(line)
                  .append(line_separator);
              buf.append(line)
                  .append(line_separator);
            }
          }
          LOG.debug(err.toString());
          r.setError(err.toString());
        }
        if (cmdBuilde.isReadOut()) {
          try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line = null;
            while ((line = input.readLine()) != null) {
              buf.append(line)
                  .append(line_separator);
            }
          }
          LOG.debug(buf.toString());
          r.setResult(buf.toString());
        }
        process.getOutputStream().close();
        int exitVal = process.waitFor();
        r.setStatus(exitVal);
      }
    } catch (IOException e) {
      LOG.error("", e);
      r.setStatus(1);
      r.setError(e.getMessage());
    } catch (InterruptedException e) {
      LOG.error("", e);
      r.setStatus(1);
      r.setError(e.getMessage());
    }
    return r;
  }

  public CmdBuilder createShellBuilder(String cmd) {
    return CmdBuilder.create(cmd);
  }

  @Override
  public CmdResult execShell(String cmd) {
    CmdBuilder shell = CmdBuilder.create(cmd).setShell(true);
    return this.exec(shell);
  }

  @Override
  public CmdResult execShell(String cmd, Object... args) {
    CmdBuilder shell = CmdBuilder.create(cmd, args).setShell(true);
    return this.exec(shell);
  }
}
