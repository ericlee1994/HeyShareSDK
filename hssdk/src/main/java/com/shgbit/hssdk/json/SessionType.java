package com.shgbit.hssdk.json;

/**
 * Created by chenfangfang on 2017/11/7.
 */

public class SessionType {
	private DeviceState ContentOnlyState;
	private DeviceState MobileState;
	private DeviceState PCState;

	public DeviceState getContentOnlyState() {
		return ContentOnlyState;
	}

	public void setContentOnlyState(DeviceState contentOnlyState) {
		ContentOnlyState = contentOnlyState;
	}

	public DeviceState getMobileState() {
		return MobileState;
	}

	public void setMobileState(DeviceState mobileState) {
		MobileState = mobileState;
	}

	public DeviceState getPCState() {
		return PCState;
	}

	public void setPCState(DeviceState PCState) {
		this.PCState = PCState;
	}
}
