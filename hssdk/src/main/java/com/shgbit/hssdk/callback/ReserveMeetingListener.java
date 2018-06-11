package com.shgbit.hssdk.callback;


import com.shgbit.hssdk.json.Meeting;

public interface ReserveMeetingListener {
    void onReserveMeeting(boolean result, String error, Meeting meeting);
}
