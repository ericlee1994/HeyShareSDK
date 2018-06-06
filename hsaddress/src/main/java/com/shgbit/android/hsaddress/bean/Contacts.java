package com.shgbit.android.hsaddress.bean;

public class Contacts {
	private String result;
	private Organization[] organization;
	private UserInfo[] users;
	private String failedMessage;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Organization[] getOrganization() {
		return organization;
	}
	public void setOrganization(Organization[] organization) {
		this.organization = organization;
	}
	public UserInfo[] getUsers() {
		return users;
	}
	public void setUsers(UserInfo[] users) {
		this.users = users;
	}
	public String getFailedMessage() {
		return failedMessage;
	}
	public void setFailedMessage(String failedMessage) {
		this.failedMessage = failedMessage;
	}
	
	
}
