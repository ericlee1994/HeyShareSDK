package com.shgbit.hssdk.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.ainemo.sdk.otf.ConnectNemoCallback;
import com.ainemo.sdk.otf.LoginResponseData;
import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.NemoSDKErrorCode;
import com.ainemo.sdk.otf.NemoSDKListener;
import com.ainemo.sdk.otf.RosterWrapper;
import com.ainemo.sdk.otf.Settings;
import com.ainemo.sdk.otf.VideoInfo;
import com.shgbit.hshttplibrary.json.XiaoYuConfig;
import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hssdk.bean.Status;
import com.shgbit.hssdk.callback.VideoUpdateListener;
import com.shgbit.hssdk.callback.VideoStateCallback;
import com.shgbit.hssdk.manager.MeetingInfoManager;

import java.util.ArrayList;
import java.util.List;

public class VideoCtrl {
    private final String TAG = "VideoCtrl";

    private static final Object lock = new Object();
    private static volatile VideoCtrl mInstance;

    public enum VideoCallState {CONNECTING, CONNECTED, DISCONNECTED}

    private VideoStateCallback mCallback;

    private VideoCtrl(){}

    public static void registerInstance() {
        if (mInstance == null) {
            mInstance = new VideoCtrl();
        }
        HeyShareSDK.setVideoCtrl(mInstance);
    }

//    private static VideoCtrl getInstance () {
//        if (mInstance == null) {
//            mInstance = new VideoCtrl();
//        }
//        return mInstance;
//    }

    protected void initNemo (Context context, XiaoYuConfig config) {
        Log.i(TAG, "nemosdk init starting");
        if (config == null) {
            Log.e(TAG, "nemosdk init failed: xiaoyu config is null!");
            return;
        }

        if (config.getServiceUrl() == null || config.getEnterpriseId() == null) {
            Log.e(TAG, "nemosdk init failed: xiaoyu config serviceurl is null!");
            return;
        }

        if (config.getEnterpriseId() == null || config.getEnterpriseId() == null) {
            Log.e(TAG, "nemosdk init failed: xiaoyu config enterpriseId is null!");
            return;
        }

        String url = config.getServiceUrl();
        String id = config.getEnterpriseId();
        if (url.startsWith("https://")) {
            url = url.replace("https://", "");
        } else if (url.startsWith("http://")) {
            url = url.replace("http://", "");
        }

        Settings settings = new Settings(id);
        settings.setPrivateCloudAddress(url);
        NemoSDK.getInstance().init(context, settings);

        Log.i(TAG, "nemosdk init end");
    }

    protected void shutdownNemo () {
        NemoSDK.getInstance().shutdown();
        if (mInstance != null){
            mInstance = null;
        }
    }

	public void setVideoStatusCallback (VideoStateCallback callback) {
		this.mCallback = callback;
	}

    public void init (String userName){
		NemoSDK.getInstance().setNemoSDKListener(mNemoSDKListener);
		meeting().setMeetingInfoUpdateListener(mVideoUpdateListener);
		meeting().init(userName);
	}

	public void finalize (){
		NemoSDK.getInstance().setNemoSDKListener(null);
		meeting().setMeetingInfoUpdateListener(null);
		meeting().finalize();
	}

    public void connect (String userName, String displayName) {
        NemoSDK.getInstance().loginExternalAccount(userName, displayName, mConnectNemoCallback);
    }

    public void resetMeeting(){
		meeting().reset();
	}

    public void callMeeting (String meetingId, String password) {
        NemoSDK.getInstance().makeCall(meetingId, password);
    }

    public void handupMeeting () {
        NemoSDK.getInstance().hangup();
    }

	public void saveNetMode(boolean isSaveNetMode){
		NemoSDK.getInstance().setSaveNetMode(isSaveNetMode);
	}

    public void switchCamera (int camera) {
        NemoSDK.getInstance().switchCamera(camera);
    }

    public void forceLayout (int id) {
        NemoSDK.getInstance().forceLayout(id);
    }

    public void screenExchange(MemberInfo m1, MemberInfo m2){
        meeting().screenExchange(m1, m2);
    }

    public void stateChange(String[] users, Status state){
        meeting().stateChange(users, state);
    }

    public void modeChange (int screenNumber) {
        meeting().modeChange(screenNumber);
    }

    public void popDown(MemberInfo m){
        meeting().popDown(m);
    }

    public void popUpDown(MemberInfo m){
        meeting().popUpDown(m);
    }

    public void audioMute (boolean mute) {
        NemoSDK.getInstance().enableMic(mute, true);
        meeting().audioMute(mute);
    }

