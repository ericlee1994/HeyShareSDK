package com.shgbit.hshttplibrary.json;

public class CancelInviteInfo {
	private String meetingId;
	private String sessionId;
	private String invitedUser;
	private String sessionType;
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getInvitedUser() {
		return invitedUser;
	}
	public void setInvitedUser(String invitedUser) {
		this.invitedUser = invitedUser;
	}

	public String getSessionType() {
		return sessionType;
	}

	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}
}
