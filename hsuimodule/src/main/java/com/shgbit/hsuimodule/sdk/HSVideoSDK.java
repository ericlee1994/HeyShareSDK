package com.shgbit.hsuimodule.sdk;

import android.content.Context;
import android.content.Intent;

import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import com.shgbit.hssdk.callback.HSSDKInstantListener;
import com.shgbit.hssdk.callback.HSSDKReserveListener;
import com.shgbit.hssdk.sdk.Common;
import com.shgbit.hssdk.sdk.HeyShareSDK;
import com.shgbit.hsuimodule.activity.OldVideoActivity;

public class HSVideoSDK {
    private static final String TAG = "HSVideoSDK";
    private String userName;
    private String settingNum;
    private String meetingId;
    private Context mContext;
    private static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();

//    private HSSDKListener sdkListener;
    private HSSDKReserveListener hssdkReserveListener;
    private HSSDKInstantListener hssdkInstantListener;
//    private HSSDKMeetingListener hssdkMeetingListener;
//    private HSSDKContactListener hssdkContactListener;
//    private Syntony syntony;

    private String[] inviteUsers;

    static boolean hasStartMeeting = false;
    private boolean hasInstantMeeting = false;
    private boolean hasLogin = false;
    private boolean isMainView = false;
    private boolean isVideoRecord = false;
    private boolean isMic = false;
    private boolean isVideo = true;
    private boolean enterMeeting = true;
    private boolean isInvite = false;
    private boolean isAudioMode = false;

    private HSVideoSDK() {
    }

    public static HSVideoSDK getInstance() {
        return Holder.mInstance;
    }

    private static class Holder {
        private static final HSVideoSDK mInstance = new HSVideoSDK();
    }

    public void init(String serverIP, Context context) {

        this.mContext = context.getApplicationContext();
        HeyShareSDK.getInstance().init(context, serverIP);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Common.SCREENHEIGHT = dm.widthPixels;
        Common.SCREENWIDTH = dm.heightPixels;

    }


//    public void setSDKListener(HSSDKListener hssdkListener){
//        Log.i(TAG, "setSDKListener");
//        this.sdkListener = hssdkListener;
//    }
//
//    public void setMeetingListener(HSSDKMeetingListener hssdkMeetingListener){
//        Log.i(TAG, "setMeetingListener");
//        this.hssdkMeetingListener = hssdkMeetingListener;
//    }
//
//    public void setContactListener(HSSDKContactListener hssdkContactListener){
//        Log.i(TAG, "setContactListener");
//        this.hssdkContactListener = hssdkContactListener;
//    }
//
//    public HSSDKContactListener getHssdkContactListener() {
//        Log.i(TAG, "getHssdkContactListener");
//        return hssdkContactListener;
//    }

    public void connect(String userName, String userPwd) {
        Log.i(TAG, "connect " + "userName:" + userName + "userPwd:" + userPwd);
        this.userName = userName;
        HeyShareSDK.getInstance().connect(userName, userPwd);

    }

    public void disconnect() {
        Log.i(TAG, "disconnect");
        HeyShareSDK.getInstance().disconnect();
    }

    public void startMeeting(String meetingNumber, String meetingPwd, String meetingName, String settingNum) {
        Log.i(TAG, "startMeeting");
        this.settingNum = settingNum;
        this.meetingId = meetingNumber;
        parseSettingNum(settingNum);

        if (meetingName == null || meetingName.equals("")) {
            meetingName = "默认会议";
        }

        Intent intent = new Intent(mContext, OldVideoActivity.class);

        intent.putExtra("username", userName);
        intent.putExtra("password", meetingPwd);
        intent.putExtra("meetingName", meetingName);
        intent.putExtra("number", meetingNumber);
        intent.putExtra("mainView", isMainView);
        intent.putExtra("videoRecord", isVideoRecord);
        intent.putExtra("isMic", !isMic);
        intent.putExtra("isVideo", !isVideo);
        intent.putExtra("isAudioMode", isAudioMode);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

        hasStartMeeting = true;

    }

