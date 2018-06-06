package com.shgbit.android.hsaddress.bean;


public class MeetingInfo {
	private String result;
	private String failedMessage;
	private Meeting[] meetings;
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
	public Meeting[] getMeetings() {
		return meetings;
	}
	public void setMeetings(Meeting[] meetings) {
		this.meetings = meetings;
	}

	
}
