package com.shgbit.hssdk.json;

import java.util.ArrayList;

public class RootOrganization {
	private String organizationId;
	private String organizationName;
	private ArrayList<UserOrganization> userOrganizations;
	private ArrayList<RootOrganization> rootOrganizations;
	public RootOrganization(){
		rootOrganizations=new ArrayList<RootOrganization>();
		userOrganizations=new ArrayList<UserOrganization>();
	}


	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public ArrayList<UserOrganization> getUserOrganizations() {
		return userOrganizations;
	}
	public void setUserOrganizations(ArrayList<UserOrganization> userOrganizations) {
		this.userOrganizations = userOrganizations;
	}
	public ArrayList<RootOrganization> getRootOrganizations() {
		return rootOrganizations;
	}
	public void setRootOrganizations(ArrayList<RootOrganization> rootOrganizations) {
		this.rootOrganizations = rootOrganizations;
	}
	
}
