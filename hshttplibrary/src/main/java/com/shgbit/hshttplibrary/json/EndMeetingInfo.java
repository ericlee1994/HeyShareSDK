package com.shgbit.hshttplibrary.json;

public class EndMeetingInfo {
	private String meetingId;
	private String userName;
	private String sessionId;
	private String sessionType;
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionType() {
		return sessionType;
	}

	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}
}
