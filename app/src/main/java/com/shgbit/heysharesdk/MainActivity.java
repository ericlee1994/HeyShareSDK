package com.shgbit.heysharesdk;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.ainemo.sdk.otf.NemoSDK;
import com.shgbit.hssdk.bean.InviteCancelInfo;
import com.shgbit.hssdk.bean.InviteMeeting;
import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hssdk.bean.VideoCallState;
import com.shgbit.hssdk.callback.HeyshareCallback;
import com.shgbit.hssdk.callback.VideoListCallback;
import com.shgbit.hssdk.callback.VideoStateCallback;
import com.shgbit.hssdk.sdk.HeyShareSDK;

import java.nio.channels.GatheringByteChannel;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private String mServerIP = "http://www.shgbitcloud.com:4005";
    private String mUserName = "lizheng";
    private String mUserPwd = "123456";
//    private String meetingId = "910043523258";
//    private String meetingPwd = "603918";

    private VideoFragment mVideoFragment;
    private LoginFragment mLoginFragment;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        HeyShareSDK.getInstance().init(this, mServerIP);

        HeyShareSDK.getInstance().setVideoStateCallback(videoStateCallback);
        HeyShareSDK.getInstance().setVideoListCallback(videoListCallback);
        HeyShareSDK.getInstance().setHeyshareCallback(heyshareCallback);

        mVideoFragment = new VideoFragment();
        mLoginFragment = new LoginFragment();
        manager = getFragmentManager();

        manager.beginTransaction().add(R.id.content_frame, mLoginFragment).commitAllowingStateLoss();

    }
    VideoStateCallback videoStateCallback = new VideoStateCallback() {
        @Override
        public void onCallFailed(String s) {
            Log.e(TAG, "onCallFailed:" + s);
        }

        @Override
        public void onCallStateChange(VideoCallState videoCallState, String s) {
            Observable.just(videoCallState)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<VideoCallState>() {
                        @Override
                        public void accept(VideoCallState videoCallState) throws Exception {
                            switch (videoCallState) {
                                case CONNECTING:
                                    Log.e(TAG, "CONNECTING");
                                    hideSoftKeyboard();
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                    if (mVideoFragment.isAdded()){
                                        manager.beginTransaction().show(mVideoFragment).commitAllowingStateLoss();
                                    }else{
                                        manager.beginTransaction().add(R.id.content_frame, mVideoFragment).commitAllowingStateLoss();
                                    }
                                    break;
                                case CONNECTED:
                                    Log.e(TAG, "CONNECTED");
                                    hideSoftKeyboard();
//                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
                                    if (mVideoFragment.isAdded()) {
                                        manager.beginTransaction().hide(mLoginFragment).show(mVideoFragment).commitAllowingStateLoss();
                                    } else {
                                        manager.beginTransaction().add(R.id.content_frame,
                                                mVideoFragment).hide(mLoginFragment).commitAllowingStateLoss();
                                    }
                                    mVideoFragment.requestLocalFrame();
                                    HeyShareSDK.getInstance().openLocalCamera();
                                    break;
                                case DISCONNECTED:
                                    Log.e(TAG, "DISCONNECTED");
                                    if (mVideoFragment.isAdded()) {
                                        mVideoFragment.releaseResource();
                                        manager.beginTransaction().hide(mVideoFragment).show(mLoginFragment).commitAllowingStateLoss();
                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                    }
                                    break;
                            }
                        }
                    });
        }
    };

    VideoListCallback videoListCallback = new VideoListCallback() {
        @Override
        public void onMemberChanged(ArrayList<MemberInfo> arrayList, ArrayList<MemberInfo> arrayList1, ArrayList<MemberInfo> arrayList2, int i, boolean b, String s) {
            Log.e(TAG, "onMemberChanged:" + arrayList.size());
            mVideoFragment.onVideoDataSourceChange(arrayList);
        }

        @Override
        public void onMemberSizeChanged(int i, int i1) {

        }

        @Override
        public void onMemberCommentExit(MemberInfo memberInfo) {

        }
    };

    HeyshareCallback heyshareCallback = new HeyshareCallback() {
        @Override
        public void onInit(boolean b, String s) {
            Common.isInit = b;
            if (Common.isInit) {
                HeyShareSDK.getInstance().connect(mUserName, mUserPwd);
            }
        }

        @Override
        public void onLogin(boolean b, String s) {
            Common.isLogin = b;
            if (b){
                Log.e(TAG, "onLogin: true" );
//                HeyShareSDK.getInstance().joinMeeting(meetingId, meetingPwd);
            }else {
                Log.e(TAG, "onLogin:" + s);
            }
        }

        @Override
        public void onLogout(boolean b, String s) {

        }

        @Override
        public void onInviteMeeting(boolean b, String s) {

        }

        @Override
        public void onDeleteMeeting(boolean b, String s) {

        }

        @Override
        public void onUpdateMeeting(boolean b, String s) {

        }

        @Override
        public void eventInvitedMeeting(InviteMeeting inviteMeeting) {

        }

        @Override
        public void eventDifferentPlaceLogin() {

        }

        @Override
        public void eventInvitingCancel(InviteCancelInfo inviteCancelInfo) {

        }


    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HeyShareSDK.getInstance().finalizeSDK();
    }

    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