    public void finalizeSDK() {
        Log.i(TAG, "finalizeSDK");
        HeyShareSDK.getInstance().finalizeSDK();
    }

    public void refuseMeeting(String meetingId, String userName, String inviter) {
        Log.i(TAG, "refuseMeeting");
        HeyShareSDK.getInstance().refuseJoinMeeting(meetingId, inviter);
    }


//    public void openAddress(Context context, int type, int layoutId, int layoutId2, AddressCallBack addressCallBack) {
//        Log.i(TAG, "openAddress");
//        if (type == 1) {//即时
//            Syntony syntony = new Syntony();
//            syntony.init(context, layoutId, layoutId2, userName);
//            syntony.startAddressList(true, "vertical", false, null, null);
//            syntony.setExCallBack(addressCallBack);
//        } else if (type == 2) {//预约
//            Syntony syntony = new Syntony();
//            syntony.init(context, layoutId, layoutId2, userName);
//            syntony.startAddressList(true, "vertical", true, null, null);
//            syntony.setExCallBack(addressCallBack);
//        } else {//通讯录
//            Syntony syntony = new Syntony();
//            syntony.init(context, layoutId, layoutId2, userName);
//            syntony.startAddressList(false, "vertical", false, null, null);
//            syntony.setExCallBack(addressCallBack);
//        }
//
//    }

    public void startInstantMeeting(String createUser, String[] inviteUsers, String settingNum, boolean enterMeeting, HSSDKInstantListener hssdkInstantListener) {
        Log.i(TAG, "startInstantMeeting," + "settingNum" + settingNum + "," + "enterMeeting:" + enterMeeting);
        this.settingNum = settingNum;
        this.enterMeeting = enterMeeting;
        this.hssdkInstantListener = hssdkInstantListener;
//        CreateMeetingInfo createMeetingInfo = new CreateMeetingInfo();
//        createMeetingInfo.setCreatedUser(createUser);
//        createMeetingInfo.setInvitedUsers(inviteUsers);
//        ServerInteractManager.getInstance().createMeeting(createMeetingInfo);
        hasInstantMeeting = true;
        HeyShareSDK.getInstance().createInstantMeeting(inviteUsers, enterMeeting, hssdkInstantListener);
    }

