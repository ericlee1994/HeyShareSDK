package com.shgbit.hssdk.callback;


import com.shgbit.hssdk.json.Meeting;

public interface InstantMeetingListener {
    void onCreateMeeting(boolean result, String error, Meeting meeting);
}
