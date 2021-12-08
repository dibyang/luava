package com.ls.luava.os;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class CmdBuilder {
	final List<String> cmds=new ArrayList<>();
  final Map<String,String> environment = new HashMap<>();

  boolean shell = false;
  boolean readOut = true;
  boolean readErr = true;

  protected CmdBuilder(String cmd) {
    environment.put("LANG","UTF-8");
		cmds.add(cmd);
	}

  public Map<String, String> getEnvironment() {
    return environment;
  }

  public CmdBuilder setShell(boolean shell) {
    this.shell = shell;
    return this;
  }

  public CmdBuilder setReadOut(boolean readOut) {
    this.readOut = readOut;
    return this;
  }

  public boolean isReadOut() {
    return readOut;
  }

  public CmdBuilder setReadErr(boolean readErr) {
    this.readErr = readErr;
    return this;
  }

  public boolean isReadErr() {
    return readErr;
  }
	
	public CmdBuilder addArg(Object arg)
	{
		if(arg!=null)
		{
			if(arg instanceof Collection<?>)
			{
				for(Object o:(Collection<?>)arg)
				{
					this.addArg(o);
				}
			}else if(arg.getClass().isArray())
			{
				for(int i = 0; i< Array.getLength(arg); i++)
				{
					this.addArg(Array.get(arg, i));
				}
				
			}else
			{
				cmds.add(arg.toString());
			}
			
		}
		return this;
	}
	
	public CmdBuilder addArgs(Object... args)
	{
		if(args!=null)
		{
			for(Object arg:args)
			{
				this.addArg(arg);
			}
		}		
		return this;
	}

  public String[] getCmdArray() {
    return cmds.toArray(new String[]{});
	}
	
	@Override
	public String toString() {
    return toString(this.getCmdArray());
  }

  public static String toString(String[] cmds) {
    return ProcessTool.createCommandLine(cmds);
  }

  public CmdResult exec() {
    return exec(null);
  }

  public CmdResult exec(PreExecute preExecute) {
    return OSProxyFactory.factory.getOSProxy().exec(this, preExecute);
  }

  public ProcessBuilder processBuilder() throws IOException {
    return OSProxyFactory.factory.getOSProxy().processBuilder(this);
  }

  public static CmdResult kill(String pid) {
    return OSProxyFactory.factory.getOSProxy().kill(pid);
  }

	public static CmdBuilder create(String cmd)
	{
		return new CmdBuilder(cmd);
	}
	
	public static CmdBuilder create(String cmd, Object... args)
	{		
		CmdBuilder builder= new CmdBuilder(cmd);
		builder.addArgs(args);	
		return builder;
	}


  public static void main(String[] args)
	{
		CmdBuilder b=CmdBuilder.create("test", "d1","d2",new int[]{3,4,5}, Arrays.asList("a6",7,"a8"),"d9");
		
		System.out.println(b.cmds);
	}

}
