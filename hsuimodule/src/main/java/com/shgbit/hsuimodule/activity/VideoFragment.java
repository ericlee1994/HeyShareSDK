package com.shgbit.hsuimodule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hssdk.sdk.HeyShareSDK;
import com.shgbit.hsuimodule.R;
import com.shgbit.hsuimodule.bean.DISCONNECT_STATE;
import com.shgbit.hsuimodule.bean.DisplayModeEnum;
import com.shgbit.hsuimodule.bean.InvitedMeeting;
import com.shgbit.hsuimodule.bean.VideoInfo;
import com.shgbit.hsuimodule.callback.IModeCallBack;
import com.shgbit.hsuimodule.callback.IPopViewCallBack;
import com.shgbit.hsuimodule.callback.ITitleCallBack;
import com.shgbit.hsuimodule.callback.IVideoViewCallBack;
import com.shgbit.hsuimodule.util.Common;
import com.shgbit.hsuimodule.widget.ModeDialog;
import com.shgbit.hsuimodule.widget.MyVideoVIew;
import com.shgbit.hsuimodule.widget.PopupOldView;
import com.shgbit.hsuimodule.widget.TitleLayout;
import com.shgbit.hsuimodule.widget.VCDialog;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;


public class VideoFragment extends Fragment implements VideoContract.View {

    private static final String TAG = "VideoFragment";

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

    private VideoContract.Presenter mPresenter;

    private RelativeLayout mWholeLayout;
    private MyVideoVIew videoView;
    private TitleLayout mTopLayout;
    private LinearLayout mBottomLayout;
    private PopupOldView popupView;
    private DisplayModeEnum lastMode;
    private DisplayModeEnum lastModeFlag;
    private DISCONNECT_STATE mExitState;
    private boolean isModeChange = false;

    private boolean muteCamera = false;
    private boolean muteMic = true;
    private boolean isAudioMode = false;
    private boolean audioMode = false;

    private VCDialog mDialog;
    private ModeDialog modeDialog;

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(VideoContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_video, container, false);

//        frameLayout = findViewById(R.id.video_fragment);
        mWholeLayout = root.findViewById(R.id.whole_layout);
        lastMode = DisplayModeEnum.FULL_FOUR;
        lastModeFlag = DisplayModeEnum.NOT_FULL_ONEFIVE;
        mPresenter.changeDisplayMode(DisplayModeEnum.NOT_FULL_ONEFIVE);

        mPresenter.connectNemo(Common.USERNAME, Common.PASSWORD);

