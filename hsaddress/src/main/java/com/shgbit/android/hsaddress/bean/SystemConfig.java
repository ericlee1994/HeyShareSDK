package com.shgbit.android.hsaddress.bean;

public class SystemConfig {
	private String result;
	private String failedMessage;
	private ServiceSettings serviceSettings;
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
	public ServiceSettings getServiceSettings() {
		return serviceSettings;
	}
	public void setServiceSettings(ServiceSettings serviceSettings) {
		this.serviceSettings = serviceSettings;
	}
	
}
