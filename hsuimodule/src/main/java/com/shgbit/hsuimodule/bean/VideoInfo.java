package com.shgbit.hsuimodule.bean;

import com.shgbit.hssdk.bean.Net_Status;
import com.shgbit.hssdk.bean.SessionType;
import com.shgbit.hssdk.bean.Status;

/**
 * Created by Eric on 2017/7/19.
 */

public class VideoInfo {

    private String dataSourceID;
    private String displayName;
    private String remoteName;
    private Status status;
    private String id;
    private SessionType sessionType;
    private int participantId;
    private boolean isContent;
    private boolean isVideoMute;
    private Net_Status net_status;
    private boolean isAudioMute;
    private boolean isLocal;
    private boolean isUVC;
//    private boolean FullScreen;
//    private boolean isAudioMode;
//    private boolean isInBanList;
//    private boolean isBlank;
//    private ArrayList<String> mUrls;
//    private String mResId;
//    private boolean isComment;


    public Net_Status getNet_status() {
        return net_status;
    }

    public void setNet_status(Net_Status net_status) {
        this.net_status = net_status;
    }

    public String getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(String dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String remoteName) {
        this.displayName = remoteName;
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

    public boolean isVideoMute() {
        return isVideoMute;
    }

    public void setVideoMute(boolean videoMute) {
        isVideoMute = videoMute;
    }

    public boolean isAudioMute() {
        return isAudioMute;
    }

    public void setAudioMute(boolean audioMute) {
        isAudioMute = audioMute;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemoteName() {
        return remoteName;
    }

    public void setRemoteName(String remoteName) {
        this.remoteName = remoteName;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public boolean isUVC() {
        return isUVC;
    }

    public void setUVC(boolean UVC) {
        isUVC = UVC;
    }
}
