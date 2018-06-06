package com.shgbit.android.hsaddress.bean;

public class JoinMeetingInfo {
	private String meetingId;
	private String userName;
	private String password;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
