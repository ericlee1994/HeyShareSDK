package com.shgbit.hssdk.bean;

public class MemberInfo {
	//custom
	private String id;
	private String userName;//same as id
	private String displayName;
	private SessionType sessionType;
	private Net_Status net_status;
	private Status status;
	private boolean isLocal;
	private boolean isBlank;//?
	private boolean isUvc;

	//xiaoyu
	private String remoteName;//same as id
	private String dataSourceID;
	private int participantId;
	private boolean isContent;
	private boolean isAudioMute;
	private boolean isVideoMute;
	
	public MemberInfo() {
		id = "";
		userName = "";
		displayName = "";
		sessionType = SessionType.ALL;
		net_status = Net_Status.NULL;
		status= null;
		isLocal = false;
		isBlank = false;
		isUvc = false;
		remoteName = "";
		dataSourceID = "";
		participantId = 0;
		isContent = false;
		isAudioMute = false;
		isVideoMute = false;
	}

	public MemberInfo(MemberInfo info) {
		isBlank = info.isBlank();
		isLocal = info.isLocal();
		id = info.getId();
		dataSourceID = info.getDataSourceID();
		participantId = info.getParticipantId();
		remoteName = info.getRemoteName();
		isAudioMute = info.isAudioMute();
		isContent = info.isContent();
		isVideoMute = info.isVideoMute();
		userName = info.getUserName();
		displayName = info.getDisplayName();
		status= info.getStatus();
		sessionType = info.getSessionType();
		net_status = info.getNet_status();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public SessionType getSessionType() {
		return sessionType;
	}

	public void setSessionType(SessionType sessionType) {
		this.sessionType = sessionType;
	}

	public Net_Status getNet_status() {
		return net_status;
	}

	public void setNet_status(Net_Status net_status) {
		this.net_status = net_status;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isLocal() {
		return isLocal;
	}

	public void setLocal(boolean local) {
		isLocal = local;
	}

	public boolean isBlank() {
		return isBlank;
	}

	public void setBlank(boolean blank) {
		isBlank = blank;
	}

	public boolean isUvc() {
		return isUvc;
	}

	public void setUvc(boolean uvc) {
		isUvc = uvc;
	}

	public String getRemoteName() {
		return remoteName;
	}

	public void setRemoteName(String remoteName) {
		this.remoteName = remoteName;
	}

	public String getDataSourceID() {
		return dataSourceID;
	}

	public void setDataSourceID(String dataSourceID) {
		this.dataSourceID = dataSourceID;
	}

	public int getParticipantId() {
		return participantId;
	}

	public void setParticipantId(int participantId) {
		this.participantId = participantId;
	}

	public boolean isContent() {
		return isContent;
	}

	public void setContent(boolean content) {
		isContent = content;
	}

	public boolean isAudioMute() {
		return isAudioMute;
	}

	public void setAudioMute(boolean audioMute) {
		isAudioMute = audioMute;
	}

	public boolean isVideoMute() {
		return isVideoMute;
	}

	public void setVideoMute(boolean videoMute) {
		isVideoMute = videoMute;
	}

}
