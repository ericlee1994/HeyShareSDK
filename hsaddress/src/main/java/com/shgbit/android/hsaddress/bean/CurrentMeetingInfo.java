package com.shgbit.android.hsaddress.bean;


public class CurrentMeetingInfo {
	private String result;
	private String failedMessage;
	private Meeting meeting;
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
	public Meeting getMeeting() {
		return meeting;
	}
	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}
	
}
