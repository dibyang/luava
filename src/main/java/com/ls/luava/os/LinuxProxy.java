package com.ls.luava.os;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class LinuxProxy extends BaseOSProxy {
	final static Logger LOG = LoggerFactory.getLogger(LinuxProxy.class);
	public final static LinuxProxy INSTANCE = new LinuxProxy();

	public LinuxProxy()
	{
	}

  @Override
  public ProcessBuilder processBuilder(CmdBuilder cmdBuilde) throws IOException {
    if(cmdBuilde.en) {
      cmdBuilde.getEnvironment().put("LANG", "en_US.UTF-8");
      cmdBuilde.getEnvironment().put("LANGUAGE","en_US:en");
    }
    return super.processBuilder(cmdBuilde);
  }

  @Override
  public OS getOS() {
    return OS.linux;
  }


  @Override
  protected String[] getCmdarray(CmdBuilder cmdBuilde) {
    String[] cmdarray = cmdBuilde.shell ? new String[]{"/bin/sh", "-c", cmdBuilde.toString()} : cmdBuilde.getCmdArray();
    return cmdarray;
  }

  @Override
  public CmdResult stopService(String service) {
    return execShell("service", service, "stop");
  }


  @Override
  public CmdResult startService(String service) {
    return execShell("service", service, "start");
  }

  @Override
  public CmdResult restartService(String service) {
    return exec("service", service, "restart");
  }

  @Override
  public CmdResult kill(String pid) {
    //LOG.info("kill -9 " + pid);
    return exec("kill", "-9", pid);
  }

  @Override
  public boolean existProcess(String pid){
    CmdResult exec = execShell("ps", "aux", "|awk '{print $2}'", "|grep", "-w", pid);
    return exec.getResult().contains(pid);
  }

  @Override
  public ServiceStatus statusService(String service) {
    ServiceStatus status = new ServiceStatus();
    CmdResult cmdResult = execShell("service", service, "status");
    status.setStatus(cmdResult.getStatus());
    status.setExists(existsService(cmdResult));
    if (status.isExists()) {
      status.setOut(cmdResult.getResult());
    }
    return status;
  }

  public static boolean existsService(CmdResult r) {
    return (r.getStatus() == 3 || r.getStatus() == 0);
  }

}
