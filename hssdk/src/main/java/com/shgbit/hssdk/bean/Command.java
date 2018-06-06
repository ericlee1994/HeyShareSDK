package com.shgbit.hssdk.bean;

public class Command {

	private Cmd name;
	
	private Object [] args;

	public Cmd getName() {
		return name;
	}

	public void setName(Cmd name) {
		this.name = name;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
	
	
}
