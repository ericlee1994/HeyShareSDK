package com.shgbit.hssdk.json;



public class UserOrganization {
	private String userName;
	private String displayName;
	private String[] department;
	private String status;
	private MobileStateSessionType MobileStateSessionType;
	private PCStateSessionType PCStateSessionType;
	private ContentOnlyStateSessionType ContentOnlyStateSessionType;
	private boolean isSelect;
	private String fWordString;
	private String firstWord;
	private String mobilePhone;
	private boolean isCollect;
	private OrganizationObject[] organizationObjects;
	public String getfWordString() {
		return fWordString;
	}
	public void setfWordString(String fWordString) {
		this.fWordString = fWordString;
	}
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

	public String[] getDepartment() {
		return department;
	}
	public void setDepartment(String[] department) {
		this.department = department;
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

	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
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
	public boolean isCollect() {
		return isCollect;
	}
	public void setCollect(boolean isCollect) {
		this.isCollect = isCollect;
	}

	public OrganizationObject[] getOrganizationObjects() {
		return organizationObjects;
	}

	public void setOrganizationObjects(OrganizationObject[] organizationObjects) {
		this.organizationObjects = organizationObjects;
	}
}
