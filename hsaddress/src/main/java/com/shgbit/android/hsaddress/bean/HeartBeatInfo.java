package com.shgbit.android.hsaddress.bean;

public class HeartBeatInfo {
	private String result;
	private String message;
	private String failedMessage;
	private HeartBeatEvents[] heartbeatEvents;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFailedMessage() {
		return failedMessage;
	}
	public void setFailedMessage(String failedMessage) {
		this.failedMessage = failedMessage;
	}
	public HeartBeatEvents[] getHeartbeatEvents() {
		return heartbeatEvents;
	}
	public void setHeartbeatEvents(HeartBeatEvents[] heartbeatEvents) {
		this.heartbeatEvents = heartbeatEvents;
	}
	
	
}
