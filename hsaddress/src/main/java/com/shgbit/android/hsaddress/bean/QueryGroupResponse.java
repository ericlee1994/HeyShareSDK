package com.shgbit.android.hsaddress.bean;

/**
 * Created by Administrator on 2017/10/31.
 */

public class QueryGroupResponse {
    private String result;
    private String failedMessage;
    private Group[] groups;

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

    public Group[] getGroups() {
        return groups;
    }

    public void setGroups(Group[] groups) {
        this.groups = groups;
    }
}
