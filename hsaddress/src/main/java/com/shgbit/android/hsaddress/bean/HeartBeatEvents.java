package com.shgbit.android.hsaddress.bean;


public class HeartBeatEvents {
	private String eventName;
	private String eventParams;
	private long timeStamp;
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventParams() {
		return eventParams;
	}
	public void setEventParams(String eventParams) {
		this.eventParams = eventParams;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
}