        return root;

    }

    private Handler mUIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOW_HIDE_LAYOUT:
                    mUIHandler.removeMessages(SHOW_HIDE_LAYOUT);
                    mUIHandler.removeMessages(HIDE_HIDE_LAYOUY);
                    mBottomLayout.setVisibility(View.INVISIBLE);
                    popupView.setVisibility(View.VISIBLE);
                    mUIHandler.sendEmptyMessageDelayed(HIDE_HIDE_LAYOUY, 5000);
                    break;
                case HANGUP:
                    String mHangupWarn;
                    if (popupView != null && popupView.getRecordingStatus()) {
                        mHangupWarn = getString(R.string.tips_41);
                    } else {
                        mHangupWarn = getString(R.string.tips_22);
                    }
                    showDialog(mHangupWarn, VCDialog.DialogType.Handup);
                    break;
                case HIDE_HIDE_LAYOUY:
                    if (popupView != null) {
                        popupView.setVisibility(View.INVISIBLE);
                        popupView.dismissPopupView();
                    }
                    mBottomLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };


    @Override
    public void showDisplayMode(DisplayModeEnum displayModeEnum) {
        Log.i(TAG, "change mode to " + displayModeEnum.toString());
        if (lastMode.equals(displayModeEnum)) {
            return;
        }
        isModeChange = true;
        mWholeLayout.removeAllViews();
        if (videoView != null) {
            videoView.destroy();
            videoView.removeAllViews();
            videoView = null;
        }
        if (displayModeEnum.equals(DisplayModeEnum.NOT_FULL_ONEFIVE)
                || displayModeEnum.equals(DisplayModeEnum.NOT_FULL_QUARTER)) {
            mTopLayout = new TitleLayout(getContext());
            mBottomLayout = new LinearLayout(getContext());
            popupView = new PopupOldView(getContext(), muteMic, muteCamera, isAudioMode, mPresenter);
            popupView.setIPopViewCallback(iPopViewCallBack);
            videoView = new MyVideoVIew(getContext(), displayModeEnum);


//            dismissPopup();
            mPresenter.hidePopView();
            mWholeLayout.addView(mTopLayout);
            mWholeLayout.addView(videoView);
            mWholeLayout.addView(popupView);
            mWholeLayout.addView(mBottomLayout);

            videoView.setiVideoViewCallBack(mVideoViewListener);
            mTopLayout.setITitleCallBack(new ITitleCallBack() {
                @Override
                public void clickBackBtn() {
                    mUIHandler.sendEmptyMessage(HANGUP);
                }
            });
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
            mBottomLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.clickBottomLayout();
                }
            });

            RelativeLayout.LayoutParams popParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT);
            popupView.setLayoutParams(popParams);

        } else if (displayModeEnum.equals(DisplayModeEnum.FULL_PIP)) {
            videoView = new MyVideoVIew(getContext(), displayModeEnum);
            videoView.setiVideoViewCallBack(mVideoViewListener);
            RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT);
            videoView.setLayoutParams(videoParams);
            mWholeLayout.addView(videoView);
        } else if (displayModeEnum.equals(DisplayModeEnum.FULL)) {
            videoView = new MyVideoVIew(getContext(), displayModeEnum);
            videoView.setiVideoViewCallBack(mVideoViewListener);
            RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            videoView.setLayoutParams(videoParams);
            mWholeLayout.addView(videoView);
        } else if (displayModeEnum.equals(DisplayModeEnum.FULL_PIP_SIX)) {
            videoView = new MyVideoVIew(getContext(), displayModeEnum);
            popupView = new PopupOldView(getContext(), muteMic, muteCamera, audioMode, mPresenter);
            popupView.setIPopViewCallback(iPopViewCallBack);
            mBottomLayout = new LinearLayout(getContext());
            mPresenter.hidePopView();
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
            mBottomLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.clickBottomLayout();
                }
            });

            RelativeLayout.LayoutParams popParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT);
            popupView.setLayoutParams(popParams);

        }
        lastMode = displayModeEnum;
        videoView.requestLocalFrame();
