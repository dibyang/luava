package com.ls.luava.os;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LinuxProxy extends BaseOSProxy {
	final static Logger LOG = LoggerFactory.getLogger(LinuxProxy.class);
	public final static LinuxProxy INSTANCE = new LinuxProxy();
  static final String LANG_EN = "LANG=\"en_US.UTF-8\";";

	public LinuxProxy()
	{
	}


  @Override
  public OS getOS() {
    return OS.linux;
  }

  @Override
  public String[] getShellCmdArray(String cmd) {
    return new String[]{"/bin/sh", "-c", cmd};
  }

  @Override
  protected String[] getCmdarray(CmdBuilder cmdBuilde) {
    String[] cmdarray = super.getCmdarray(cmdBuilde);
    cmdarray[0] = LANG_EN + cmdarray[0];
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
