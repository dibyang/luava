package com.ls.luava.os;

import java.io.IOException;

public interface OSProxy {
  OS getOS();

  CmdResult exec(String cmd, Object... args);

  CmdResult exec(CmdBuilder cmdBuilde);

  CmdResult exec(CmdBuilder cmdBuilde, PreExecute preExecute);

  ProcessBuilder processBuilder(CmdBuilder cmdBuilde) throws IOException;

  CmdResult execShell(String cmd);

  CmdResult execShell(String cmd, Object... args);

  CmdResult stopService(String service);

  CmdResult startService(String service);

  CmdResult restartService(String service);

  CmdResult kill(String pid);

  ServiceStatus statusService(String service);

}