//        if (mainView != null) {
//            videoView.setPizhu(mainView);
//        }
    }

    @Override
    public void hidePopView() {
        if (popupView != null) {
            popupView.setVisibility(View.INVISIBLE);
            popupView.dismissPopupView();
        }
    }

    @Override
    public void showBottomLayout() {
        mUIHandler.sendEmptyMessage(SHOW_HIDE_LAYOUT);
    }

    @Override
    public void onVideoList(ArrayList<VideoInfo> mScreenList, ArrayList<VideoInfo> mOtherList, ArrayList<VideoInfo> mUnjoinedList) {

    }

    @Override
    public void showDialog(Object content, VCDialog.DialogType type) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

        mDialog = new VCDialog(getContext(), R.style.Dialog, type);
        mDialog.setContent(content);
        mDialog.setDialogCallback(mDialogCallback);
        mDialog.setCanceledOnTouchOutside(false);

        mDialog.show();
    }

    @Override
    public void showModeDialog() {
        modeDialog = new ModeDialog(getContext(), R.style.ModeSwitchDialog, true);
        modeDialog.setIModeCallBack(iModeCallBack);
        modeDialog.show();
    }

    @Override
    public void updatePopView(ArrayList<MemberInfo> mScreenList, ArrayList<MemberInfo> mOtherList, ArrayList<MemberInfo> mUnjoinedList) {
        popupView.setMeetingUserData(mScreenList, mOtherList, mUnjoinedList);
    }

    @Override
    public void hideBottomLayout() {
        mUIHandler.sendEmptyMessage(HIDE_HIDE_LAYOUY);
    }

    @Override
    public void startRecord(boolean result, String error) {
        popupView.startRecord(result, error);
    }

    @Override
    public void endRecord(boolean result, String error) {
        popupView.endRecord(result, error);
    }

    @Override
    public void getRecordTime(long time) {
        popupView.setRecordTime(time);
    }

    @Override
    public void initRecord(boolean isShow, String status, String meetingId) {
        popupView.initRecord(isShow, status, meetingId);
    }

    @Override
    public void onFinish() {
        mTopLayout.finishClock();
        mDialog = null;
        modeDialog = null;
        videoView.setOnClickListener(null);
        mUIHandler.removeCallbacksAndMessages(null);
        videoView.destroy();
        popupView.destroy();
        getActivity().finish();
    }

    private IModeCallBack iModeCallBack = new IModeCallBack() {
        @Override
        public void getDisplayMode(DisplayModeEnum displayModeEnum) {
            mPresenter.changeDisplayMode(displayModeEnum);
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

                        Common.meetingId = m.getMeetingId();
                        Common.meetingPd = m.getPassword();
                        Common.meetingName = m.getMeetingName();

                        mPresenter.hangUp();

                    }
//                    MessageData.getInstance().updateMessageStatus(m.getMeetingId(), true);

                } else if (type == VCDialog.DialogType.Handup) {
                    mPresenter.hangUp();

                } else if (type == VCDialog.DialogType.Normal) {
                    mPresenter.finish();

                } else if (type == VCDialog.DialogType.ErrorHangup) {
                    mPresenter.hangUp();
                    mPresenter.finish();

                } else if (type == VCDialog.DialogType.Recall) {
                    mPresenter.hangUp();
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
                    mPresenter.refuseInvite();
                } else if (type == VCDialog.DialogType.Normal) {
                    if (popupView != null) {
                        popupView.StopVideotape();
                    }
                    mPresenter.finish();
                } else if (type == VCDialog.DialogType.ErrorHangup || type == VCDialog.DialogType.Recall) {
                    if (popupView != null) {
                        popupView.StopVideotape();
                    }
                    mPresenter.hangUp();
                } else {

                }
            } catch (Throwable e) {
                Log.e(TAG, "onCancel Throwable:" + e.toString());
            }
        }
    };


    private IVideoViewCallBack mVideoViewListener = new IVideoViewCallBack() {
        @Override
        public void backToDefaultMode(VideoInfo vi0, VideoInfo vi1) {

            if (lastMode.equals(DisplayModeEnum.NOT_FULL_ONEFIVE)) {
                lastModeFlag = DisplayModeEnum.NOT_FULL_ONEFIVE;
                mPresenter.changeDisplayMode(DisplayModeEnum.FULL);

            } else if (lastMode.equals(DisplayModeEnum.NOT_FULL_QUARTER)) {
                lastModeFlag = DisplayModeEnum.NOT_FULL_QUARTER;
                mPresenter.exchangeScreen(vi0, vi1);
                mPresenter.changeDisplayMode(DisplayModeEnum.FULL);

            } else if (lastMode.equals(DisplayModeEnum.FULL)) {
                mPresenter.changeDisplayMode(lastModeFlag);
            }
        }

    };

    private IPopViewCallBack iPopViewCallBack = new IPopViewCallBack() {
        @Override
        public void onClickPopBtn() {
            mUIHandler.sendEmptyMessage(HIDE_HIDE_LAYOUY);
        }

        @Override
        public void onClickMenuBtn(String type) {
            mPresenter.clickMenuBtn(type);
        }

        @Override
        public void onClickPerson(MemberInfo memberInfo) {
            mPresenter.clickMenuPerson(memberInfo);
        }
    };

}
