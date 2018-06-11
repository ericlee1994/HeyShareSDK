package com.shgbit.hssdk.sdk;

import com.shgbit.hshttplibrary.MeetingCeche;
import com.shgbit.hshttplibrary.ServerInteractManager;
import com.shgbit.hshttplibrary.callback.ServerInteractCallback;
import com.shgbit.hshttplibrary.json.BusyMeetingInfo;
import com.shgbit.hshttplibrary.json.CancelInviteInfo;
import com.shgbit.hshttplibrary.json.CreateMeetingInfo;
import com.shgbit.hshttplibrary.json.DeleteInfo;
import com.shgbit.hshttplibrary.json.InviteCancledInfo;
import com.shgbit.hshttplibrary.json.InviteMeetingInfo;
import com.shgbit.hshttplibrary.json.InvitedMeeting;
import com.shgbit.hshttplibrary.json.JoinMeetingInfo;
import com.shgbit.hshttplibrary.json.LoginInfo;
import com.shgbit.hshttplibrary.json.Meeting;
import com.shgbit.hshttplibrary.json.MeetingRecord;
import com.shgbit.hshttplibrary.json.QuiteMeetingInfo;
import com.shgbit.hshttplibrary.json.RefuseInfo;
import com.shgbit.hshttplibrary.json.ReserveInfo;
import com.shgbit.hshttplibrary.json.ReserveMeetingInfo;
import com.shgbit.hshttplibrary.json.SendInfo;
import com.shgbit.hshttplibrary.json.SyncPidInfo;
import com.shgbit.hshttplibrary.json.TimeoutInfo;
import com.shgbit.hshttplibrary.json.User;
import com.shgbit.hshttplibrary.json.YunDesktop;
import com.shgbit.hssdk.callback.HeyshareCallback;
import com.shgbit.hssdk.callback.InstantMeetingListener;
import com.shgbit.hssdk.callback.ReserveMeetingListener;
import com.shgbit.hssdk.tool.JsonUtil;


public class CommonCtrl{
    private static CommonCtrl instance;
    private HeyshareCallback callback;
    private ReserveMeetingListener reserveMeetingListener;
    private InstantMeetingListener instantMeetingListener;
    private CommonCtrl (){
        ServerInteractManager.getInstance().setServerInteractCallback(serverInteractCallback);
    }

    public static void registerInstance () {
        if (instance == null) {
            instance = new CommonCtrl();
        }
        HeyShareSDK.setCommonCtrl(instance);
    }

    public void setHeyshareCallback (HeyshareCallback callback) {
        this.callback = callback;
    }

