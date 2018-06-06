package com.shgbit.hssdk.callback;

import com.shgbit.hshttplibrary.json.InviteCancledInfo;
import com.shgbit.hshttplibrary.json.InvitedMeeting;
import com.shgbit.hshttplibrary.json.RefuseInfo;
import com.shgbit.hshttplibrary.json.TimeoutInfo;
import com.shgbit.hshttplibrary.json.User;

public interface HeyshareCallback {
    void onLogin (boolean result, String error, User user);
    void onLogout (boolean result, String error);
    void onCheckPwd (boolean result, String error);
    void onMotifyPwd (boolean result, String error);
    void onJoinMeeting(boolean result, String error);
    void onInviteMeeting(boolean success, String error);
    void onQuiteMeeting(boolean success, String error);
    void onDeleteMeeting(boolean success, String error);
    void onUpdateMeeting(boolean success, String error);
    void onBusyMeeting(boolean success, String error);
    void onMeetings();
    void eventUserStateChanged(RefuseInfo[] refuseInfos, TimeoutInfo[] timeoutInfos);
    void eventInvitedMeeting(InvitedMeeting meeting);
    void eventDifferentPlaceLogin();
    void eventInvitingCancle(InviteCancledInfo ici);
}
