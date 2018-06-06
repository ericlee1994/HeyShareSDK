package com.shgbit.hssdk.sdk;

import android.content.Context;

import com.shgbit.hshttplibrary.AddressCeche;
import com.shgbit.hshttplibrary.MeetingCeche;
import com.shgbit.hshttplibrary.ServerInteractManager;
import com.shgbit.hshttplibrary.callback.AddressUpdateCallback;
import com.shgbit.hshttplibrary.callback.ServerAddressCallback;
import com.shgbit.hshttplibrary.callback.ServerConfigCallback;
import com.shgbit.hshttplibrary.callback.ServerInteractCallback;
import com.shgbit.hshttplibrary.callback.ServerRecordCallback;
import com.shgbit.hshttplibrary.json.Group;
import com.shgbit.hshttplibrary.json.HotFixConfig;
import com.shgbit.hshttplibrary.json.InviteCancledInfo;
import com.shgbit.hshttplibrary.json.InvitedMeeting;
import com.shgbit.hshttplibrary.json.Meeting;
import com.shgbit.hshttplibrary.json.PushConfig;
import com.shgbit.hshttplibrary.json.RefuseInfo;
import com.shgbit.hshttplibrary.json.RootOrganization;
import com.shgbit.hshttplibrary.json.TimeoutInfo;
import com.shgbit.hshttplibrary.json.User;
import com.shgbit.hshttplibrary.json.UserOrganization;
import com.shgbit.hshttplibrary.json.XiaoYuConfig;
import com.shgbit.hshttplibrary.json.YunDesktop;
import com.shgbit.hssdk.callback.HeyshareAdrsCallback;
import com.shgbit.hssdk.callback.HeyshareRecordCallback;
import com.shgbit.hssdk.callback.InstantMeetingListener;
import com.shgbit.hssdk.callback.ReserveMeetingListener;
import com.shgbit.hssdk.callback.HeyshareCallback;

import java.util.ArrayList;

public class HeyShareSDK {
    private CommonCtrl commonCtrl;
    private VideoCtrl videoCtrl;

    private String userName;
    private Context mContext;

    private ReserveMeetingListener reserveMeetingListener;
    private InstantMeetingListener instantMeetingListener;

    private HeyshareCallback heyshareCallback;
    private HeyshareAdrsCallback heyshareAdrsCallback;
    private HeyshareRecordCallback heyshareRecordCallback;

    private static HeyShareSDK sInstance = null;

    public static HeyShareSDK getInstance() {
        if (sInstance == null) {
            sInstance = new HeyShareSDK();
        }
        return sInstance;
    }

    public void setHeyshareCallback (HeyshareCallback callback) {
        this.heyshareCallback = callback;
    }

    public void setHeyshareAdrsCallback (HeyshareAdrsCallback callback) {
        this.heyshareAdrsCallback = callback;
    }

    public void setHeyshareRecordCallback (HeyshareRecordCallback callback) {
        this.heyshareRecordCallback = callback;
    }

    public void init(Context context, String IP){
        mContext = context;

        commonCtrl = new CommonCtrl();
        videoCtrl = new VideoCtrl();

        ServerInteractManager.getInstance().init(IP);
        ServerInteractManager.getInstance().setServerConfigCallback(mConfigCallback);
        ServerInteractManager.getInstance().setServerInteractCallback(mInteractCallback);
        ServerInteractManager.getInstance().setServerAddressCallback(mAddressCallback);
        ServerInteractManager.getInstance().setServerRecordCallback(mRecordCallback);

        AddressCeche.getInstance().setDataUpdateListener(mAddressUpdateCallback);
    }

    public void finalize(){
        ServerInteractManager.getInstance().finalize();
        MeetingCeche.getInstance().finalize();
        AddressCeche.getInstance().finalize();
        VideoCtrl.getInstance().finalize();
        sInstance = null;
    }

    private CommonCtrl getCommonCtrl () {
        if (commonCtrl == null) {
            commonCtrl = new CommonCtrl();
        }
        return  commonCtrl;
    }

    public void login(String userName, String userPwd){
        this.userName = userName;
        AddressCeche.getInstance().init(userName);
        getCommonCtrl().login(userName, userPwd);
    }

    public void logout() {
        getCommonCtrl().logout();
    }

    public void checkPwd(String userName, String userPwd) {
        getCommonCtrl().checkPwd(userName, userPwd);
    }

    public void modifyPwd(String userName, String userPwd) {
        getCommonCtrl().modifyPwd(userName, userPwd);
    }

    public void createReserveMeeting(String meetingName, String startTime, String endTime, String[] inviteUsers, ReserveMeetingListener listener){
        this.reserveMeetingListener = listener;
        getCommonCtrl().createReserveMeeting(userName, meetingName, startTime, endTime, inviteUsers);
    }

