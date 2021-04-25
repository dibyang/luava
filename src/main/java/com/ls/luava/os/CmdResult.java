package com.ls.luava.os;


public class CmdResult {
	public final static int SUCCESS=0;
	public final static CmdResult Null=new CmdResult(-99999,"Null Result Object","Null Result Object");
	private int status=0;
	private String result="";
	private String error="";
	
	
	public final boolean isNull;
	
	
	public CmdResult() {
		isNull=true;
	}
	
	public boolean isSuccess()
	{
		return this.status==SUCCESS;
	}
	
	private CmdResult(int status, String result, String error) {
		isNull=true;
		this.status = status;
		this.result = result;
		this.error = error;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		StringBuilder s=new StringBuilder();
		s.append("{")
		.append("status:")
		.append(status)
		.append(",")
		.append("result:\"")
		.append(result)
		.append("\",")
		.append("error:\"")
		.append(error)
		.append("\"}");
		return s.toString();
	}

}
