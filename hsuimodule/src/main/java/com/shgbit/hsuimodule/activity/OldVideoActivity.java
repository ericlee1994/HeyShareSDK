package com.shgbit.hsuimodule.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ainemo.sdk.otf.NemoSDK;
import com.shgbit.hshttplibrary.json.InvitedMeeting;
import com.shgbit.hshttplibrary.json.MeetingRecord;
import com.shgbit.hssdk.bean.LoginData;
import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hssdk.bean.STATUS;
import com.shgbit.hssdk.bean.VI;
import com.shgbit.hssdk.bean.VideoCallState;
import com.shgbit.hssdk.callback.HSXYLoginCallback;
import com.shgbit.hssdk.callback.VideoListCallback;
import com.shgbit.hssdk.callback.VideoStateCallback;
import com.shgbit.hssdk.sdk.Common;
import com.shgbit.hssdk.sdk.HeyShareSDK;
import com.shgbit.hssdk.sdk.Reason;
import com.shgbit.hsuimodule.R;
import com.shgbit.hsuimodule.bean.CallMeetingInfo;
import com.shgbit.hsuimodule.bean.DISCONNECT_STATE;
import com.shgbit.hsuimodule.bean.DisplayModeEnum;
import com.shgbit.hsuimodule.callback.IModeCallBack;
import com.shgbit.hsuimodule.callback.IPopViewCallBack;
import com.shgbit.hsuimodule.callback.ITitleCallBack;
import com.shgbit.hsuimodule.callback.IVideoViewCallBack;
import com.shgbit.hsuimodule.widget.ModeDialog;
import com.shgbit.hsuimodule.widget.MyVideoVIew;
import com.shgbit.hsuimodule.widget.PopupOldView;
import com.shgbit.hsuimodule.widget.TitleLayout;
import com.shgbit.hsuimodule.widget.VCDialog;
import com.shgbit.hsuimodule.widget.VideoRecord;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.view.Window.FEATURE_NO_TITLE;

public class OldVideoActivity extends BaseActivity implements IPopViewCallBack {

    private static final String TAG = "VideoActivity";

    private static final int CONNECT_NEMO_ERROR = 0x001;
    private static final int DIALOGINVITED = 0x003;
    private static final int HANGUP = 0x004;
    private static final int UPDATAVIEW = 0x005;
    private static final int AUDIOMODE = 0x006;
    private static final int TOAST = 0x007;
    private static final int CLOSEMIC = 0x008;
    private static final int CALL_NEMO_ERROR = 0x009;
    private static final int RE_MAKECALL = 0x011;
    private static final int HANGUP_NEMO_ERROR = 0x012;
    private static final int MEETING_TIME = 0x013;
    private static final int MAKE_CALL = 0x014;
    private static final int MESSAGE_MEMBER = 0x015;
    private static final int SHOW_HIDE_LAYOUT = 0x017;
    private static final int HIDE_HIDE_LAYOUY = 0x018;
    private static final int HIDE_OTHER_TITLE = 0x019;
    private static final int UPDATE_POPDATA = 0x020;
    private static final int NOCALLBACK_TIME = 8;
    private static final int ENTER_COMMENT = 0x021;
    private static final int EXIT_COMMENT = 0x022;
    private static final int PICUPLOADSTATE = 0x023;
    private static final int SENDCOMMENT = 0x024;
    private static final int CLEANPZ = 0x025;
    private static final int ClOSEVIDEO = 0x026;
    private static final int MSG_BTN_SWITCH_MIC = 0x100;
    private static final int MSG_BTN_SWITCH_VIDEO = 0x200;
    private static final int MSG_BTN_VOICE_MODE = 0x300;
    private static final int MSG_BTN_SWITCH_CAMERA = 0x400;
    private static final int MSG_BTN_VOICE_MODE_UNCHANGED = 0x500;

    public static CallMeetingInfo mRecallMeeting;

    private boolean foregroundCamera = true;
    private boolean muteCamera = false;
    private boolean muteMic = true;
    private boolean isHangup = false;
    private boolean isGetTime = false;
    private boolean isInPIP = false;
    private boolean isModeChange = false;
    private boolean hasContent = false;
    private boolean autoContent = true;
    private boolean audioMode = false; //true 语音，false 视频
    private boolean openPic = false;
    private boolean inComment = false;
    private boolean hasComment = false;
    private boolean clickPizhu = false;
    private boolean picState = false;
    private boolean hasEndMqtt = false;
    private boolean isVideoRecord = false;
    private boolean isMainView = false;

    private Context mContext;
    private VCDialog mDialog;
    private ModeDialog modeDialog;
    private MyVideoVIew videoView;
    private DISCONNECT_STATE mExitState;

    private FrameLayout frameLayout;
    private RelativeLayout mWholeLayout;
    private TitleLayout mTopLayout;
    private LinearLayout mBottomLayout;
    private PopupOldView popupView;
    private DisplayModeEnum lastMode;
    private DisplayModeEnum lastModeFlag;

    private ArrayList<MemberInfo> mScreenList;
    private ArrayList<MemberInfo> mOtherList;
    private ArrayList<MemberInfo> mUnjoinedList;
    private String mHangUpWarn;
    private String[] mUsers;
    private DisplayModeEnum contentlastmode = DisplayModeEnum.NOT_FULL_ONEFIVE;

    private int cameraId = 1;
    private int memberSum;
    private int joinSum;

