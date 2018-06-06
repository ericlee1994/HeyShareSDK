package com.shgbit.android.hsaddress.bean;

public class KickoutInfo {
	private String meetingId;
	private String[] kickoutUsers;
	private String sessionId;
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String[] getKickoutUsers() {
		return kickoutUsers;
	}
	public void setKickoutUsers(String[] kickoutUsers) {
		this.kickoutUsers = kickoutUsers;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