    public void createReservationMeeting(String createUser, String meetingName, String startTime, String endTime, String[] inviteUsers, HSSDKReserveListener hssdkReserveListener){
        Log.i(TAG, "createReservationMeeting");
        this.hssdkReserveListener = hssdkReserveListener;
//        ReserveInfo reserveInfo = new ReserveInfo();
//        reserveInfo.setCreatedUser(createUser);
//        reserveInfo.setMeetingName(meetingName);
//        reserveInfo.setStartTime(startTime);
//        reserveInfo.setEndTime(endTime);
//        reserveInfo.setInvitedUsers(inviteUsers);
//        ServerInteractManager.getInstance().reserveMeeting(reserveInfo);
        HeyShareSDK.getInstance().createReserveMeeting(meetingName, startTime, endTime, inviteUsers, hssdkReserveListener);
    }

//    public void sendMessage (String meetingId, String[] users) {
//        Log.i(TAG, "sendMessage");
//        SendInfo sendInfo = new SendInfo();
//        sendInfo.setMeetingId(meetingId);
//        sendInfo.setUsers(users);
//        ServerInteractManager.getInstance().sendMessage(sendInfo);
//
//    }
//
//    public Meeting getMeeting (String meetingId) {
//        Log.i(TAG, "getMeeting");
//        return MeetingCeche.getInstance().getMeeting(meetingId);
//    }
//
//    public ArrayList<Meeting> getDayOfMeetings (int year, int month, int day) {
//        Log.i(TAG, "getDayOfMeetings");
//        return MeetingCeche.getInstance().getDayOfMeetings(year,month,day);
//    }
//
//    public boolean checkHasMeeting (int year, int month, int day) {
//        Log.i(TAG, "checkHasMeeting");
//        return MeetingCeche.getInstance().checkHasMeeting(year, month, day);
//    }
//
//    public String getMeetingInfo(String meetingId){
//        Log.i(TAG, "getMeetingInfo");
//        return ServerInteractManager.getInstance().getSyncGetMeeting(meetingId);
//    }
//
//    public void modifyReserveMeeting (ReserveMeetingInfo reserveMeetingInfo) {
//        Log.i(TAG, "modifyReserveMeeting");
//        ServerInteractManager.getInstance().updateMeeting(reserveMeetingInfo);
//    }
//
//    public void deleteReserveMeeting (String meetingId) {
//        Log.i(TAG, "deleteReserveMeeting");
//        HeyShareSDK.getInstance().deleteReserveMeeting(meetingId);
//    }
//
//    public void inviteUsers (String meetingId, String inviter, String[] users) {
//        Log.i(TAG, "inviteUsers");
//        InviteMeetingInfo inviteMeetingInfo = new InviteMeetingInfo();
//        inviteMeetingInfo.setMeetingId(meetingId);
//        inviteMeetingInfo.setUserName(inviter);
//        inviteMeetingInfo.setInvitedUsers(users);
//        ServerInteractManager.getInstance().inviteMeeting(inviteMeetingInfo);
//    }
//
//    public void inviteUsers(String[] users) {
//        Log.i(TAG, "inviteUsers");
//        inviteUsers = users;
//        InviteMeetingInfo inviteMeetingInfo = new InviteMeetingInfo();
//        inviteMeetingInfo.setMeetingId(meetingId);
//        inviteMeetingInfo.setUserName(userName);
//        inviteMeetingInfo.setInvitedUsers(users);
//        ServerInteractManager.getInstance().inviteMeeting(inviteMeetingInfo);
//        isInvite = true;
//    }
//
//    public void checkPWD (String userName, String pwd) {
//        Log.i(TAG, "checkPWD");
//        LoginInfo loginInfo = new LoginInfo();
//        loginInfo.setUserName(userName);
//        loginInfo.setPassword(pwd);
//        ServerInteractManager.getInstance().checkPwd(loginInfo);
//    }
//
//    public void modifyPWD (String userName, String pwd) {
//        Log.i(TAG, "modifyPWD");
//        LoginInfo loginInfo = new LoginInfo();
//        loginInfo.setUserName(userName);
//        loginInfo.setPassword(pwd);
//        ServerInteractManager.getInstance().motifyPwd(loginInfo);
//    }
//
//    public void cancelInvite(String meetingId, String invitedName){
//        Log.i(TAG, "cancelInvite");
//        CancelInviteInfo cancelInviteInfo = new CancelInviteInfo();
//        cancelInviteInfo.setMeetingId(meetingId);
//        cancelInviteInfo.setInvitedUser(invitedName);
//        ServerInteractManager.getInstance().cancelMeeting(cancelInviteInfo);
//    }
//
//    public void endMeeting(String meetingId, String inviteName){
//        Log.i(TAG, "endMeeting");
//        QuiteMeetingInfo quiteMeetingInfo = new QuiteMeetingInfo();
//        quiteMeetingInfo.setMeetingId(meetingId);
//        quiteMeetingInfo.setUserName(inviteName);
//        ServerInteractManager.getInstance().quiteMeeting(quiteMeetingInfo);
//
//    }
//
//    public String getContactJson(){
//        Log.i(TAG, "getContactJson");
//        return  ServerInteractManager.getInstance().getContactResult();
//    }

