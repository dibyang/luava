package com.ls.luava.os;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.*;

public abstract class BaseOSProxy implements OSProxy {
  final static Logger LOG = LoggerFactory.getLogger(BaseOSProxy.class);
  static final String line_separator = "\n";
  static final ThreadPoolExecutor executorService = new ThreadPoolExecutor(2, 1, 5L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new BasicThreadFactory.Builder().namingPattern("shell-executor-%d").daemon(true).build(), new ThreadPoolExecutor.CallerRunsPolicy());

  @Override
  public ProcessBuilder processBuilder(CmdBuilder cmdBuilder) throws IOException {
    String[] cmdArray = getCmdarray(cmdBuilder);

    String cmdline = CmdBuilder.toString(cmdArray);
    if (cmdBuilder.debug) {
      LOG.info(cmdline);
    }
    ProcessBuilder processBuilder = new ProcessBuilder(cmdArray);
    processBuilder.environment().putAll(cmdBuilder.getEnvironment());
    if (cmdBuilder.debug) {
      LOG.info("environment:{}", processBuilder.environment());
    }
    return processBuilder;
  }

  protected String[] getCmdarray(CmdBuilder cmdBuilder) {
    return cmdBuilder.shell ? new String[]{"/bin/sh", "-c", cmdBuilder.toString()} : cmdBuilder.getCmdArray();
  }

  @Override
  public Process process(String cmd, Object... args) {
    return process(CmdBuilder.create(cmd, args));
  }

  @Override
  public Process process(CmdBuilder cmdBuilder) {
    return process(cmdBuilder, null);
  }

  @Override
  public Process process(CmdBuilder cmdBuilder, PreExecute preExecute) {
    Process process = null;
    try {
      ProcessBuilder processBuilder = processBuilder(cmdBuilder);
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
  public CmdResult exec(CmdBuilder cmdBuilder) {
    return exec(cmdBuilder, null);
  }

  @Override
  public CmdResult exec(CmdBuilder cmdBuilder, PreExecute preExecute) {
    CmdResult r = new CmdResult();
    try {
      ProcessBuilder processBuilder = processBuilder(cmdBuilder);
      if (preExecute != null) {
        preExecute.preExecute(processBuilder);
      }
      final Process process = processBuilder.start();
      List<Callable<Void>> task = Lists.newArrayList();
      CountDownLatch countDownLatch = new CountDownLatch(2);
      task.add(() -> {
        try {
          StringBuilder err = new StringBuilder();
          try (BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = error.readLine()) != null) {
              err.append(line)
                      .append(line_separator);
            }
          }
          r.setError(err.toString());

        } catch (IOException e) {
          LOG.warn("", e);
          r.setStatus(1);
          r.setError(e.getMessage());
        } finally {
          countDownLatch.countDown();
        }
        return null;
      });

      task.add(() -> {
        StringBuilder buf = new StringBuilder();
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
          String line;
          while ((line = input.readLine()) != null) {
            buf.append(line)
                    .append(line_separator);
          }
        } catch (IOException e) {
          r.setStatus(1);
          r.setError(e.getMessage());
        } finally {
          countDownLatch.countDown();
          r.setResult(buf.toString());
        }
        return null;
      });

      executorService.invokeAll(task);
      countDownLatch.wait();
      process.getOutputStream().close();
      int exitVal = process.waitFor();
      r.setStatus(exitVal);
      if (cmdBuilder.debug) {
        LOG.info(r.toString());
      }
    } catch (IOException | InterruptedException e) {
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
