package com.shgbit.android.hsaddress.bean;


public class CreateInfo {
	private String result;
	private Meeting meeting;
	private String failedMessage;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Meeting getMeeting() {
		return meeting;
	}
	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}
	public String getFailedMessage() {
		return failedMessage;
	}
	public void setFailedMessage(String failedMessage) {
		this.failedMessage = failedMessage;
	}
	
}
