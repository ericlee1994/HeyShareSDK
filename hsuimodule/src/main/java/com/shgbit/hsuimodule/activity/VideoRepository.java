package com.shgbit.hsuimodule.activity;

import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hssdk.callback.HeyshareCallback;
import com.shgbit.hssdk.callback.VideoStateCallback;
import com.shgbit.hssdk.json.InviteCancledInfo;
import com.shgbit.hssdk.json.InvitedMeeting;
import com.shgbit.hssdk.json.RefuseInfo;
import com.shgbit.hssdk.json.TimeoutInfo;
import com.shgbit.hssdk.json.User;
import com.shgbit.hssdk.sdk.VideoCtrl;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class VideoRepository implements ServerDataSource {

    private static VideoRepository INSTANCE = null;
    private final ServerDataSource mServerDataSource;
    private RecordCallback recordCallback;

    private VideoRepository(ServerDataSource serverDataSource) {
        mServerDataSource = checkNotNull(serverDataSource);
    }


    public static VideoRepository getInstance(ServerDataSource serverDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new VideoRepository(serverDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public HeyshareCallback getHeyShareCallback(){
        return  heyshareCallback;
    }


    public HeyshareCallback heyshareCallback = new HeyshareCallback() {
        @Override
        public void onLogin(boolean result, String error, User user) {

        }

        @Override
        public void onLogout(boolean result, String error) {

        }

        @Override
        public void onCheckPwd(boolean result, String error) {

        }

        @Override
        public void onMotifyPwd(boolean result, String error) {

        }

        @Override
        public void onJoinMeeting(boolean result, String error) {

        }

        @Override
        public void onInviteMeeting(boolean success, String error) {

        }

        @Override
        public void onQuiteMeeting(boolean success, String error) {

        }

        @Override
        public void onDeleteMeeting(boolean success, String error) {

        }

        @Override
        public void onUpdateMeeting(boolean success, String error) {

        }

        @Override
        public void onBusyMeeting(boolean success, String error) {

        }

        @Override
        public void onMeetings() {

        }

        @Override
        public void startRecord(boolean result, String err) {
            recordCallback.onStartRecord(result, err);
        }

        @Override
        public void endRecord(boolean result, String err) {
            recordCallback.onEndRecord(result, err);
        }

        @Override
        public void eventUserStateChanged(RefuseInfo[] refuseInfos, TimeoutInfo[] timeoutInfos) {

        }

        @Override
        public void eventInvitedMeeting(InvitedMeeting meeting) {

        }

        @Override
        public void eventDifferentPlaceLogin() {

        }

        @Override
        public void eventInvitingCancle(InviteCancledInfo ici) {

        }
    };

    @Override
    public void getVideos(LoadVideosCallback loadVideosCallback) {

    }

    @Override
    public void getInviteUsers(InviteUsersCallback inviteUsersCallback) {

    }

    @Override
    public void getInviteMeeting(InviteMeetingCallback inviteMeetingCallback) {

    }

    public VideoStateCallback videoStateCallback = new VideoStateCallback() {
        @Override
        public void onConnectFailed(String err) {

        }

        @Override
        public void onConnectSuccess(LoginResponseData loginResponseData) {

        }

        @Override
        public void onCallFailed(String err) {

        }

        @Override
        public void onCallStateChange(VideoCtrl.VideoCallState state, String s) {

        }

        @Override
        public void onMemberChanged(ArrayList<MemberInfo> mScreen, ArrayList<MemberInfo> mOther, ArrayList<MemberInfo> mUnjoined, int duration, boolean isMemberContent, String meetingName) {

        }

        @Override
        public void onMemberSizeChanged(int joinedSize, int memberSize) {

        }
    };

    @Override
    public void getRecord(ServerDataSource.RecordCallback recordCallback) {
        this.recordCallback = recordCallback;
    }
}
