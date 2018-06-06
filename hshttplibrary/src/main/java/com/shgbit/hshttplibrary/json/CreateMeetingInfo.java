package com.shgbit.hshttplibrary.json;

public class CreateMeetingInfo {
	private String createdUser;
	private String[] invitedUsers;
	private String sessionId;
	private String sessionType;
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
