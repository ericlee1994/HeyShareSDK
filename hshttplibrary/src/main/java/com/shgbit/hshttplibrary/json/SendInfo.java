package com.shgbit.hshttplibrary.json;

/**
 * Created by Administrator on 2017/9/25.
 */

public class SendInfo {
    private String meetingId;
    private String sessionId;
    private String[] users;

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }
}
