package com.shgbit.hshttplibrary.json;

public class User {
	private String userName;
	private String displayName;
	private String status;
	private SessionType sessionType;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public SessionType getSessionType() {
		return sessionType;
	}

	public void setSessionType(SessionType sessionType) {
		this.sessionType = sessionType;
	}
}