    private boolean isUvcCamera;
    private int currentCamera = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "VideoActivity onCreate");
        isHangup = false;
        muteMic = Common.ISMUTEMIC;
        audioMode = Common.ISAUDIOMODE;
        mExitState = DISCONNECT_STATE.NORMAL;
        mContext = this;

        getIntentInfo();
        connectNemo(Common.USERNAME, Common.PASSWORD);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_video);
        initView();

        HeyShareSDK.getInstance().setVideoListCallback(videoListCallback);
        HeyShareSDK.getInstance().setVideoStateCallback(videoStateCallback);

    }

    public void getIntentInfo(){
        Intent intentInfo = getIntent();
        final String number = intentInfo.getStringExtra("number");
        String password = intentInfo.getStringExtra("password");
        String meetingName = intentInfo.getStringExtra("meetingName");
        String username = intentInfo.getStringExtra("username");
        isMainView = intentInfo.getBooleanExtra("mainView", false);
        isVideoRecord = intentInfo.getBooleanExtra("videoRecord", false);
        muteMic = intentInfo.getBooleanExtra("isMic", true);
        muteCamera = intentInfo.getBooleanExtra("isVideo", false);
        audioMode = intentInfo.getBooleanExtra("isAudioMode", false);

        Common.USERNAME = username;
        Common.PASSWORD = password;
        Common.isNemoConnected = false;

        mRecallMeeting = new CallMeetingInfo();
        mRecallMeeting.setId(number);
        mRecallMeeting.setPw(password);
        mRecallMeeting.setName(meetingName);
    }

    private void connectNemo(String number, String password) {
        try {
            if (!Common.isNemoConnected) {
                if (mUIHandler != null) {
                    Message msg = mUIHandler.obtainMessage();
                    msg.what = CONNECT_NEMO_ERROR;
                    msg.obj = "连接视频服务器无响应！(n: no response!)";
                    mUIHandler.sendMessageDelayed(msg, NOCALLBACK_TIME * 1000);
                }
                Log.i(TAG, "try to connect Nemo...");
//                nemoSDK.connectNemo(SystemParams.getUsername() + "?t_mobile", SystemParams.getUsername() + "?t_mobile", mConnectNemoCallback);
//                nemoSDK.loginExternalAccount(Common.USERNAME + "?t_mobile", Common.USERNAME + "?t_mobile", mConnectNemoCallback);
                HeyShareSDK.getInstance().loginXY(hsxyLoginCallback);


            } else {
//                joinMeeting(number, password);
                HeyShareSDK.getInstance().joinMeeting(number, password);
            }
        } catch (Exception e) {
            Log.e(TAG, "connect Throwable:" + e.toString());
        }
    }

    private void initView() {

        frameLayout = findViewById(R.id.video_fragment);
        mWholeLayout = findViewById(R.id.whole_layout);
        lastMode = DisplayModeEnum.FULL_FOUR;
        lastModeFlag = DisplayModeEnum.NOT_FULL_ONEFIVE;
        changeMode(DisplayModeEnum.NOT_FULL_ONEFIVE);
    }

    @Override
    public void onClickPopBtn() {
        mUIHandler.sendEmptyMessage(HIDE_HIDE_LAYOUY);
    }

    @Override
    public void onClickMenuBtn(String type) {
        clickMenuBtn(type);
    }

    @Override
    public void onClickPerson(MemberInfo memberInfo) {
        if (memberInfo.getStatus().equals(STATUS.JOINED)) {
//            MeetingInfoManager.getInstance().PopUpDown(memberInfo);
            HeyShareSDK.getInstance().popUpDown(memberInfo);
        } else {
            String[] user = {memberInfo.getId()};
            reInvited(user);
//            MeetingInfoManager.getInstance().StateChange(user, STATUS.INVITING);
            HeyShareSDK.getInstance().stateChange(user, STATUS.INVITING);
        }
    }
    public static void reInvited(String[] users) {
        if (mRecallMeeting != null) {
            HeyShareSDK.getInstance().inviteUsers(mRecallMeeting.getId(), users);
        }
    }

    private static class UIHandler extends Handler {
        private final WeakReference<OldVideoActivity> mVideoActivity;

        public UIHandler(OldVideoActivity videoActivity){
            mVideoActivity = new WeakReference<>(videoActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            OldVideoActivity activity = mVideoActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case CONNECT_NEMO_ERROR:
                        activity.showDialog(msg.obj, VCDialog.DialogType.Normal);
                        break;
                    case DIALOGINVITED:
                        InvitedMeeting invitedMeeting = (InvitedMeeting) msg.obj;
                        activity.showDialog(invitedMeeting, VCDialog.DialogType.Invite);
                        break;
                    case CLOSEMIC:
                        Log.e(TAG, "CLOSEMIC");
                        activity.muteMic = true;
//                        activity.nemoSDK.enableMic(activity.muteMic,true);
                        HeyShareSDK.getInstance().audioMute(activity.muteMic);
                        activity.videoView.closeLocalMic(activity.muteMic);
                        if (activity.muteMic){
                            Toast.makeText(activity, R.string.tips_45, Toast.LENGTH_SHORT).show();
                        }
//                        MQTTClient.getInstance().publishMsg(TOPIC.CTRL_DEVICE + mRecallMeeting, activity.getLocalCtrlStatus());
                        break;
                    case ClOSEVIDEO:
                        Log.e(TAG, "ClOSEVIDEO");
                        activity.muteCamera = true;
//                        activity.nemoSDK.setVideoMute(activity.muteCamera);
                        HeyShareSDK.getInstance().videoMute(activity.muteCamera);
                        activity.videoView.closeLocalView(activity.muteCamera);
                        break;
                    case MSG_BTN_SWITCH_MIC:
                        Log.e(TAG, "MSG_BTN_SWITCH_MIC");
                        activity.muteMic = !activity.muteMic;
                        activity.videoView.closeLocalMic(activity.muteMic);
//                        activity.nemoSDK.enableMic(activity.muteMic, true);
                        HeyShareSDK.getInstance().audioMute(activity.muteMic);
                        break;
                    case MSG_BTN_SWITCH_VIDEO:
                        Log.e(TAG, "MSG_BTN_SWITCH_VIDEO");
                        activity.muteCamera = !activity.muteCamera;
                        activity.videoView.closeLocalView(activity.muteCamera);
//                        activity.nemoSDK.setVideoMute(activity.muteCamera);
                        HeyShareSDK.getInstance().videoMute(activity.muteCamera);
                        break;
                    case MSG_BTN_SWITCH_CAMERA:
                        Log.e(TAG, "MSG_BTN_SWITCH_CAMERA");

                        if (activity.cameraId != 0 && activity.cameraId != 1){
                            activity.foregroundCamera = ! activity.foregroundCamera;
                            activity.cameraId = activity.foregroundCamera ? 1 : 0;
                        }else {
                            activity.cameraId = (int) msg.obj;
                        }

//                        activity.nemoSDK.switchCamera(activity.cameraId);
                        HeyShareSDK.getInstance().switchCamera(activity.cameraId);

                        break;
                    case MSG_BTN_VOICE_MODE:
                        Log.e(TAG, "MSG_BTN_VOICE_MODE");
                        activity.audioMode = !activity.audioMode;
//                        activity.nemoSDK.switchCallMode(activity.audioMode);
                        HeyShareSDK.getInstance().audioMode(activity.audioMode);

//                        MeetingInfoManager.getInstance().ModeVoice(activity.audioMode);


                        if (!activity.audioMode) {
                            activity.videoView.closeLocalView(activity.muteCamera);
//                            activity.nemoSDK.setVideoMute(activity.muteCamera);
                            HeyShareSDK.getInstance().videoMute(activity.muteCamera);
                        }
                        break;
                    case MSG_BTN_VOICE_MODE_UNCHANGED:
//                        activity.nemoSDK.switchCallMode(activity.audioMode);
                        HeyShareSDK.getInstance().audioMode(activity.audioMode);
                        HeyShareSDK.getInstance().setVoiceMode(activity.audioMode);
//                        MeetingInfoManager.getInstance().ModeVoice(activity.audioMode);
                        break;
                    case TOAST:
                        Toast.makeText(activity, activity.getString(R.string.tips_19), Toast.LENGTH_LONG).show();
                        break;
                    case CALL_NEMO_ERROR:
                        activity.showDialog(msg.obj, VCDialog.DialogType.ReStart);
                        break;
                    case HANGUP_NEMO_ERROR:
                        activity.showDialog(msg.obj, VCDialog.DialogType.ErrorHangup);
                        break;

                    case MAKE_CALL:
                        activity.reCall();
                        break;

                    case MEETING_TIME:
                        activity.mTopLayout.setmClockView((Integer) msg.obj);
                        break;

                    case MESSAGE_MEMBER:
                        activity.mTopLayout.setmTitleTxt(mRecallMeeting.getName(), (String) msg.obj);
                        break;

                    case SHOW_HIDE_LAYOUT:
                        activity.mUIHandler.removeMessages(SHOW_HIDE_LAYOUT);
                        activity.mUIHandler.removeMessages(HIDE_HIDE_LAYOUY);
                        activity.mBottomLayout.setVisibility(View.INVISIBLE);
                        activity.popupView.setVisibility(View.VISIBLE);
//                        activity.popupView.showPopupView();
                        activity.mUIHandler.sendEmptyMessageDelayed(HIDE_HIDE_LAYOUY, 5000);
                        break;

                    case HIDE_HIDE_LAYOUY:
                        activity.dismissPopup();
                        activity.mBottomLayout.setVisibility(View.VISIBLE);
                        break;

                    case HIDE_OTHER_TITLE:

                        break;

                    case UPDATE_POPDATA:

                        if (activity.popupView != null) {
                            activity.popupView.setMeetingUserData(activity.mScreenList, activity.mOtherList, activity.mUnjoinedList);
                        }
                        break;

                    case HANGUP:
//                    showDialog(getString(R.string.tip_22), VCDialog.DialogType.Handup);
                        if (activity.popupView != null && activity.popupView.getRecordingStatus()) {
                            activity.mHangUpWarn = activity.getString(R.string.tips_41);
                        } else {
                            activity.mHangUpWarn = activity.getString(R.string.tips_22);
                        }
                        activity.showDialog(activity.mHangUpWarn, VCDialog.DialogType.Handup);
//                    showHangupDialog(getString(R.string.tip_22));
                        break;

                    case UPDATAVIEW:
                        if (activity.hasContent) {
                            if (activity.autoContent) {
                                if (!activity.lastMode.equals(DisplayModeEnum.FULL_PIP_SIX)) {
                                    activity.contentlastmode = activity.lastMode;
                                }
                                activity.changeMode(DisplayModeEnum.FULL_PIP_SIX);
                                activity.isInPIP = true;
                            }
                        } else {
                            if (activity.isInPIP) {
                                activity.changeMode(activity.contentlastmode);
                                activity.isInPIP = false;
                            }
                            activity.autoContent = true;
                        }

                        if (activity.lastMode.equals(DisplayModeEnum.FULL) && activity.mScreenList.size() == 0) {
                            activity.changeMode(DisplayModeEnum.NOT_FULL_ONEFIVE);
                        }
                        break;

//                    case ENTER_COMMENT:
//                        if (activity.mainView == null) {
//                            activity.mainView = new CustomPaintView(activity,
//                                    activity.clientId, false);
//                            if (TRACKS != null) {
//                                activity.mainView.followList(TRACKS);
//                                TRACKS = null;
//                            }
//                            activity.mainView.setCallBack(activity.iMainViewCallBack);
//                            activity.videoView.setPizhu(activity.mainView);
//                        }
//                        if (activity.clickPizhu) {
//                            if (activity.mainView != null) {
//                                activity.mainView.setDraw(true);
//                            }
//
//                        } else {
//                            if (activity.mainView != null) {
//                                activity.mainView.setDraw(false);
//                            }
//                        }
//                        break;
//
//                    case EXIT_COMMENT:
//                        activity.clickPizhu = false;
//                        activity.changeMode(DISPLAY_MODE.NOT_FULL_ONEFIVE);
//                        if (activity.mainView != null) {
//                            activity.mainView.setDraw(false);
//                        }
//
//                        if (activity.hasEndMqtt == true && activity.videoView != null && activity.mainView != null) {
//                            activity.mainView.Finalize();
//                            activity.mainView = null;
//                            activity.hasEndMqtt = false;
//                        }
//                        break;

//                    case PICUPLOADSTATE:
//                        if (activity.picState) {
//                            Toast.makeText(activity, "图片上传成功！", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(activity, "图片上传失败！", Toast.LENGTH_SHORT).show();
//                        }
//                        break;

//                    case SENDCOMMENT:
//                        if (activity.mainView != null) {
//                            activity.mainView.Synchronize(msg.obj.toString());
//                        }
//                        break;

//                    case CLEANPZ:
//                        activity.showDialog("确认清空批注？", VCDialog.DialogType.CleanPZ);
//                        break;
                    default:
                        break;
                }
            }
        }
    }

    private final UIHandler mUIHandler = new UIHandler(this);

    private void showDialog(Object content, VCDialog.DialogType type) {

        try {

            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }

            mDialog = new VCDialog(this, R.style.Dialog, type);
            mDialog.setContent(content);
            mDialog.setDialogCallback(mDialogCallback);
            mDialog.setCanceledOnTouchOutside(false);

            mDialog.show();
        } catch (Throwable e) {
            Log.e(TAG, "showDialog Throwable:" + e.toString());
        }
    }

    private void reCall() {
        try {

            HeyShareSDK.getInstance().resetMIManager();

            mExitState = DISCONNECT_STATE.NORMAL;

            mTopLayout.setmConferenceIDTxt(mRecallMeeting.getId());
            videoView.closeLocalView(false);

            if (!Common.isNemoConnected) {
                if (mUIHandler != null) {
                    Log.i(TAG, "reCall connect mUIHandler send");
                    Message msg = mUIHandler.obtainMessage();
                    msg.what = CONNECT_NEMO_ERROR;
                    msg.obj = "连接视频服务器无响应！";
                    mUIHandler.sendMessageDelayed(msg, NOCALLBACK_TIME * 1000);
                }
//                nemoSDK.loginExternalAccount(Common.USERNAME, Common.USERNAME, mConnectNemoCallback);
                HeyShareSDK.getInstance().loginXY(hsxyLoginCallback);
            } else {
                Log.e(TAG, "recall make call");
                makeCall();
            }
        } catch (Exception e) {
            Log.e(TAG, "reCall Throwable:" + e.toString());
        }
    }

    HSXYLoginCallback hsxyLoginCallback = new HSXYLoginCallback() {
        @Override
        public void onFailed(String info) {
            Log.e(TAG, "hsxyLoginCallback onFailed:" + info);
        }

        @Override
        public void onSuccess(LoginData loginData, boolean b) {
            mUIHandler.removeMessages(CONNECT_NEMO_ERROR);
            HeyShareSDK.getInstance().joinMeeting(mRecallMeeting.getId(), mRecallMeeting.getPw());
        }
    };

    private void makeCall() {
        Log.i(TAG, "After joining meeting, then try to make call");
        isHangup = false;
        try {

            if (mUIHandler != null) {
                Message msg = mUIHandler.obtainMessage();
                msg.what = CALL_NEMO_ERROR;
                msg.obj = "系统出错，请重启软件(n:make call failed!)";
                mUIHandler.sendMessageDelayed(msg, NOCALLBACK_TIME * 1000);
            }
            Log.i(TAG, "nemo make call...." + "number : " + mRecallMeeting.getId() + "password" + mRecallMeeting.getPw());
//            nemoSDK.makeCall(mRecallMeeting.getId(), mRecallMeeting.getPw());
            HeyShareSDK.getInstance().joinMeeting(mRecallMeeting.getPw(), mRecallMeeting.getPw());
        } catch (Exception e) {
            Log.e(TAG, "makeCall Throwable:" + e.toString());
        }
    }

    private void dismissPopup() {
        if (popupView != null) {
            popupView.setVisibility(View.INVISIBLE);
            popupView.dismissPopupView();
        }
    }

    private void changeMode(DisplayModeEnum displayModeEnum) {
        Log.i(TAG, "change mode to " + displayModeEnum.toString());
        if (lastMode.equals(displayModeEnum)) {
            return;
        }

//        lastModeFlag = lastMode;
        isModeChange = true;
        mWholeLayout.removeAllViews();
        if (videoView != null) {
            videoView.destroy();
            videoView.removeAllViews();
            videoView = null;
        }
        if (displayModeEnum.equals(DisplayModeEnum.NOT_FULL_ONEFIVE)
                || displayModeEnum.equals(DisplayModeEnum.NOT_FULL_QUARTER)) {
            mTopLayout = new TitleLayout(this);
            mBottomLayout = new LinearLayout(this);
            popupView = new PopupOldView(this, muteMic, muteCamera, audioMode);
            videoView = new MyVideoVIew(this, displayModeEnum);


            dismissPopup();
            mWholeLayout.addView(mTopLayout);
            mWholeLayout.addView(videoView);
            mWholeLayout.addView(popupView);
            mWholeLayout.addView(mBottomLayout);

            videoView.setiVideoViewCallBack(mVideoViewListener);
            mTopLayout.setITitleCallBack(mTitleListener);
            RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT / 10);
            mTopLayout.setLayoutParams(topParams);
            mTopLayout.setOrientation(LinearLayout.HORIZONTAL);

            RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT * 5 / 6);
            videoParams.topMargin = Common.SCREENHEIGHT / 10;
            videoView.setLayoutParams(videoParams);

            RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT / 20);
            bottomParams.topMargin = Common.SCREENHEIGHT * 19 / 20;
            mBottomLayout.setLayoutParams(bottomParams);
            mBottomLayout.setBackgroundResource(R.drawable.btn_arrow_up);
            mBottomLayout.setOnClickListener(mClickListener);

            RelativeLayout.LayoutParams popParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT);
            popupView.setLayoutParams(popParams);

        } else if (displayModeEnum.equals(DisplayModeEnum.FULL_PIP)) {
            videoView = new MyVideoVIew(this, displayModeEnum);
            videoView.setiVideoViewCallBack(mVideoViewListener);
            RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT);
            videoView.setLayoutParams(videoParams);
            mWholeLayout.addView(videoView);
        } else if (displayModeEnum.equals(DisplayModeEnum.FULL)) {
            videoView = new MyVideoVIew(this, displayModeEnum);
            videoView.setiVideoViewCallBack(mVideoViewListener);
            RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            videoView.setLayoutParams(videoParams);
            mWholeLayout.addView(videoView);
        } else if (displayModeEnum.equals(DisplayModeEnum.FULL_PIP_SIX)) {
            videoView = new MyVideoVIew(this, displayModeEnum);
            popupView = new PopupOldView(this, muteMic, muteCamera, audioMode);
            mBottomLayout = new LinearLayout(this);
            dismissPopup();
            videoView.setiVideoViewCallBack(mVideoViewListener);

            mWholeLayout.addView(videoView);
            mWholeLayout.addView(popupView);
            mWholeLayout.addView(mBottomLayout);

            RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT);
            videoView.setLayoutParams(videoParams);

            RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT / 20);
            bottomParams.topMargin = Common.SCREENHEIGHT * 19 / 20;

            mBottomLayout.setLayoutParams(bottomParams);
            mBottomLayout.setBackgroundResource(R.drawable.btn_arrow_up);
            mBottomLayout.setOnClickListener(mClickListener);

            RelativeLayout.LayoutParams popParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT);
            popupView.setLayoutParams(popParams);

        }
        lastMode = displayModeEnum;
        videoView.requestLocalFrame();
