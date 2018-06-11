package com.shgbit.hssdk.json;

public class InvitedMeeting {
	private String meetingId;
	private String password;
	private String meetingName;
	private String inviter;
	private String inviterDisplayName;
	private String type;

	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMeetingName() {
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	public String getInviter() {
		return inviter;
	}
	public void setInviter(String inviter) {
		this.inviter = inviter;
	}
	public String getInviterDisplayName() {
		return inviterDisplayName;
	}
	public void setInviterDisplayName(String inviterDisplayName) {
		this.inviterDisplayName = inviterDisplayName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
