package com.shgbit.android.hsaddress.bean;

/**
 * Created by Administrator on 2017/10/31.
 */

public class CreateGroupInfo {
    private String sessionId;
    private String name;
    private String[] member;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getMember() {
        return member;
    }

    public void setMember(String[] member) {
        this.member = member;
    }
}
