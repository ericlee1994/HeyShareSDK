package com.shgbit.hshttplibrary.json;


public class LoginResponse {
	private String result;
	private String failedMessage;
	private String sessionId;
	private User user;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getFailedMessage() {
		return failedMessage;
	}
	public void setFailedMessage(String failedMessage) {
		this.failedMessage = failedMessage;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
