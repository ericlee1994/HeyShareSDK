package com.shgbit.hshttplibrary.json;


public class EndYunDeskResponse {
	private String result;
	private String failedMessage;
	private YunDesktop yunDesktop;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getFailedMessage() {
		return failedMessage;
	}
	public void setFailedMessage(String failedMessage) {
		this.failedMessage = failedMessage;
	}
	public YunDesktop getYunDesktop() {
		return yunDesktop;
	}
	public void setYunDesktop(YunDesktop yunDesktop) {
		this.yunDesktop = yunDesktop;
	}
	
}
