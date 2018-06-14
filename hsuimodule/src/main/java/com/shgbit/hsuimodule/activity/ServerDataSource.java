package com.shgbit.hsuimodule.activity;

import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hsuimodule.bean.InvitedMeeting;
import com.shgbit.hsuimodule.bean.User;

import java.util.ArrayList;

public interface ServerDataSource {
    interface LoadVideosCallback {
        void onVideosLoaded(ArrayList<MemberInfo> mScreenList, ArrayList<MemberInfo> mOtherList, ArrayList<MemberInfo> mUnjoinedList);
        void onNoVideosAvailable();
    }

    interface InviteUsersCallback {
        void onInviteUsers(User[] users);
    }

    interface InviteMeetingCallback {
        void onInviteMeeting(InvitedMeeting invitedMeeting);
    }

    interface RecordCallback {
        void onStartRecord(boolean result, String error);
        void onEndRecord(boolean result, String error);
    }

    void getVideos(LoadVideosCallback loadVideosCallback);

    void getInviteUsers(InviteUsersCallback inviteUsersCallback);

    void getInviteMeeting(InviteMeetingCallback inviteMeetingCallback);

    void getRecord(RecordCallback recordCallback);
}
