package com.shgbit.android.hsaddress.bean;

public class ReserveInfo {
	private String meetingName;
	private String startTime;
	private String endTime;
	private String createdUser;
	private String[] invitedUsers;
	private String sessionId;
	private String sessionType;
	public String getMeetingName() {
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public String[] getInvitedUsers() {
		return invitedUsers;
	}
	public void setInvitedUsers(String[] invitedUsers) {
		this.invitedUsers = invitedUsers;
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
