package com.shgbit.hshttplibrary.json;

public class Online {
	private String result;
	private OnlineUser[] onlineUsers;
	private String failedMessage;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public OnlineUser[] getOnlineUsers() {
		return onlineUsers;
	}
	public void setOnlineUsers(OnlineUser[] onlineUsers) {
		this.onlineUsers = onlineUsers;
	}
	public String getFailedMessage() {
		return failedMessage;
	}
	public void setFailedMessage(String failedMessage) {
		this.failedMessage = failedMessage;
	}

	
}