//        if (mainView != null) {
//            videoView.setPizhu(mainView);
//        }
    }

    private IVideoViewCallBack mVideoViewListener = new IVideoViewCallBack() {
        @Override
        public void backToDefaultMode(VI vi0, VI vi1) {
//                if (!isSelfExit) {
//                    autoContent = false;
//                }
            if (lastMode.equals(DisplayModeEnum.NOT_FULL_ONEFIVE)) {
                lastModeFlag = DisplayModeEnum.NOT_FULL_ONEFIVE;
                changeMode(DisplayModeEnum.FULL);

            } else if (lastMode.equals(DisplayModeEnum.NOT_FULL_QUARTER)) {
                lastModeFlag = DisplayModeEnum.NOT_FULL_QUARTER;
//                MeetingInfoManager.getInstance().ScreenExchange(vi0, vi1);
                HeyShareSDK.getInstance().exchangeScreen(vi0, vi1);
                changeMode(DisplayModeEnum.FULL);
            }
//                else if (lastMode.equals(DISPLAY_MODE.FULL_PIP_SIX)) {
//                    isInPIP = false;
//                    changeMode(lastModeFlag);
//                }
            else if (lastMode.equals(DisplayModeEnum.FULL)) {
//                    if (lastModeFlag.equals(DISPLAY_MODE.FULL_PIP_SIX)) {
//                        changeMode(DISPLAY_MODE.NOT_FULL_ONEFIVE);
//                    } else {
                changeMode(lastModeFlag);
//                    }
//                if (clickPizhu) {
//                    requestCtrl .postCmtLeave(curResource);
//                    mUIHandler.sendEmptyMessage(EXIT_COMMENT);
//                    clickPizhu = false;
//                }
            }
        }

        @Override
        public void closePic() {
            openPic = false;
//            MeetingInfoManager.getInstance().PicShare(false, null);
        }

        @Override
        public void switchPosition() {

        }

    };

    private ITitleCallBack mTitleListener = new ITitleCallBack() {
        @Override
        public void clickBackBtn() {
            mUIHandler.sendEmptyMessage(HANGUP);
        }
    };

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mUIHandler.sendEmptyMessage(SHOW_HIDE_LAYOUT);
            popupView.setMeetingUserData(mScreenList, mOtherList, mUnjoinedList);

        }
    };

    private VCDialog.DialogCallback mDialogCallback = new VCDialog.DialogCallback() {

        @Override
        public void onOk(VCDialog.DialogType type, Object object) {
            Log.i(TAG, "Dialog OK");
            try {
                if (popupView != null) {
                    popupView.StopVideotape();
                }
                if (type == VCDialog.DialogType.Invite) {

                    InvitedMeeting m = (InvitedMeeting) object;

                    mExitState = DISCONNECT_STATE.RECALL;

                    if (m != null && m.getMeetingId() != null && m.getPassword() != null) {

                        mRecallMeeting = new CallMeetingInfo();
                        mRecallMeeting.setId(m.getMeetingId());
                        mRecallMeeting.setPw(m.getPassword());
                        mRecallMeeting.setName(m.getMeetingName());

                        hangup();

                    }

                } else if (type == VCDialog.DialogType.Handup) {
                    hangup();
                    Finish();

                } else if (type == VCDialog.DialogType.Normal) {
                    Finish();

                } else if (type == VCDialog.DialogType.ErrorHangup) {
//                    quitMeeting();
                    Finish();

                } else if (type == VCDialog.DialogType.Recall) {
                    hangup();

                } else if (type == VCDialog.DialogType.ReStart) {
                    hangup();
//                    Intent intent = new Intent();
//                    intent.setClass(VideoActivity.this, SplashActivity.class);
//                    VideoActivity.this.startActivity(intent);
//
//                    VideoActivity.this.finish();
//                    VCApplication.getInstance().exit();
//
//                } else if (type == VCDialog.DialogType.CleanPZ) {
//                    mainView.clearStroke();
                }
            } catch (Throwable e) {
                Log.e(TAG, "onOk Throwable: " + e.toString());
            }
        }

        @Override
        public void onCancel(VCDialog.DialogType type, Object object) {
            Log.i(TAG, "Dialog Cancel");
            try {
                if (type == VCDialog.DialogType.Invite) {
//                    BusyMeetingInfo bmi = new BusyMeetingInfo();
//                    bmi.setMeetingId(((InvitedMeeting) object).getMeetingId());
//                    bmi.setInviter(((InvitedMeeting) object).getInviter());
//                    bmi.setUserName(Common.USERNAME);
//                    vcApplication.getInteractManager().busyMeeting(bmi);
//                    ServerInteractManager.getInstance().busyMeeting(bmi);
                    HeyShareSDK.getInstance().refuseJoinMeeting(((InvitedMeeting) object).getMeetingId(),((InvitedMeeting) object).getInviter());
                } else if (type == VCDialog.DialogType.Normal) {
                    if (popupView != null) {
                        popupView.StopVideotape();
                    }
                    Finish();
                } else if (type == VCDialog.DialogType.ErrorHangup || type == VCDialog.DialogType.Recall) {
                    if (popupView != null) {
                        popupView.StopVideotape();
                    }
                    hangup();
                } else {

                }
            } catch (Throwable e) {
                Log.e(TAG, "onCancel Throwable:" + e.toString());
            }
        }
    };
    public void hangup() {
        Log.i(TAG, "hangup and quitMeeting!");
        isHangup = true;
        isGetTime = false;
        if (mUIHandler != null) {
            Message msg = mUIHandler.obtainMessage();
            msg.what = HANGUP_NEMO_ERROR;
            msg.obj = "请重新加入会议！";
            mUIHandler.sendMessageDelayed(msg, NOCALLBACK_TIME * 1000);
        }
//        if (hasComment) {
//            requestCtrl.postCmtLeave(curResource);
//        }
        if (mUnjoinedList != null) {
            for (int i = 0; i < mUnjoinedList.size(); i++) {
                if (mUnjoinedList.get(i).getStatus().equals(STATUS.INVITING)) {
                    for (int j = 0; j < mUsers.length; j++) {
                        if (mUnjoinedList.get(i).getId().equals(mUsers[j])) {
                            String[] user1 = {mUnjoinedList.get(i).getId()};
//                                MeetingInfoManager.getInstance().StateChange(user1, STATUS.WAITING);
                            HeyShareSDK.getInstance().stateChange(user1, STATUS.WAITING);
                            HeyShareSDK.getInstance().cancelInvite(mRecallMeeting.getId(), mUnjoinedList.get(i).getId());
                            Log.i(TAG, "SIM:cancel invite:" + mUnjoinedList.get(i).getId());
                        }
                    }
                }
            }
        }

        HeyShareSDK.getInstance().quitMeeting();
    }

    private void Finish() {
        try {
//            nemoSDK.switchCamera(1);
            HeyShareSDK.getInstance().switchCamera(1);
            mTopLayout.finishClock();
            mDialog = null;
            modeDialog = null;

//            nemoSDK.setNemoSDKListener(null);
//            nemoSDK.setNemoKickOutListener(null);

            videoView.setOnClickListener(null);
//            ServerInteractManager.getInstance().removeServerInteractCallback(this);

            mUIHandler.removeCallbacksAndMessages(null);
            HeyShareSDK.getInstance().destroyMIManager();

//            MQTTClient.release();

//            if (mUVCCamera != null) {
//                mUVCCamera.stopPreview();
//            }
//            releaseCamera();
//            synchronized (mSync) {
//
//                if (mUSBMonitor != null) {
//                    mUSBMonitor.unregister();
//                    GBLog.e(TAG, "mUSBMonitor.unregister()");
//                    mUSBMonitor.destroy();
//                    mUSBMonitor = null;
//                }
//            }
//            mUVCCameraView = null;

            videoView.destroy();
            popupView.destroy();
//            syntony.destroy();
//            syntony = null;

            finish();
        } catch (Throwable e) {
            Log.e(TAG, "Finish Throwable:" + e.toString());
        }
    }

    private void autoSwitchCamera() {
        if(this.muteCamera)
        {
            return;
        }

//        if(isUvcCamera)
//        {
//            switch (currentCamera)
//            {
//                case 0:
//                    currentCamera = 1;
//                    releaseCamera();
//                    videoView.updateCamera(false);
//                    NemoSDK.getInstance().updateCamera(false);
//                    videoView.getmLocalVideoCell().notifyRender();
//                    videoView.requestLocalFrame();
//                    switchPhoneCamera(currentCamera);
//                    break;
//                case 1:
//
//                    isUvcCamera = true;
//                    currentCamera = 2;
//                    videoView.updateCamera(true);
//                    NemoSDK.getInstance().updateCamera(true);
////                    videoView.getmLocalVideoCell().releaseGxt();
//                    onDialogResult(true);
//                    break;
//                case 2:
//
//                    releaseCamera();
//                    currentCamera = 0;
//                    videoView.updateCamera(false);
//                    NemoSDK.getInstance().updateCamera(false);
//                    videoView.getmLocalVideoCell().notifyRender();
//                    videoView.requestLocalFrame();
//                    switchPhoneCamera(currentCamera);
//                    break;
//                default:
//                    break;
//            }
//
//        }
//        else{
            foregroundCamera = !foregroundCamera;
            cameraId = foregroundCamera ? 1 : 0;
            switchPhoneCamera(cameraId);
//        }
    }

    private void switchPhoneCamera(int cameraId)
    {
        if(cameraId != 0 && cameraId !=1)
        {
            foregroundCamera = !foregroundCamera;
            this.cameraId = foregroundCamera ? 1 : 0;
        }
        else{
            this.cameraId = cameraId;
        }

//        nemoSDK.switchCamera(this.cameraId);  // 0：后置 1：前置
        HeyShareSDK.getInstance().switchCamera(this.cameraId);
    }

    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), 0);
        }
    }

    final String BTN_SHOW_PERSON = "btn_show_person";
    final String BTN_ADD_PERSON = "btn_add_person";
    final String BTN_BAN_MIC = "btn_ban_mic";
    final String BTN_CLOSE_CAMERA = "btn_voice_mode";
    final String BTN_VOICE_MODE = "btn_audio_mode";
    final String BTN_SWITCH_CAMERA = "btn_switch_camera";
    final String BTN_DISPLAY_MODE = "btn_display_mode";
    final String BTN_HANGUP = "btn_hangup";
    final String BTN_UPLOAD_PIC = "btn_uploadpic";
    final String BTN_OPEN_PIC = "btn_openpic";
    final String BTN_PIZHU = "btn_pizhu";

    private void clickMenuBtn(String type) {
        if (BTN_SHOW_PERSON.equals(type)) {
            mUIHandler.removeMessages(HIDE_HIDE_LAYOUY);
        } else {
            if (BTN_ADD_PERSON.equals(type)) {
//                Log.i(TAG, "[user operation]click add person");
//                if (hssdkContactListener != null) {
//                    boolean flag = hssdkContactListener.contact(mContext, R.id.video_fragment);
//                    if (!flag){
//                        syntony.startAddressList(true, "horizontal", false, null, null);
//                        syntony.setExCallBack(videoCallBack);
//                    }
//                }else {
//                    syntony.startAddressList(true, "horizontal", false, null, null);
//                    syntony.setExCallBack(videoCallBack);
//                }
            } else if (BTN_BAN_MIC.equals(type)) {
                Log.e(TAG, "[user operation]click muteMic btn:" + muteMic);
                mUIHandler.sendEmptyMessage(MSG_BTN_SWITCH_MIC);
            } else if (BTN_CLOSE_CAMERA.equals(type)) {
                Log.i(TAG, "[user operation]click off camera");
                mUIHandler.sendEmptyMessage(MSG_BTN_SWITCH_VIDEO);
            } else if (BTN_VOICE_MODE.equals(type)) {
                Log.i(TAG, "[user operation]click into Voice Mode");
                mUIHandler.sendEmptyMessage(MSG_BTN_VOICE_MODE);
            } else if (BTN_SWITCH_CAMERA.equals(type)) {
                Log.i(TAG, "[user operation]click switch camera");
//                mUIHandler.sendEmptyMessage(MSG_BTN_SWITCH_CAMERA);
                autoSwitchCamera();
            } else if (BTN_DISPLAY_MODE.equals(type)) {
                Log.i(TAG, "[user operation]click display mode");
                showModeDialog();
            } else if (BTN_HANGUP.equals(type)) {
                Log.i(TAG, "[user operation]click hangup btn");
                mUIHandler.sendEmptyMessage(HANGUP);
//                requestCtrl.postCmtLeave(curResource);
            } else if (BTN_UPLOAD_PIC.equals(type)) {
                Log.i(TAG, "[user operation]click upload pic btn");
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            } else if (BTN_OPEN_PIC.equals(type)) {
//                Log.i(TAG, "[user operation]click open pic btn");
//                openPic = !openPic;
//                if (openPic) {
//                    requestCtrl.downloadPic();
//                } else {
//                    MeetingInfoManager.getInstance().PicShare(false, null);
//                }
            } else if (BTN_PIZHU.equals(type)) {
//                Log.i(TAG, "[user operation]click pizhu btn");
//                if (mScreenList.size() > 0 && mScreenList.get(0) != null) {
//
//                    clickPizhu = true;
//                    String codeName = codeName(mScreenList);
//                    for (int i = 0; i < mScreenList.size(); i++) {
//                        if ((hasComment && curResource.equals(mScreenList.get(i).getResId())) || !hasComment){
//                            if (hasComment) {
//                                MeetingInfoManager.getInstance().CommetModeChange();
//                            }
//                            if (lastMode.equals(DISPLAY_MODE.NOT_FULL_ONEFIVE)) {
//                                lastModeFlag = DISPLAY_MODE.NOT_FULL_ONEFIVE;
//                                changeMode(DISPLAY_MODE.FULL);
//
//                            } else if (lastMode.equals(DISPLAY_MODE.NOT_FULL_QUARTER)) {
//                                lastModeFlag = DISPLAY_MODE.NOT_FULL_QUARTER;
//                                changeMode(DISPLAY_MODE.FULL);
//                            }
//                            if (hasComment) {
//                                mUIHandler.sendEmptyMessage(ENTER_COMMENT);
//                            }
//                            Log.e(TAG, "share resource :" + codeName(mScreenList));
//                            requestCtrl.postCmtEnter(codeName);
//                            break;
//                        }
//                    }
//                }
            }
            mUIHandler.sendEmptyMessage(SHOW_HIDE_LAYOUT);
        }
    }

    private void showModeDialog() {

        modeDialog = new ModeDialog(this, R.style.ModeSwitchDialog, true);
        modeDialog.setIModeCallBack(iModeCallBack);
        modeDialog.show();
    }

    private IModeCallBack iModeCallBack = new IModeCallBack() {
        @Override
        public void getDisplayMode(DisplayModeEnum displayModeEnum) {

            if (!displayModeEnum.equals(DisplayModeEnum.FULL_PIP_SIX)) {
                autoContent = false;
                isInPIP = false;
            }
            changeMode(displayModeEnum);
        }
    };

    private VideoListCallback videoListCallback = new VideoListCallback() {
        @Override
        public void onMemberChanged(ArrayList<MemberInfo> mScreen, ArrayList<MemberInfo> mOther, ArrayList<MemberInfo> mUnjoined, int duration, boolean isMemberContent, String meetingName) {
            for (int i = 0; i < mScreen.size(); i++) {
                Log.e(TAG, "onMemberChanged isLocal["+ i +"]" + mScreen.get(i).isLocal());
                Log.e(TAG, "onMemberChanged isMic[" + i + "]" + mScreen.get(i).isAudioMute());
                Log.e(TAG, "onMemberChanged isUvc[" + i + "]" + mScreen.get(i).isUvc());
                Log.e(TAG, "onMemberChanged getDataSourceID["+ i +"]" + mScreen.get(i).getDataSourceID());
                Log.e(TAG, "onMemberChanged getDisplayName["+ i +"]" + mScreen.get(i).getDisplayName());
                Log.e(TAG, "onMemberChanged getDisplayName["+ i +"]" + mScreen.get(i).getNet_status());
            }
            if (mScreen != null && mScreen.size() > 0) {
                if (!mScreen.get(0).getDataSourceID().equalsIgnoreCase(NemoSDK.getLocalVideoStreamID())) {
//                    nemoSDK.focusVideoStream(mScreen.get(0).getDataSourceID());
//                    nemoSDK.forceLayout(mScreen.get(0).getParticipantId());
                    HeyShareSDK.getInstance().forceLayout(mScreen.get(0).getParticipantId());
                }
            }

            if (videoView != null) {
                videoView.updateViewList(mScreen, mOther);
            }
            if (mScreenList == null) {
                mScreenList = new ArrayList<>();
            }
            mScreenList.clear();
            for (int i = 0; i < mScreen.size(); i++) {
                mScreenList.add(new MemberInfo(mScreen.get(i)));
            }

            if (mOtherList == null) {
                mOtherList = new ArrayList<>();
            }
            mOtherList.clear();
            for (int i = 0; i < mOther.size(); i++) {
                mOtherList.add(new MemberInfo(mOther.get(i)));
            }

            if (mUnjoinedList == null) {
                mUnjoinedList = new ArrayList<>();
            }
            mUnjoinedList.clear();
            for (int i = 0; i < mUnjoined.size(); i++) {
                mUnjoinedList.add(new MemberInfo(mUnjoined.get(i)));
            }

            for (int i = 0; i < mScreenList.size(); i++) {
                Log.i(TAG, "ScreenList name:" + mScreenList.get(i).getDisplayName());
            }

            if (!isGetTime || isModeChange) {
                Message msg = Message.obtain(mUIHandler, MEETING_TIME, duration);
                msg.sendToTarget();
                isGetTime = true;
            }


            hasContent = isMemberContent;

            mUIHandler.sendEmptyMessage(UPDATE_POPDATA);
            mUIHandler.sendEmptyMessage(UPDATAVIEW);

//            if (isFirstRegisterUVC) {
//                mUSBMonitor = new USBMonitor(mContext, mOnDeviceConnectListener);
//                mUSBMonitor.register();
//                isFirstRegisterUVC = false;
//                mUVCCameraView = videoView.getmLocalVideoCell();
//            }
        }

        @Override
        public void onMemberSizeChanged(int joinedSize, int memberSize) {
            Log.i(TAG, "onMemberSizeChanged, joinedSize:" + joinedSize + ", memberSize" + memberSize);
            memberSum = 0;
            memberSum = memberSize;
            joinSum = 0;
            joinSum = joinedSize;
            String tMember = joinSum + "/" + memberSum;

            Message msg = Message.obtain(mUIHandler, MESSAGE_MEMBER, tMember);
            msg.sendToTarget();
        }

        @Override
        public void onMemberCommentExit(MemberInfo mExit) {

        }
    };

    VideoStateCallback videoStateCallback = new VideoStateCallback() {
        @Override
        public void onCallFailed(String info) {
            Log.e(TAG, "onCallFailed:" + info);
        }

        @Override
        public void onCallStateChange(VideoCallState videoCallState, final String s) {
            Observable.just(videoCallState).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<VideoCallState>() {
                        @Override
                        public void accept(VideoCallState callState) {
                            switch (callState) {
                                case CONNECTING:
                                    mUIHandler.removeMessages(CALL_NEMO_ERROR);
                                    mUIHandler.removeMessages(RE_MAKECALL);
                                    hideSoftKeyboard();
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                    mUIHandler.sendEmptyMessage(MSG_BTN_VOICE_MODE_UNCHANGED);
                                    HeyShareSDK.getInstance().stateChange(mUsers, STATUS.INVITING);

//                                    MeetingInfoManager.getInstance().NemoChange(null, NemoSDK.getLocalVideoStreamID());
//                                    MeetingInfoManager.getInstance().StateChange(mUsers, STATUS.INVITING);

                                    Log.i(TAG, "Nemo call is connecting :" + s);
                                    break;
                                case CONNECTED:
                                    Log.i(TAG, "Nemo call has connected" + s);
                                    mUIHandler.sendEmptyMessage(AUDIOMODE);
                                    mUIHandler.removeMessages(CALL_NEMO_ERROR);
                                    if (muteMic == true) {
                                        mUIHandler.sendEmptyMessage(CLOSEMIC);
                                    }
                                    if (muteCamera == true && audioMode == false) {
                                        mUIHandler.sendEmptyMessage(ClOSEVIDEO);
                                    }

                                    if (isVideoRecord) {
                                        Log.e(TAG, "Video record at connected");
                                        MeetingRecord meetingRecord = new MeetingRecord();
                                        meetingRecord.setMeetingId(mRecallMeeting.getId());
                                        VideoRecord.getInstance(mContext).startRecord(meetingRecord);
                                    }

                                    videoView.requestLocalFrame();
//                                    HeyShareSDK.getInstance().videoMute();

//                                    nemoSDK.requestCamera();
//                                    isFirstReceive = true;
//                                    requestCtrl.getCmtStatus();
//                                    Runnable runnable = new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            GBLog.e(TAG, nemoSDK.getStatisticsInfo());
//                                            parseData(nemoSDK.getStatisticsInfo());
//                                            mUIHandler.postDelayed(this, 5000);
//                                        }
//                                    };
//                                    mUIHandler.postDelayed(runnable, 5000);
//                                    SyncPidInfo syncPidInfo = new SyncPidInfo();
//                                    syncPidInfo.setMeetingId(mRecallMeeting.getId());
//                                    vcApplication.getInteractManager().syncPid(syncPidInfo);

//                                    MQTTClient.getInstance().publishMsg(TOPIC.CTRL_DEVICE + mRecallMeeting.getId(), getLocalCtrlStatus());
                                    break;
                                case DISCONNECTED:
                                    Log.i(TAG, "Nemo call has disconnected :" + s);
                                    mUIHandler.removeMessages(CALL_NEMO_ERROR);
                                    mUIHandler.removeMessages(HANGUP_NEMO_ERROR);

                                    videoView.updateViewList(new ArrayList<MemberInfo>(), new ArrayList<MemberInfo>());

                                    if (!isHangup && s != null) {
                                        if ("status_ok".equalsIgnoreCase(s)) {
//                                            quitMeeting();
                                        } else {
                                            Message msg = Message.obtain(mUIHandler, HANGUP_NEMO_ERROR, s == null ? "连接断开！(n:unknow)" : Reason.getReason(s));
                                            Log.e(TAG, "disconnect" + Reason.getReason(s));
                                            msg.sendToTarget();
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
        }
    };

}
