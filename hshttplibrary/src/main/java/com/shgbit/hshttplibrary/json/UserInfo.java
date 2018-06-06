package com.shgbit.hshttplibrary.json;

public class UserInfo {
	private String userName;
	private String displayName;
	private String firstWord;
	private String mobilePhone;
	private String[] organizations;
	private OrganizationObject[] organizationObjects;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getFirstWord() {
		return firstWord;
	}
	public void setFirstWord(String firstWord) {
		this.firstWord = firstWord;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String[] getOrganizations() {
		return organizations;
	}
	public void setOrganizations(String[] organizations) {
		this.organizations = organizations;
	}

	public OrganizationObject[] getOrganizationObjects() {
		return organizationObjects;
	}

	public void setOrganizationObjects(OrganizationObject[] organizationObjects) {
		this.organizationObjects = organizationObjects;
	}
}