    public void createInstantMeeting(String[] inviteUsers, InstantMeetingListener listener){
        this.instantMeetingListener = listener;
        getCommonCtrl().createInstantMeeting(userName, inviteUsers);
    }

    public void inviteUsers(String meetingId, String[] users){
        getCommonCtrl().inviteUsers(userName, meetingId, users);
    }

    public void cancelInviting(String meetingId, String invitedName){
        getCommonCtrl().cancelInviting(meetingId, invitedName);
    }

    public void refuseInviting(String meetingId, String inviter){
        getCommonCtrl().refuseInviting(userName, meetingId, inviter);
    }

    public void modifyReserveMeeting (String meetingId, String meetingName, String startTime, String endTime, String[] inviteUsers) {
        getCommonCtrl().modifyReserveMeeting(meetingId, meetingName, startTime, endTime, inviteUsers);
    }

    public void deleteReserveMeeting(String meetingId){
        getCommonCtrl().deleteReserveMeeting(meetingId);
    }

    public void sendMsg(String meetingId, String[] users) {
        getCommonCtrl().sendMsg(meetingId, users);
    }

    public void syncPid(String meetingId) {
        getCommonCtrl().syncPid(meetingId);
    }


    //获取会议相关
    public ArrayList<Meeting> getDayofMeetings(int year, int month, int day){
        return MeetingCeche.getInstance().getDayOfMeetings(year, month, day);
    }

    public boolean checkHasMeeting (int year, int month, int day) {
        return MeetingCeche.getInstance().checkHasMeeting(year, month, day);
    }

    public  Meeting getMeetingById(String meetingId) {
        return MeetingCeche.getInstance().getMeeting(meetingId);
    }

    public String getSyncMeetingById(String meetingId){
        return getCommonCtrl().getSyncMeetingById(meetingId);
    }

    public String getSyncCurrentMeeting() {
        return getCommonCtrl().getSyncCurrentMeeting();
    }


    //通讯录相关
    public void addFrequentContacts(String[] userNames) {
        getCommonCtrl().addFrequentContacts(userNames);
    }

    public void createGroup(String groupName, String[] menbers) {
        getCommonCtrl().createGroup(groupName, menbers);
    }

    public void deleteGroup (String groupId) {
        getCommonCtrl().deleteGroup(groupId);
    }

    public void updateGroupName(String groupId, String groupName) {
        getCommonCtrl().updateGroupName(groupId, groupName);
    }

    public void addMembersToGroup(String groupId, String[] menbers) {
        getCommonCtrl().addMembersToGroup(groupId, menbers);
    }

    public void deleteMembersFromGroup(String groupId, String[] menbers) {
        getCommonCtrl().deleteMembersFromGroup(groupId, menbers);
    }

    public String getContactJsonString(){
        return getCommonCtrl().getContactJsonString();
    }

    public ArrayList<Group> getGroupList() {
        return AddressCeche.getInstance().getGpList();
    }

    public RootOrganization getAddressData() {
        return AddressCeche.getInstance().getAddressData();
    }

    public ArrayList<UserOrganization> getFrequentContacts() {
        return AddressCeche.getInstance().getGeneralContacts();
    }

    public ArrayList<UserOrganization> getOrderedContacts() {
        return AddressCeche.getInstance().getOrderedContacts();
    }

    public ArrayList<UserOrganization> getGroupMenbers(String groupName) {
        return AddressCeche.getInstance().getGroupType(groupName);
    }

    //录音
    public void startRecord(String meetingId) {
        getCommonCtrl().startRecord(meetingId);
    }

    public void endRecord(String meetingId) {
        getCommonCtrl().endRecord(meetingId);
    }

    /**
     * video ctrl
     */

    public VideoCtrl video () {
        return VideoCtrl.getInstance();
    }

    //callbacks
    private ServerConfigCallback mConfigCallback = new ServerConfigCallback() {
        @Override
        public void configXiaoyu(XiaoYuConfig config) {
            VideoCtrl.getInstance().initNemo(mContext, config);
        }

        @Override
        public void configHotfix(HotFixConfig config) {

        }

        @Override
        public void configPush(PushConfig config) {

        }
    };