    public void login (String userName, String userPwd) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserName(userName);
        loginInfo.setPassword(userPwd);
        ServerInteractManager.getInstance().login(loginInfo);
    }

    public void logout () {
        ServerInteractManager.getInstance().logout();
    }

    public void checkPwd(String userName, String userPwd) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserName(userName);
        loginInfo.setPassword(userPwd);
        ServerInteractManager.getInstance().checkPwd(loginInfo);
    }

    public void modifyPwd(String userName, String userPwd) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserName(userName);
        loginInfo.setPassword(userPwd);
        ServerInteractManager.getInstance().motifyPwd(loginInfo);
    }

    public void createReserveMeeting (String createUser, String meetingName, String startTime, String endTime, String[] inviteUsers, ReserveMeetingListener listener) {
        reserveMeetingListener = listener;
        ReserveInfo reserveMeeting = new ReserveInfo();
        reserveMeeting.setCreatedUser(createUser);
        reserveMeeting.setMeetingName(meetingName);
        reserveMeeting.setStartTime(startTime);
        reserveMeeting.setEndTime(endTime);
        reserveMeeting.setInvitedUsers(inviteUsers);
        ServerInteractManager.getInstance().reserveMeeting(reserveMeeting);
    }

    public void createInstantMeeting(String createUser, String[] inviteUsers, InstantMeetingListener listener) {
        instantMeetingListener = listener;
        CreateMeetingInfo instantMeeting = new CreateMeetingInfo();
        instantMeeting.setCreatedUser(createUser);
        instantMeeting.setInvitedUsers(inviteUsers);
        ServerInteractManager.getInstance().createMeeting(instantMeeting);
    }

    public void joinMeeting(String userName, String meetingId, String meetingPwd){
        JoinMeetingInfo joinMeetingInfo = new JoinMeetingInfo();
        joinMeetingInfo.setMeetingId(meetingId);
        joinMeetingInfo.setPassword(meetingPwd);
        joinMeetingInfo.setUserName(userName);
        ServerInteractManager.getInstance().joinMeeting(joinMeetingInfo);
    }

    public void quitMeeting(String userName, String meetingId){
        QuiteMeetingInfo quiteMeetingInfo = new QuiteMeetingInfo();
        quiteMeetingInfo.setMeetingId(meetingId);
        quiteMeetingInfo.setUserName(userName);
        ServerInteractManager.getInstance().quiteMeeting(quiteMeetingInfo);
    }

    public void inviteUsers(String userName, String meetingId, String[] users){
        InviteMeetingInfo inviteMeetingInfo = new InviteMeetingInfo();
        inviteMeetingInfo.setMeetingId(meetingId);
        inviteMeetingInfo.setInvitedUsers(users);
        inviteMeetingInfo.setUserName(userName);
        ServerInteractManager.getInstance().inviteMeeting(inviteMeetingInfo);
    }

    public void cancelInviting(String meetingId, String invitedName){
        CancelInviteInfo cancelInviteInfo = new CancelInviteInfo();
        cancelInviteInfo.setMeetingId(meetingId);
        cancelInviteInfo.setInvitedUser(invitedName);
        ServerInteractManager.getInstance().cancleMeeting(cancelInviteInfo);
    }

    public void refuseInviting(String userName, String meetingId, String inviter){
        BusyMeetingInfo busyMeetingInfo = new BusyMeetingInfo();
        busyMeetingInfo.setMeetingId(meetingId);
        busyMeetingInfo.setInviter(inviter);
        busyMeetingInfo.setUserName(userName);
        ServerInteractManager.getInstance().busyMeeting(busyMeetingInfo);
    }

    public void modifyReserveMeeting (String meetingId, String meetingName, String startTime, String endTime, String[] inviteUsers) {
        ReserveMeetingInfo reserveMeetingInfo = new ReserveMeetingInfo();
        reserveMeetingInfo.setMeetingId(meetingId);
        reserveMeetingInfo.setMeetingName(meetingName);
        reserveMeetingInfo.setInvitedUsers(inviteUsers);
        reserveMeetingInfo.setStartTime(startTime);
        reserveMeetingInfo.setEndTime(endTime);
        ServerInteractManager.getInstance().updateMeeting(reserveMeetingInfo);
    }

    public void deleteReserveMeeting(String meetingId){
        DeleteInfo deleteReserveMeeting = new DeleteInfo();
        deleteReserveMeeting.setMeetingId(meetingId);
        ServerInteractManager.getInstance().deleteMeeting(deleteReserveMeeting);
    }

    public void sendMsg(String meetingId, String[] users) {
        SendInfo sendInfo = new SendInfo();
        sendInfo.setMeetingId(meetingId);
        sendInfo.setUsers(users);
        ServerInteractManager.getInstance().sendMessage(sendInfo);
    }

    public void syncPid (String meetingId) {
        SyncPidInfo syncPidInfo = new SyncPidInfo();
        syncPidInfo.setMeetingId(meetingId);
        ServerInteractManager.getInstance().syncPid(syncPidInfo);
    }

    public String getSyncMeetingById(String meetingId) {
        return ServerInteractManager.getInstance().getSyncGetMeeting(meetingId);
    }

    public String getSyncCurrentMeeting() {
        return ServerInteractManager.getInstance().getSyncCurrentMeeting();
    }

    public com.shgbit.hssdk.json.Meeting[] getDayofMeetings(int year, int month, int day){
        return JsonUtil.modelAconvertoB(MeetingCeche.getInstance().getDayOfMeetings(year, month, day),com.shgbit.hssdk.json.Meeting[].class);
    }

    public boolean checkHasMeeting (int year, int month, int day) {
        return MeetingCeche.getInstance().checkHasMeeting(year, month, day);
    }

    public  com.shgbit.hssdk.json.Meeting getMeetingById(String meetingId) {
        return  JsonUtil.modelAconvertoB(MeetingCeche.getInstance().getMeeting(meetingId),com.shgbit.hssdk.json.Meeting.class);
    }

    public void startRecord(String meetingId) {
        MeetingRecord meetingRecord = new MeetingRecord();
        meetingRecord.setMeetingId(meetingId);
        ServerInteractManager.getInstance().startRecord(meetingRecord);
    }

    public void endRecord(String meetingId) {
        MeetingRecord meetingRecord = new MeetingRecord();
        meetingRecord.setMeetingId(meetingId);
        ServerInteractManager.getInstance().endRecord(meetingRecord);
    }

    private ServerInteractCallback serverInteractCallback = new ServerInteractCallback(){
        @Override
        public void onLogin(boolean result, String error, User user) {
        if (callback != null) {
            callback.onLogin(result, error, JsonUtil.modelAconvertoB(user, com.shgbit.hssdk.json.User.class));
        }
        }

        @Override
        public void onLogout(boolean result, String error) {
            if (callback != null) {
                callback.onLogout(result, error);
            }
        }

        @Override
        public void onCheckPwd(boolean result, String error) {
            if (callback != null) {
                callback.onCheckPwd(result, error);
            }
        }

        @Override
        public void onMotifyPwd(boolean result, String error) {
            if (callback != null) {
                callback.onMotifyPwd(result, error);
            }
        }

        @Override
        public void onStartYunDesk(boolean result, String error, YunDesktop yunDesktop) {

        }

        @Override
        public void onEndYunDesk(boolean result, String error, YunDesktop yunDesktop) {

        }

        @Override
        public void onCreateMeeting(boolean result, String error, Meeting meeting) {
            if (instantMeetingListener != null) {
                instantMeetingListener.onCreateMeeting(result, error, JsonUtil.modelAconvertoB(meeting,com.shgbit.hssdk.json.Meeting.class));
            }
        }

        @Override
        public void onJoinMeeting(boolean result, String error) {
            if (callback != null) {
                callback.onJoinMeeting(result, error);
            }
        }

        @Override
        public void onInviteMeeting(boolean success, String error) {
            if (callback != null) {
                callback.onInviteMeeting(success, error);
            }
        }

        @Override
        public void onKickoutMeeting(boolean success, String error) {

        }

        @Override
        public void onQuiteMeeting(boolean success, String error) {
            if (callback != null) {
                callback.onQuiteMeeting(success, error);
            }
        }

        @Override
        public void onEndMeeting(boolean success, String error) {

        }

        @Override
        public void onReserveMeeting(boolean success, String error, Meeting meeting) {
            if (reserveMeetingListener != null) {
                reserveMeetingListener.onReserveMeeting(success, error, JsonUtil.modelAconvertoB(meeting,com.shgbit.hssdk.json.Meeting.class));
            }
        }

        @Override
        public void onDeleteMeeting(boolean success, String error) {
            if (callback != null) {
                callback.onDeleteMeeting(success, error);
            }
        }

        @Override
        public void onUpdateMeeting(boolean success, String error) {
            if (callback != null) {
                callback.onUpdateMeeting(success, error);
            }
        }

        @Override
        public void onBusyMeeting(boolean success, String error) {
            if (callback != null) {
                callback.onBusyMeeting(success, error);
            }
        }

        @Override
        public void onMeetings() {
            if (callback != null) {
                callback.onMeetings();
            }
        }

        @Override
        public void onValidate(boolean result, String err) {

        }

        @Override
        public void startRecord(boolean result, String err) {
            if (callback != null) {
                callback.startRecord(result, err);
            }
        }

        @Override
        public void endRecord(boolean result, String err) {
            if (callback != null) {
                callback.endRecord(result, err);
            }
        }

        @Override
        public void eventUserStateChanged(RefuseInfo[] refuseInfos, TimeoutInfo[] timeoutInfos) {
            if (callback != null) {
                callback.eventUserStateChanged(JsonUtil.modelAconvertoB(refuseInfos, com.shgbit.hssdk.json.RefuseInfo[].class), JsonUtil.modelAconvertoB(timeoutInfos, com.shgbit.hssdk.json.TimeoutInfo[].class));
            }
        }

        @Override
        public void eventInvitedMeeting(InvitedMeeting meeting) {
        if (callback != null) {
            callback.eventInvitedMeeting(JsonUtil.modelAconvertoB(meeting, com.shgbit.hssdk.json.InvitedMeeting.class));
        }
        }

        @Override
        public void eventStartYunDesk(YunDesktop yunDesktop) {

        }

        @Override
        public void eventEndYunDesk() {

        }

        @Override
        public void eventStartWhiteBoard() {

        }

        @Override
        public void eventEndWhiteBoard() {

        }

        @Override
        public void eventDifferentPlaceLogin() {
            if (callback != null) {
                callback.eventDifferentPlaceLogin();
            }
        }

        @Override
        public void eventInvitingCancle(InviteCancledInfo ici) {
        if (callback != null) {
            callback.eventInvitingCancle(JsonUtil.modelAconvertoB(ici, com.shgbit.hssdk.json.InviteCancledInfo.class));
        }
        }
    };

}
