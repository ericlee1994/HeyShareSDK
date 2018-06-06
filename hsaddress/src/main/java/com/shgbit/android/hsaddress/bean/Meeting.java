package com.shgbit.android.hsaddress.bean;


public class Meeting {
	private String meetingId;
	private String meetingName;
	private User createdUser;
	private User[] users;
	private Record record;
	private String status;
	private String type;
	private String password;
	private YunDesktop yunDesktop;
	private String startTime;//"2016-10-20 15:00"
	private String endTime;
	private String actualStartTime;
	private String actualEndTime;
	private int duration;//second
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getMeetingName() {
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	public User getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(User createdUser) {
		this.createdUser = createdUser;
	}
	public User[] getUsers() {
		return users;
	}
	public void setUsers(User[] users) {
		this.users = users;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public YunDesktop getYunDesktop() {
		return yunDesktop;
	}
	public void setYunDesktop(YunDesktop yunDesktop) {
		this.yunDesktop = yunDesktop;
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
	public String getActualStartTime() {
		return actualStartTime;
	}
	public void setActualStartTime(String actualStartTime) {
		this.actualStartTime = actualStartTime;
	}
	public String getActualEndTime() {
		return actualEndTime;
	}
	public void setActualEndTime(String actualEndTime) {
		this.actualEndTime = actualEndTime;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
