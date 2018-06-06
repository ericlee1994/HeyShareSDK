package com.shgbit.android.hsaddress.bean;

public class OnlineUser {
	private String userName;
	private String status;
	private MobileStateSessionType MobileStateSessionType;
	private PCStateSessionType PCStateSessionType;
	private ContentOnlyStateSessionType ContentOnlyStateSessionType;

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

	public MobileStateSessionType getMobileStateSessionType() {
		return MobileStateSessionType;
	}

	public void setMobileStateSessionType(MobileStateSessionType mobileStateSessionType) {
		MobileStateSessionType = mobileStateSessionType;
	}

	public PCStateSessionType getPCStateSessionType() {
		return PCStateSessionType;
	}

	public void setPCStateSessionType(PCStateSessionType PCStateSessionType) {
		this.PCStateSessionType = PCStateSessionType;
	}

	public ContentOnlyStateSessionType getContentOnlyStateSessionType() {
		return ContentOnlyStateSessionType;
	}

	public void setContentOnlyStateSessionType(ContentOnlyStateSessionType contentOnlyStateSessionType) {
		ContentOnlyStateSessionType = contentOnlyStateSessionType;
	}
}