    private ServerInteractCallback mInteractCallback = new ServerInteractCallback() {
        @Override
        public void onLogin(boolean result, String error, User user) {
            if (heyshareCallback != null) {
                heyshareCallback.onLogin(result, error, user);
            }
        }

        @Override
        public void onLogout(boolean result, String error) {
            if (heyshareCallback != null) {
                heyshareCallback.onLogout(result, error);
            }
        }

        @Override
        public void onCheckPwd(boolean result, String error) {
            if (heyshareCallback != null) {
                heyshareCallback.onCheckPwd(result, error);
            }
        }

        @Override
        public void onMotifyPwd(boolean result, String error) {
            if (heyshareCallback != null) {
                heyshareCallback.onMotifyPwd(result, error);
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
                instantMeetingListener.onCreateMeeting(result, error, meeting);
            }
        }

        @Override
        public void onJoinMeeting(boolean result, String error) {
            if (heyshareCallback != null) {
                heyshareCallback.onJoinMeeting(result, error);
            }
        }

        @Override
        public void onInviteMeeting(boolean success, String error) {
            if (heyshareCallback != null) {
                heyshareCallback.onInviteMeeting(success, error);
            }
        }

        @Override
        public void onKickoutMeeting(boolean success, String error) {

        }

        @Override
        public void onQuiteMeeting(boolean success, String error) {
            if (heyshareCallback != null) {
                heyshareCallback.onQuiteMeeting(success, error);
            }
        }

        @Override
        public void onEndMeeting(boolean success, String error) {

        }

        @Override
        public void onReserveMeeting(boolean success, String error, Meeting meeting) {
            if (reserveMeetingListener != null) {
                reserveMeetingListener.onReserveMeeting(success, error, meeting);
            }
        }

        @Override
        public void onDeleteMeeting(boolean success, String error) {
            if (heyshareCallback != null) {
                heyshareCallback.onDeleteMeeting(success, error);
            }
        }

        @Override
        public void onUpdateMeeting(boolean success, String error) {
            if (heyshareCallback != null) {
                heyshareCallback.onUpdateMeeting(success, error);
            }
        }

        @Override
        public void onBusyMeeting(boolean success, String error) {
            if (heyshareCallback != null) {
                heyshareCallback.onBusyMeeting(success, error);
            }
        }

        @Override
        public void onMeetings() {
            if (heyshareCallback != null) {
                heyshareCallback.onMeetings();
            }
        }

        @Override
        public void onValidate(boolean result, String err) {

        }

        @Override
        public void eventUserStateChanged(RefuseInfo[] refuseInfos, TimeoutInfo[] timeoutInfos) {
            if (heyshareCallback != null) {
                heyshareCallback.eventUserStateChanged(refuseInfos, timeoutInfos);
            }
        }

        @Override
        public void eventInvitedMeeting(InvitedMeeting meeting) {
            if (heyshareCallback != null) {
                heyshareCallback.eventInvitedMeeting(meeting);
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
            if (heyshareCallback != null) {
                heyshareCallback.eventDifferentPlaceLogin();
            }
        }

        @Override
        public void eventInvitingCancle(InviteCancledInfo ici) {
            if (heyshareCallback != null) {
                heyshareCallback.eventInvitingCancle(ici);
            }
        }
    };

    private ServerAddressCallback mAddressCallback = new ServerAddressCallback() {

        @Override
        public void onPostContactUser(boolean success, String error) {
            if (heyshareAdrsCallback != null) {
                heyshareAdrsCallback.onPostContactUser(success, error);
            }
        }

        @Override
        public void onCreateGroup(boolean success, String error) {
            if (heyshareAdrsCallback != null) {
                heyshareAdrsCallback.onCreateGroup(success, error);
            }
        }

        @Override
        public void onDeleteGroup(boolean success, String error) {
            if (heyshareAdrsCallback != null) {
                heyshareAdrsCallback.onDeleteGroup(success, error);
            }
        }

        @Override
        public void onUpdateGroup(boolean success, String error) {
            if (heyshareAdrsCallback != null) {
                heyshareAdrsCallback.onUpdateGroup(success, error);
            }
        }

        @Override
        public void onAddToGroup(boolean success, String error) {
            if (heyshareAdrsCallback != null) {
                heyshareAdrsCallback.onAddToGroup(success, error);
            }
        }

        @Override
        public void onDeleFrmGroup(boolean success, String error) {
            if (heyshareAdrsCallback != null) {
                heyshareAdrsCallback.onDeleFrmGroup(success, error);
            }
        }
    };

    private ServerRecordCallback mRecordCallback = new ServerRecordCallback() {
        @Override
        public void startRecord(boolean result, String err) {
            if (heyshareRecordCallback != null) {
                heyshareRecordCallback.startRecord(result, err);
            }
        }

        @Override
        public void endRecord(boolean result, String err) {
            if (heyshareRecordCallback != null) {
                heyshareRecordCallback.endRecord(result, err);
            }
        }
    };

    private AddressUpdateCallback mAddressUpdateCallback = new AddressUpdateCallback() {
        @Override
        public void onDataUpdate() {
            if (heyshareAdrsCallback != null) {
                heyshareAdrsCallback.onDataUpdate();
            }
        }
    };
}
