package com.ls.luava.os;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WindowsProxy extends BaseOSProxy {
	final static Logger LOG = LoggerFactory.getLogger(WindowsProxy.class);
	public final static WindowsProxy INSTANCE = new WindowsProxy();

	public WindowsProxy()
	{
	}


  @Override
  public OS getOS() {
    return OS.windows;
  }

  @Override
  public String[] getShellCmdArray(String cmd) {
    return new String[]{"cmd.exe", "/c", cmd};
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
  public boolean existProcess(String pid){
    CmdResult exec = execShell("tasklist",  "/FI", "PID eq " + pid);
    return exec.getResult().contains(pid);
  }

  @Override
  public CmdResult kill(String pid) {
    return exec("tskill", pid);
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
