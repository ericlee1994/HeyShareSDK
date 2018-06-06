package com.shgbit.android.hsaddress.bean;

public class LoginInfo {
	private String userName;
	private String password;
	
	public LoginInfo(String username, String password) {
		this.userName = username;
		this.password = password;
	}
	
	public LoginInfo() {

	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
