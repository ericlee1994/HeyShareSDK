package com.shgbit.hshttplibrary.json;

/**
 * Created by Administrator on 2017/10/31.
 */

public class DeleFrmGroupInfo {
    private String sessionId;
    private String id;
    private String[] members;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }
}
