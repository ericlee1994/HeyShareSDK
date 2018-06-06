package com.shgbit.hshttplibrary.json;

public class ServiceSettings {
	private XiaoYuConfig xiaoyuConfig;
	private PushConfig GBPushConfig;
	private HotFixConfig GAHFSConfig;
	public XiaoYuConfig getXiaoyuConfig() {
		return xiaoyuConfig;
	}
	public void setXiaoyuConfig(XiaoYuConfig xiaoyuConfig) {
		this.xiaoyuConfig = xiaoyuConfig;
	}

	public PushConfig getGBPushConfig() {
		return GBPushConfig;
	}

	public void setGBPushConfig(PushConfig GBPushConfig) {
		this.GBPushConfig = GBPushConfig;
	}

	public HotFixConfig getGAHFSConfig() {
		return GAHFSConfig;
	}
	public void setGAHFSConfig(HotFixConfig gAHFSConfig) {
		GAHFSConfig = gAHFSConfig;
	}
	
}
