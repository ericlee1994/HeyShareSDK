package com.shgbit.hshttplibrary.json;


/**
 * Created by Administrator on 2017/9/25.
 */

public class ReserveRepsonse {
    private String result;
    private String failedMessage;
    private Meeting meeting;

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

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }
}
