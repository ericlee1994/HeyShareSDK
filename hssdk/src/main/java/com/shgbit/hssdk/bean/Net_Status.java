package com.shgbit.hssdk.bean;

public enum Net_Status {
	NULL, 	//初始
	NORMAL,	//正常
	LOADING,//加载中
	LOST,	//带宽不足
	VIDEO_MUTE, //
	CONTENTONLY_UNSEND, //contentonly入会并且没点发送
	AUDIO_MODE
}
