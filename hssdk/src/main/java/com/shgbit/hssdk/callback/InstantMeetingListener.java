package com.shgbit.hssdk.callback;


import com.shgbit.hshttplibrary.json.Meeting;

public interface InstantMeetingListener {
    void onCreateMeeting(boolean result, String error, Meeting meeting);
}