    private void parseSettingNum(String settingNum) {
        if (settingNum == null) {
            return;
        } else {
            char[] simpleNums = settingNum.toCharArray();

            for (int i = 0; i < simpleNums.length; i++) {
                if (i == 0) {
                    if (simpleNums[0] == '1') {
                        isMainView = true;
                    }else {
                        isMainView = false;
                    }
                }else if (i == 1) {
                    if (simpleNums[1] == '1'){
                        isVideoRecord = true;
                    }else {
                        isVideoRecord = false;
                    }
                }else if (i == 2) {
                    if (simpleNums[2] == '1') {
                        isMic = true;
                    } else {
                        isMic = false;
                    }
                }else if (i == 3) {
                    if (simpleNums[3] == '0') {
                        isVideo = false;
                    }else {
                        isVideo = true;
                    }
                }else if (i == 4) {
                    if (simpleNums[4] == '1') {
                        isAudioMode = true;
                    }else {
                        isAudioMode = false;
                    }
                }
            }

        }
        Log.e(TAG, "isMainView:" + isMainView + ", isVideoRecord:" + isVideoRecord + ", isMic:" + isMic  + ", isVideo:" + isVideo);
    }

    private void writeLog(Context context){

//        try {
//            String pathName = SDCARD_DIR + "/HSSDK/" + context.getPackageName() + "/log";
//            File dir = new File(pathName);
//            if (!dir.exists()){
//                dir.mkdirs();
//            }
//
//            GBLog.filepath = SDCARD_DIR + "/HSSDK/" + context.getPackageName() + "/log";
//            WALog.filepath = GBLog.filepath;
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }

//    private void checkID() {
//        try {
//            ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(),
//                    PackageManager.GET_META_DATA);
//            String id = appInfo.metaData.getString("HS_APP_ID");
//            if (id == null && sdkListener != null) {
//                sdkListener.initState(false);
//                Log.e(TAG, "APP_ID:null");
//            }
//            ValidateInfo validateInfo = new ValidateInfo();
//            validateInfo.setPkgId(mContext.getPackageName());
//            validateInfo.setAppId(id);
//            Log.e(TAG, "pkgId:" + mContext.getPackageName() + ",appId:" + id);
//            ServerInteractManager.getInstance().validate(validateInfo);
//        }catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//
//    }

//    private ServerInteractCallback serverInteractCallback = new ServerInteractCallback() {
//        @Override
//        public void onLogin(boolean result, String error, User user) {
//            if (sdkListener != null) {
//                sdkListener.connectState(result, user);
//                hasLogin = true;
//            }else {
//                hasLogin = false;
//                Log.e(TAG, "onLogin: sdkListener = null" );
//            }
//        }
//
//        @Override
//        public void onLogout(boolean result, String error) {
//            if (sdkListener != null) {
//                sdkListener.disconnectState(result, error);
//                hasLogin = false;
//            }else {
//                Log.e(TAG, "onLogout: sdkListener = null");
//            }
//        }
//
//        @Override
//        public void onCheckPwd(boolean result, String error) {
//            if (hssdkMeetingListener != null) {
//                hssdkMeetingListener.onCheckPwd(result, error);
//            }else {
//                Log.e(TAG, "onCheckPwd: hssdkMeetingListener = null");
//            }
//        }
//
//        @Override
//        public void onMotifyPwd(boolean result, String error) {
//            if (hssdkMeetingListener != null) {
//                hssdkMeetingListener.onMotifyPwd(result, error);
//            }else {
//                Log.e(TAG, "onMotifyPwd: hssdkMeetingListener = null");
//            }
//        }
//
//        @Override
//        public void onStartYunDesk(boolean result, String error, YunDesktop yunDesktop) {
//
//        }
//
//        @Override
//        public void onEndYunDesk(boolean result, String error, YunDesktop yunDesktop) {
//
//        }
//
//        @Override
//        public void onCreateMeeting(boolean result, String error, Meeting meeting) {
//
//            if (hssdkInstantListener != null) {
//                hssdkInstantListener.onCreateMeetng(result, error, meeting);
//
//                if (result && hasInstantMeeting && enterMeeting) {
//                    startMeeting(meeting.getMeetingId(), meeting.getPassword(), "即时会议", settingNum);
//                } else {
//                    Log.e(TAG, "onCreateMeeting: false," + error);
//                }
//                hasInstantMeeting = false;
//            }else {
//                Log.e(TAG, "onCreateMeeting: hssdkInstantListener = null");
//            }
//        }
//
//        @Override
//        public void onJoinMeeting(boolean result, String error) {
//
//        }
//
//        @Override
//        public void onInviteMeeting(boolean success, String error) {
//            if (success && isInvite){
//                if (inviteUsers != null) {
//                    MeetingInfoManager.getInstance().StateChange(inviteUsers, STATUS.INVITING);
//                    inviteUsers = null;
//                    isInvite = false;
//                }
//            }
//        }
//
//        @Override
//        public void onKickoutMeeting(boolean success, String error) {
//
//        }
//
//        @Override
//        public void onQuiteMeeting(boolean success, String error) {
//
//        }
//
//        @Override
//        public void onEndMeeting(boolean success, String error) {
//
//        }
//
//        @Override
//        public void onReserveMeeting(boolean success, String error, Meeting meeting) {
//            if (hssdkReserveListener != null) {
//                hssdkReserveListener.onReserveMeeting(success, error, meeting);
//            }
//
//        }
//
//        @Override
//        public void onDeleteMeeting(boolean success, String error) {
//            if (hssdkMeetingListener != null) {
//                hssdkMeetingListener.onDeleteMeeting(success, error);
//            }
//        }
//
//        @Override
//        public void onUpdateMeeting(boolean success, String error) {
//            if (hssdkMeetingListener != null) {
//                hssdkMeetingListener.onUpdateMeeting(success, error);
//            }
//        }
//
//        @Override
//        public void onBusyMeeting(boolean success, String error) {
//
//        }
//
//        @Override
//        public void onMeetings() {
//            if (hssdkMeetingListener != null) {
//                hssdkMeetingListener.onMeetings();
//            }
//        }
//
//        @Override
//        public void eventUserStateChanged(RefuseInfo[] refuseInfos, TimeoutInfo[] timeoutInfos) {
//            hssdkMeetingListener.eventUserStateChanged(refuseInfos, timeoutInfos);
//        }
//
//        @Override
//        public void eventInvitedMeeting(InvitedMeeting meeting) {
//            if (!hasStartMeeting && hssdkMeetingListener !=null) {
//                hssdkMeetingListener.inviteMeeting(meeting);
//            }else {
//                if (hssdkMeetingListener == null) {
//                    Log.e(TAG, "eventInvitedMeeting: hssdkMeetingListener = null");
//                }
//            }
//        }
//
//        @Override
//        public void eventStartYunDesk(YunDesktop yunDesktop) {
//
//        }
//
//        @Override
//        public void eventEndYunDesk() {
//
//        }
//
//        @Override
//        public void eventStartWhiteBoard() {
//
//        }
//
//        @Override
//        public void eventEndWhiteBoard() {
//
//        }
//
//        @Override
//        public void eventDifferentPlaceLogin() {
//            if (hssdkMeetingListener != null) {
//                hssdkMeetingListener.eventDifferentPlaceLogin();
//            }
//        }
//
//        @Override
//        public void eventInvitingCancle(InviteCancledInfo ici) {
//            if (hssdkMeetingListener != null) {
//                hssdkMeetingListener.eventInvitingCancle(ici);
//            }
//        }
//
//        @Override
//        public void onValidate(boolean result, String err) {
//            if (result) {
//                Log.e(TAG, "onValidate success");
//
//            }else {
//                if (sdkListener != null) {
//                    sdkListener.initState(false);
//                    Log.e(TAG, "onValidate:" + err);
//                }else {
//                    Log.e(TAG, "onValidate: sdkListener = null");
//                }
//            }
//        }
//    };
}