    public void videoMute (boolean mute) {
        NemoSDK.getInstance().setVideoMute(mute);
        meeting().videoMute(mute);
    }

    public void audioMode (boolean audio) {
        NemoSDK.getInstance().switchCallMode(audio);
        meeting().audioMode(audio);
    }

    ConnectNemoCallback mConnectNemoCallback = new ConnectNemoCallback() {
        @Override
        public void onFailed(int i) {
            String info = "Connect Failed:";
            if (i == 1) {
                info += "(n:INVALID_PARAM)";
            } else if (i == 2) {
                info += "(n:NETWORK_UNAVAILABLE)";
            } else if (i == 3) {
                info += "(n:WRONG_PASSWORD)";
            } else if (i == 4) {
                info += "(n:HOST_ERROR)";
            } else {
                info += "(UNKNOW_ERROR)";
            }
            Log.e(TAG, info);
            if (mCallback != null) {
                mCallback.onConnectFailed(info);
            }
        }

        @Override
        public void onSuccess(LoginResponseData loginResponseData, boolean b) {
            Log.i(TAG, "connect success");
            if (mCallback != null){
                mCallback.onConnectSuccess(loginResponseData);
            }
        }

        @Override
        public void onNetworkTopologyDetectionFinished(LoginResponseData loginResponseData) {

        }

    };

    private NemoSDKListener mNemoSDKListener = new NemoSDKListener() {
        @Override
        public void onContentStateChanged(ContentState contentState) {

        }

        @Override
        public void onCallFailed(int i) {
            String info = "Call Failed:";
            if (i == NemoSDKErrorCode.WRONG_PASSWORD) {
                info += "(n:WRONG_PASSWORD)";
            } else if (i == NemoSDKErrorCode.INVALID_PARAM) {
                info += "(n:INVALID_PARAM)";
            } else if (i == NemoSDKErrorCode.NETWORK_UNAVAILABLE) {
                info += "(n:NETWORK_UNAVAILABLE)";
            } else if (i == NemoSDKErrorCode.HOST_ERROR) {
                info += "(n:HOST_ERROR)";
            } else {
                info += "(UNKNOW_ERROR)";
            }
            if (mCallback != null) {
                mCallback.onCallFailed(info);
            }
        }

        @Override
        public void onNewContentReceive(Bitmap bitmap) {

        }

        @Override
        public void onCallStateChange(CallState callState, String s) {
            Log.i(TAG, "callstatechanged:" + callState +" " + s);
            if (mCallback != null) {
                if (callState == CallState.CONNECTING) {
                    mCallback.onCallStateChange(VideoCallState.CONNECTING, s);
                } else if (callState == CallState.CONNECTED) {
                    mCallback.onCallStateChange(VideoCallState.CONNECTED, s);
                } else if (callState == CallState.DISCONNECTED) {
                    mCallback.onCallStateChange(VideoCallState.DISCONNECTED, s);
                }
            }
        }

        @Override
        public void onVideoDataSourceChange(List<VideoInfo> list) {
            meeting().nemoChange(list, NemoSDK.getLocalVideoStreamID());
        }

        @Override
        public void onRosterChange(RosterWrapper rosterWrapper) {

        }

        @Override
        public void onConfMgmtStateChanged(int i, String s, boolean b) {

        }

        @Override
        public void onRecordStatusNotification(int i, boolean b, String s) {

        }

        @Override
        public void onKickOut(int i, int i1) {

        }

        @Override
        public void onNetworkIndicatorLevel(int i) {

        }

        @Override
        public void onVideoStatusChange(int i) {

        }

        @Override
        public void onIMNotification(int i, String s, String s1) {

        }
    };

    private VideoUpdateListener mVideoUpdateListener = new VideoUpdateListener() {
        @Override
        public void onMemberChanged(ArrayList<MemberInfo> mScreen, ArrayList<MemberInfo> mOther, ArrayList<MemberInfo> mUnjoined, int duration, boolean isMemberContent, String meetingName) {
            if (mCallback != null) {
                mCallback.onMemberChanged(mScreen, mOther, mUnjoined, duration, isMemberContent, meetingName);
            }
        }

        @Override
        public void onMemberSizeChanged(int joinedSize, int totalSize) {
            if (mCallback != null) {
                mCallback.onMemberSizeChanged(joinedSize, totalSize);
            }
        }

    };

    private static MeetingInfoManager meetingInfoManager;
    private static MeetingInfoManager meeting () {
        if (meetingInfoManager == null) {
            MeetingInfoManager.registerInstance();
        }
        return meetingInfoManager;
    }

    public static void setMeetingInfoManager (MeetingInfoManager m) {
        meetingInfoManager = m;
    }
}
