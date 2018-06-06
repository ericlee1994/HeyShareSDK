package com.shgbit.hsuimodule.widget;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shgbit.hssdk.bean.DisplayType;
import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hssdk.bean.STATUS;
import com.shgbit.hssdk.bean.VI;
import com.shgbit.hssdk.sdk.Common;
import com.shgbit.hssdk.sdk.HeyShareSDK;
import com.shgbit.hsuimodule.R;
import com.shgbit.hsuimodule.activity.OldVideoActivity;
import com.shgbit.hsuimodule.bean.DisplayModeEnum;
import com.shgbit.hsuimodule.bean.VideoCellView;
import com.shgbit.hsuimodule.callback.IViewLayoutCallBack;


//import com.shgbit.android.whiteboard.CustomPaintView;

/**
 * Created by Eric on 2017/9/23.
 */

public class CellView extends FrameLayout {
    private static final String TAG = "CellView";

    public VideoCellView videoCellView;
    private View picView;
    private View ctrlView;

    private ImageView mImgContent;
    private LinearLayout mllytPicture;
    private ImageView mImgLeft;
    private ImageView mImgRight;

    private LinearLayout mLlytRobot;
    private LinearLayout mLlytStatus;
    private LinearLayout mLlytButton;
    private LinearLayout mLlytStatus1;
    private LinearLayout mLlytStatus2;
    private LinearLayout mLlytButton1;
    private LinearLayout mLlytButton2;

    private ImageView mImgRobot;
    private TextView mTxtRobot;

    private ImageView mImgSwitch;
    private ImageView mImgCloseView;
    private ImageView mImgCloseView2;

    private TextView mTxtStatus;
    private ImageView mImgInvite;
    private ImageView mImgClose;
    private ImageView mImgCancelCall;

    private TextView mTxtName;
    private ImageView mImgScale;
    private ImageView mImgBanMic;
    private ImageView mImgBG;

    private int mPosition;
    private DisplayModeEnum displayModeEnum = DisplayModeEnum.NOT_FULL_ONEFIVE;
    private VI vi = null;
    private IViewLayoutCallBack iViewLayoutCallBack;

    private boolean isNeedInit = true;
    private boolean isFullScreen = false;
    private Context mContext;
    private boolean isNeedChange = false;

//    private CustomPaintView mMainView;
    private boolean isAddComment = false;
    private boolean isRmComment = true;

    private int mCur = 0;

    private int width,height;

    private int l, r, t, b;

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public CellView(Context context) {
        super(context);
    }

    public CellView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setInfo(VI v, DisplayModeEnum displayModeEnum) {
        this.displayModeEnum = displayModeEnum;
        if (v != null && vi != null && vi.getmDisplayType() != v.getmDisplayType() || vi == null) {
            isNeedChange = true;
        }
        this.vi = v;
    }

    public String getPicturePage () {
        return ""+mCur;
    }

//    public void startPizhu (CustomPaintView mainView) {
//        if (isAddComment) {
//            return;
//        }
//
//        if (mainView == null) {
//            return;
//        }
//
//        mMainView = mainView;
//
//        isAddComment = true;
//        isRmComment = false;
//
//        if (vi != null && vi.getmDisplayType() == DisplayType.PICTURE) {
//            String str = vi.getmResId();
//            if (str != null && str.contains("@")) {
//                str = str.split("@")[0];
//                str = str.replace("image", "");
//                try {
//                    mCur = Integer.parseInt(str);
//                }catch (Throwable e){
//                    Log.e(TAG, "INTAGER paser failed");
//                }
//            }
//        }
//
//        LayoutParams lp = new LayoutParams(width, height);
//        addView(mMainView,lp);
//        mMainView.setLayout(width, height, false);
//
//        ctrlView.bringToFront();
//    }
//
//    public void endPizhu () {
//        if (isRmComment) {
//            return;
//        }
//
//        if (mMainView == null) {
//            return;
//        }
//
//        isAddComment = false;
//        isRmComment = true;
//
//        removeView(mMainView);
//    }

    public void init (Context context) {
        mContext = context;
        if (!isNeedInit){
            if (!isNeedChange) {
                return;
            }
            isNeedChange = false;
            if (vi == null || vi.getmDisplayType() == DisplayType.VIDEO) {
                mImgContent.setImageBitmap(null);
                picView.setVisibility(INVISIBLE);
                videoCellView.setVisibility(VISIBLE);
            } else {
                videoCellView.setSourceID("");
                videoCellView.setVisibility(INVISIBLE);
                picView.setVisibility(VISIBLE);
            }
            return;
        }
        isNeedInit = false;

        videoCellView  = new VideoCellView(context);
        addView(videoCellView);

        picView = LayoutInflater.from(context).inflate(R.layout.picture, null);
        addView(picView);

        mImgContent = (ImageView) picView.findViewById(R.id.img_content);
        mllytPicture = (LinearLayout) picView.findViewById(R.id.llyt_picture);
        mImgLeft = (ImageView) picView.findViewById(R.id.img_left);
        mImgRight = (ImageView) picView.findViewById(R.id.img_right);

        mImgLeft.setOnClickListener(mClickListener);
        mImgRight.setOnClickListener(mClickListener);

        mllytPicture.setVisibility(INVISIBLE);
        mCur = 0;

        videoCellView.setVisibility(INVISIBLE);
        picView.setVisibility(INVISIBLE);

        ctrlView = LayoutInflater.from(context).inflate(R.layout.displayview, null);
        addView(ctrlView);

        mLlytRobot = (LinearLayout) ctrlView.findViewById(R.id.llyt_robot);
        mLlytStatus = (LinearLayout) ctrlView.findViewById(R.id.llyt_status);
        mLlytButton = (LinearLayout) ctrlView.findViewById(R.id.llyt_button);
        mLlytStatus1 = (LinearLayout) ctrlView.findViewById(R.id.llyt_status1);
        mLlytStatus2 = (LinearLayout) ctrlView.findViewById(R.id.llyt_status2);
        mLlytButton1 = (LinearLayout) ctrlView.findViewById(R.id.llyt_button1);
        mLlytButton2 = (LinearLayout) ctrlView.findViewById(R.id.llyt_button2);
        mImgRobot = (ImageView) ctrlView.findViewById(R.id.img_robot);
        mTxtRobot = (TextView) ctrlView.findViewById(R.id.txt_robot);
        mImgSwitch = (ImageView) ctrlView.findViewById(R.id.img_switch_button1);
        mImgCloseView = (ImageView) ctrlView.findViewById(R.id.img_close_button1);
        mImgCloseView2 = (ImageView) ctrlView.findViewById(R.id.img_close_button2);
        mImgClose = (ImageView) ctrlView.findViewById(R.id.img_closeview_status1);
        mTxtStatus = (TextView) ctrlView.findViewById(R.id.txt_status);
        mImgInvite = (ImageView) ctrlView.findViewById(R.id.img_inviteagain_status1);
        mImgCancelCall = (ImageView) ctrlView.findViewById(R.id.img_callcancle_status2);

        mTxtName = (TextView) ctrlView.findViewById(R.id.txt_name);
        mImgScale = (ImageView) ctrlView.findViewById(R.id.img_scale);
        mImgBanMic = (ImageView) ctrlView.findViewById(R.id.img_banmic);
        mImgBG = (ImageView) ctrlView.findViewById(R.id.img_bg);

        mImgSwitch.setOnClickListener(mClickListener);
        mImgCloseView.setOnClickListener(mClickListener);
        mImgCloseView2.setOnClickListener(mClickListener);
        mImgClose.setOnClickListener(mClickListener);
        mImgInvite.setOnClickListener(mClickListener);
        mImgCancelCall.setOnClickListener(mClickListener);
        mImgScale.setOnClickListener(mClickListener);

    }

    public void setIViewChangeCallBack(IViewLayoutCallBack iViewLayoutCallBack) {
        this.iViewLayoutCallBack = iViewLayoutCallBack;
    }

    public void Finalize () {
        removeAllViews();
        if (videoCellView != null) {
            videoCellView.setSourceID("");
            videoCellView = null;
        }
    }

    public VideoCellView getVideoCellView() {
        return videoCellView;
    }

    public VI getVideoInfo() {
        return vi;
    }

    public void hideButtonLayout() {
        mLlytButton.setVisibility(INVISIBLE);
    }

    public void showButtonLayout () {
        if ((mPosition == 0 && !displayModeEnum.equals(DisplayModeEnum.NOT_FULL_QUARTER))|| vi == null || vi.getStatus().equals(STATUS.JOINED) == false) {
            return;
        }
        mLlytButton.setVisibility(VISIBLE);
        mLlytButton.bringToFront();
        if (displayModeEnum.equals(DisplayModeEnum.NOT_FULL_QUARTER)) {
            mLlytButton.setBackgroundResource(R.drawable.bg_view_btn0);
            mLlytButton1.setVisibility(View.INVISIBLE);
            mLlytButton2.setVisibility(View.VISIBLE);
            mLlytButton2.bringToFront();
        } else {
            mLlytButton.setBackgroundResource(R.drawable.bg_view_btn);
            mLlytButton1.setVisibility(View.VISIBLE);
            mLlytButton1.bringToFront();
            mLlytButton2.setVisibility(View.INVISIBLE);
        }
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public void updateView () {
//        if (vi != null && vi.getmDisplayType() == DisplayType.PICTURE) {
//            if (vi != null && vi.getmUrls() != null) {
//                ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_CENTER).setUseMemCache(true).build();
//                x.image().bind(mImgContent, vi.getmUrls().get(mCur),imageOptions);
//                if (mPosition == 0 && !isAddComment) {
//                    if (vi.getmUrls().size() == 1) {
//                        mllytPicture.setVisibility(INVISIBLE);
//                    } else {
//                        mllytPicture.setVisibility(VISIBLE);
//                        if (mCur == 0) {
//                            mImgLeft.setVisibility(INVISIBLE);
//                        } else {
//                            mImgLeft.setVisibility(VISIBLE);
//                        }
//                        if (mCur == vi.getmUrls().size()-1) {
//                            mImgRight.setVisibility(INVISIBLE);
//                        } else {
//                            mImgRight.setVisibility(VISIBLE);
//                        }
//                    }
//                } else {
//                    mllytPicture.setVisibility(INVISIBLE);
//                }
//            }
//        }

        setBackgroundImg();
        if (vi == null) {
            mTxtName.setText("");
            mTxtName.setVisibility(INVISIBLE);

            mImgScale.setVisibility(INVISIBLE);

            mImgBanMic.setVisibility(INVISIBLE);

            mLlytStatus.setVisibility(INVISIBLE);
            mLlytButton.setVisibility(INVISIBLE);
            mLlytRobot.setVisibility(INVISIBLE);

            videoCellView.setVisibility(INVISIBLE);
            videoCellView.setSourceID("");
            videoCellView.setParticipantId(-2);

            mImgContent.setImageBitmap(null);
            picView.setVisibility(INVISIBLE);

            if (displayModeEnum.equals(DisplayModeEnum.FULL_PIP_SIX)){
                mImgBG.setVisibility(INVISIBLE);
            }else {
                mImgBG.setVisibility(VISIBLE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                this.setBackgroundResource(R.drawable.video_shape1);
            }

        } else {

            if ((displayModeEnum.equals(DisplayModeEnum.FULL_PIP) || displayModeEnum.equals(DisplayModeEnum.FULL_PIP_SIX)
                    || displayModeEnum.equals(DisplayModeEnum.FULL))
                    && mPosition == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    this.setBackground(null);
                }
            }else {
                this.setBackgroundResource(R.drawable.video_shape);
            }

            mTxtName.setVisibility(VISIBLE);
            if (vi.getDisplayName() != null || !vi.getDisplayName().isEmpty()) {
                mTxtName.setText("  " + vi.getDisplayName() + "  ");
            }

            if (((displayModeEnum.equals(DisplayModeEnum.FULL_PIP) || displayModeEnum.equals(DisplayModeEnum.FULL)
                    || displayModeEnum.equals(DisplayModeEnum.NOT_FULL_ONEFIVE)) && mPosition == 0)
                    || displayModeEnum.equals(DisplayModeEnum.NOT_FULL_QUARTER)) {
                mImgScale.setVisibility(VISIBLE);
                if(displayModeEnum.equals(DisplayModeEnum.NOT_FULL_ONEFIVE)
                        || displayModeEnum.equals(DisplayModeEnum.NOT_FULL_QUARTER)) {
                    mImgScale.setImageResource(R.drawable.btn_scale);
                }else {
                    mImgScale.setImageResource(R.drawable.btn_scale_pressed);
                }
            }

            if (vi.isAudioMute()) {
                mImgBanMic.setVisibility(VISIBLE);
            }else {
                mImgBanMic.setVisibility(INVISIBLE);
            }

            if (vi.getStatus().equals(STATUS.JOINED)){
                mLlytStatus.setVisibility(INVISIBLE);

                if (vi.getNet_status() == MemberInfo.NET_STATUS.Normal){
                    mImgBG.setVisibility(INVISIBLE);
                    mLlytRobot.setVisibility(INVISIBLE);
                    videoCellView.setVisibility(VISIBLE);

                    videoCellView.setSourceID(vi.getDataSourceID());
                    videoCellView.setParticipantId(vi.getParticipantId());
                    videoCellView.setContent(vi.isContent());
                }else {
                    mImgBG.setVisibility(VISIBLE);
                    if (displayModeEnum.equals(DisplayModeEnum.FULL_PIP)
                            || displayModeEnum.equals(DisplayModeEnum.FULL_PIP_SIX)) {
                        mImgBG.setImageResource(R.drawable.bg_whole);
                    }
                    videoCellView.setVisibility(INVISIBLE);
                    videoCellView.setSourceID("");
                    mLlytRobot.setVisibility(VISIBLE);
                    mTxtRobot.setVisibility(VISIBLE);
                    if (vi.getNet_status() == MemberInfo.NET_STATUS.Lost) {
                        mTxtRobot.setText(R.string.network_limit);
                        mImgRobot.setImageResource(R.drawable.robot_network);
                    } else if (vi.getNet_status() == MemberInfo.NET_STATUS.Loading) {
                        mTxtRobot.setText(R.string.no_signal);
                        mImgRobot.setImageResource(R.drawable.robot_network);
                    } else if (vi.getNet_status() == MemberInfo.NET_STATUS.ContentOnlyUnsend) {
                        mTxtRobot.setText(R.string.content_prepare);
                        mImgRobot.setImageResource(R.drawable.robot_network);
                    } else if (vi.getNet_status() == MemberInfo.NET_STATUS.VideoMute) {
                        mTxtRobot.setText(R.string.close_camera);
                        mImgRobot.setImageResource(R.drawable.robot_camera);
                    } else if (vi.getNet_status() == MemberInfo.NET_STATUS.VoiceMode) {
                        mTxtRobot.setText(R.string.voice_mode);
                        mImgRobot.setImageResource(R.drawable.img_voice_mode);
                    } else {
                        mTxtRobot.setText(R.string.no_signal);
                        mImgRobot.setImageResource(R.drawable.robot_network);
                    }
                }
            }else {
                mLlytRobot.setVisibility(INVISIBLE);
                mImgBG.setVisibility(VISIBLE);
                if (displayModeEnum.equals(DisplayModeEnum.FULL_PIP)
                        || displayModeEnum.equals(DisplayModeEnum.FULL_PIP_SIX)) {
                    mImgBG.setImageResource(R.drawable.bg_whole);
                }
                mLlytStatus.bringToFront();
                if (vi.getStatus().equals(STATUS.BUSY)) {
                    videoCellView.setVisibility(INVISIBLE);

                    mLlytStatus.setVisibility(VISIBLE);
                    mLlytStatus1.setVisibility(VISIBLE);
                    mLlytStatus2.setVisibility(INVISIBLE);
                    mTxtStatus.setText(getResources().getString(R.string.busy));
                } else if (vi.getStatus().equals(STATUS.INVITING)) {
                    videoCellView.setVisibility(INVISIBLE);

                    mLlytStatus.setVisibility(VISIBLE);
                    mLlytStatus1.setVisibility(INVISIBLE);
                    mLlytStatus2.setVisibility(VISIBLE);
                    mTxtStatus.setText(getResources().getString(R.string.inviting));
                } else if (vi.getStatus().equals(STATUS.TIMEOUT)) {
                    videoCellView.setVisibility(INVISIBLE);

                    mLlytStatus.setVisibility(VISIBLE);
                    mLlytStatus1.setVisibility(VISIBLE);
                    mLlytStatus2.setVisibility(INVISIBLE);
                    mTxtStatus.setText(getResources().getString(R.string.timeout));
                }
            }
            mTxtName.bringToFront();
        }
    }


    private void setBackgroundImg() {
        if (displayModeEnum.equals(DisplayModeEnum.NOT_FULL_ONEFIVE)) {
            switch (mPosition) {
                case 0:
                    mImgBG.setImageResource(R.drawable.bg_big_one);
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    mImgBG.setImageResource(R.drawable.bg_small_five );
                    break;
                default:
                    break;
            }
        }else if (displayModeEnum.equals(DisplayModeEnum.NOT_FULL_QUARTER)) {
            switch (mPosition) {
                case 0:
                case 1:
                case 2:
                case 3:
                    mImgBG.setImageResource(R.drawable.bg_quarter);
                    break;
                default:
                    break;
            }
        }else if (displayModeEnum.equals(DisplayModeEnum.FULL_PIP)) {
//            mImgBG.setImageResource(R.drawable.bg_whole);
        }
    }

    public void setCtrlBtnSize(int width, int height) {
        float textsize = 20f;
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) mImgBanMic.getLayoutParams();
        RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) mImgScale.getLayoutParams();
        if (displayModeEnum.equals(DisplayModeEnum.NOT_FULL_ONEFIVE) || displayModeEnum.equals(DisplayModeEnum.FULL_PIP)
                || displayModeEnum.equals(DisplayModeEnum.FULL) || displayModeEnum.equals(DisplayModeEnum.FULL_PIP_SIX)) {
            if (mPosition == 0) {
                textsize = (float) (0.6 * height * 0.1);
                lp1.width = height/8;
                lp1.height = height/8;
                lp2.width = height/8;
                lp2.height = height/8;
            } else {
                textsize = (float) (0.6 * height * 0.25);
                lp1.width = height/3;
                lp1.height = height/3;
                lp2.width = height/4;
                lp2.height = height/4;
            }
        }else if (displayModeEnum.equals(DisplayModeEnum.NOT_FULL_QUARTER)) {
            textsize = (float) (0.6 * height * 0.2);
            lp1.width = height/6;
            lp1.height = height/6;
            lp2.width = height/6;
            lp2.height = height/6;
        }else {
            textsize = (float) (0.6 * height * 0.2);
            lp1.width = height/4;
            lp1.height = height/4;
            lp2.width = height/4;
            lp2.height = height/4;
        }

        textsize = textsize == 0?20f:textsize;

        mTxtRobot.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
        mTxtStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
        mTxtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
        mImgBanMic.setLayoutParams(lp1);
        mImgScale.setLayoutParams(lp2);
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.img_switch_button1){
                Log.i(TAG, "[user operation]click exchange view");
                if (iViewLayoutCallBack != null) {
                    iViewLayoutCallBack.changeId(mPosition);
                }
            }else if (id == R.id.img_close_button1){
                Log.i(TAG, "[user operation]click popdown view");
                if (iViewLayoutCallBack != null && vi != null) {
                    if (vi.getmDisplayType() == DisplayType.PICTURE) {
                        iViewLayoutCallBack.closePic();
                    } else {
                        iViewLayoutCallBack.changeStatus(mPosition);
                    }
                }
            }else if (id == R.id.img_close_button2){
                Log.i(TAG, "[user operation]click popdown view");
                if (iViewLayoutCallBack != null && vi != null) {
                    if (vi.getmDisplayType() == DisplayType.PICTURE) {
                        iViewLayoutCallBack.closePic();
                    } else {
                        iViewLayoutCallBack.changeStatus(mPosition);
                    }
                }
            }else if (id == R.id.img_closeview_status1) {
                Log.i(TAG, "btn_call_cancel");

                String[] user = {vi.getId()};
//                MeetingInfoManager.getInstance().StateChange(user, STATUS.WAITING);
                HeyShareSDK.getInstance().stateChange(user, STATUS.WAITING);

//                CancelInviteInfo cl = new CancelInviteInfo();
//                cl.setMeetingId(VideoActivity.mRecallMeeting.getId());
//                cl.setInvitedUser(vi.getRemoteName());
//                VideoActivity.vcApplication.getInteractManager().cancelMeeting(cl);
//                ServerInteractManager.getInstance().cancelMeeting(cl);
//                ServerInteractManager.getInstance().cancleMeeting(cl);

                HeyShareSDK.getInstance().cancelInvite(OldVideoActivity.mRecallMeeting.getId(), vi.getRemoteName());

            }else if (id == R.id.img_inviteagain_status1) {
                Log.i(TAG, "btn_invite_again");
                if (vi != null) {
                    String[] user1 = {vi.getId()};
                    OldVideoActivity.reInvited(user1);
//                    MeetingInfoManager.getInstance().StateChange(user1, STATUS.INVITING);
                    HeyShareSDK.getInstance().stateChange(user1, STATUS.INVITING);
                }
            }else if (id == R.id.img_callcancle_status2) {
                Log.i(TAG, "btn_invite_cancel");
                if (vi != null) {
                    String[] user2 = {vi.getId()};
//                    MeetingInfoManager.getInstance().StateChange(user2, STATUS.WAITING);
                    HeyShareSDK.getInstance().stateChange(user2, STATUS.WAITING);
                }
            }else if (id == R.id.img_scale) {
                iViewLayoutCallBack.backToDefaultMode(vi);
            }else if (id == R.id.img_left) {
                mCur = mCur - 1;
                changePicture();
            }else if (id == R.id.img_right) {
                mCur = mCur + 1;
                changePicture();
            }
//            switch (view.getId()) {
//                case R.id.img_robot:
//                    break;
//                case R.id.img_switch_button1:
//                    Log.i(TAG, "[user operation]click exchange view");
//                    if (iViewLayoutCallBack != null) {
//                        iViewLayoutCallBack.changeId(mPosition);
//                    }
//                    break;
//                case R.id.img_close_button1:
//                    Log.i(TAG, "[user operation]click popdown view");
//                    if (iViewLayoutCallBack != null && vi != null) {
//                        if (vi.getmDisplayType() == DisplayType.PICTURE) {
//                            iViewLayoutCallBack.closePic();
//                        } else {
//                            iViewLayoutCallBack.changeStatus(mPosition);
//                        }
//                    }
//                    break;
//                case R.id.img_close_button2:
//                    Log.i(TAG, "[user operation]click popdown view");
//                    if (iViewLayoutCallBack != null && vi != null) {
//                        if (vi.getmDisplayType() == DisplayType.PICTURE) {
//                            iViewLayoutCallBack.closePic();
//                        } else {
//                            iViewLayoutCallBack.changeStatus(mPosition);
//                        }
//                    }
//                    break;
//                case R.id.img_closeview_status1:
//                    Log.i(TAG, "btn_call_cancel");
//
//                    String[] user = {vi.getId()};
//                    MeetingInfoManager.getInstance().StateChange(user, STATUS.WAITING);
//
//                    CancleInviteInfo cl = new CancleInviteInfo();
//                    cl.setMeetingId(VideoActivity.mRecallMeeting.getId());
//                    cl.setSessionId(Common.SESSIONID);
//                    cl.setInvitedUser(vi.getRemoteName());
//                    VideoActivity.vcApplication.getInteractManager().cancelMeeting(cl);
//                    break;
//                case R.id.img_inviteagain_status1:
//                    Log.i(TAG, "btn_invite_again");
//                    if (vi != null) {
//                        String[] user1 = {vi.getId()};
//                        VideoActivity.reInvited(user1);
//                        MeetingInfoManager.getInstance().StateChange(user1, STATUS.INVITING);
//                    }
//                    break;
//                case R.id.img_callcancle_status2:
//                    Log.i(TAG, "btn_invite_cancel");
//                    if (vi != null) {
//                        String[] user2 = {vi.getId()};
//                        MeetingInfoManager.getInstance().StateChange(user2, STATUS.WAITING);
//                    }
//                    break;
//                case R.id.img_scale:
//                    iViewLayoutCallBack.backToDefaultMode(vi);
//                    break;
//                case R.id.img_left:
//                    mCur = mCur - 1;
//                    changePicture();
//                    break;
//                case R.id.img_right:
//                    mCur = mCur + 1;
//                    changePicture();
//                    break;
//                default:
//                    break;
//            }
        }
    };

    private void changePicture () {
//        if (vi != null && vi.getmUrls() != null) {
//            ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).build();
//            x.image().bind(mImgContent, vi.getmUrls().get(mCur),imageOptions);
//
//            if (vi.getmUrls().size() == 1) {
//                mllytPicture.setVisibility(INVISIBLE);
//            } else {
//                mllytPicture.setVisibility(VISIBLE);
//                if (mCur == 0) {
//                    mImgLeft.setVisibility(INVISIBLE);
//                } else {
//                    mImgLeft.setVisibility(VISIBLE);
//                }
//                if (mCur == vi.getmUrls().size()-1) {
//                    mImgRight.setVisibility(INVISIBLE);
//                } else {
//                    mImgRight.setVisibility(VISIBLE);
//                }
//            }
//        }
    }

    private int lastX;
    private int lastY;
    private int firstX;
    private int firstY;

    private boolean isclick;
    private long startTime = 0;
    private long endTime = 0;

    private int screenWidth = Common.SCREENHEIGHT;
    private int screenHeight = Common.SCREENWIDTH;
    private TranslateAnimation translateAnimation;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        if (displayModeEnum.equals(DisplayModeEnum.FULL_PIP)
                && mPosition != 0) {
            int rawX = (int) event.getRawX();
            int rawY = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    Log.e(TAG, "ACTION_DOWN");
                    isclick = false;
                    firstX = rawX;
                    firstY = rawY;
                    lastX = rawX;
                    lastY = rawY;
                    startTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_MOVE:
                    isclick = true;
                    int dx = rawX-lastX;
                    int dy = rawY-lastY;
                    int distance = (int) Math.sqrt(dx * dx + dy * dy);
                    if(distance == 0){
                        isclick = false;
                        break;
                    }
                    float x = getX() + dx;
                    float y = getY() + dy;

                    x = x < 0 ? 0:x > screenWidth - getWidth() ? screenWidth - getWidth():x;
                    y = y < 0 ? 0:y + getHeight() > screenHeight ? screenHeight - getHeight():y;
                    layout((int)x, (int)y, (int)(x + getWidth()), (int)(y + getHeight()));
                    lastX = rawX;
                    lastY = rawY;
                    break;
                case MotionEvent.ACTION_UP:

                    endTime = System.currentTimeMillis();
                    //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                    if ((endTime - startTime) > 0.1 * 1000L) {
                        isclick = true;
                    } else {
                        isclick = false;
                        if (vi != null) {
                            Log.e(TAG, "click small view");
//                            MeetingInfoManager.getInstance().ScreenExchange("", vi.getId(), null, vi.getSessionType());
                            iViewLayoutCallBack.changeId(mPosition);
                            MyVideoVIew.isMove = false;
                        }
                    }
                    int offX = rawX-lastX;
                    final int offY = rawY-lastY;
//                    if(isDrag){
//                        setPressed(false);
//                        if (getRight() >= screenWidth - 100){
//                            translateAnimation = new TranslateAnimation(0, screenWi tionListener() {
//                                @Override
//                                public void onAnimationStart(Animation animation) {
//
//                                }
//
//                                @Override
//                                public void onAnimationEnd(Animation animation) {
//                                    layout((int)(Common.mScreenHeight * 0.75), getTop() + offY, Common.mScreenHeight, getBottom() + offY);
//                                    clearAnimation();
//                                }
//
//                                @Override
//                                public void onAnimationRepeat(Animation animation) {
//
//                                }
//                            });
//                            this.startAnimation(translateAnimation);
//                            lastX = 1440;
//                            lastY = rawY;
//                        }else if (getLeft() <= 100){
////
//                            translateAnimation = new TranslateAnimation(0, -getLeft(), 0, 0);
//                            translateAnimation.setDuration(500);
//                            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
//                                @Override
//                                public void onAnimationStart(Animation animation) {
//
//                                }
//
//                                @Override
//                                public void onAnimationEnd(Animation animation) {
//                                    layout(0, getTop() + offY, (int)(Common.mScreenHeight * 0.25), getBottom() + offY);
//                                    clearAnimation();
//                                }
//
//                                @Override
//                                public void onAnimationRepeat(Animation animation) {
//
//                                }
//                            });
//                            this.startAnimation(translateAnimation);
//                            lastX = 0;
//                            lastY = rawY;
//                        }else {
//                            layout(getLeft() + offX, getTop() + offY,
//                                    getRight() + offX, getBottom() + offY);
//                        }
//                    }

//                    Point point = getExactlyCoordinate(new Point(getLeft() + offX, getTop() + offY), new Point(Common.mScreenHeight, Common.mScreenWidth),
//                            new Point(Common.mScreenHeight/6, Common.mScreenWidth/6), MyVideoVIew.getOtherPosition(mPosition));
//                    layout(point.x, point.y, point.x + Common.mScreenHeight/6, point.y + Common.mScreenWidth/6);
                    l = getLeft() + offX;
                    t = getTop() + offY;
                    r = getRight() + offX;
                    b = getBottom() + offY;
                    MyVideoVIew.isMove = true;
                    layout(getLeft() + offX, getTop() + offY,
                            getRight() + offX, getBottom() + offY);
                    break;
                default:
                    break;
            }
            return true;
        }else if ((displayModeEnum.equals(DisplayModeEnum.FULL_PIP) || displayModeEnum.equals(DisplayModeEnum.FULL_PIP_SIX))
                && mPosition == 0) {
            switch (event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_UP:
                    Log.e(TAG, "isFisrtScreen");
//                    iViewLayoutCallBack.showHideLayout();
                    break;
                default:
                    break;
            }

            return true;
        }else{
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    UIHandler.removeMessages(HIDE_LAYOUT);
                    if (vi != null) {
                        iViewLayoutCallBack.hideViewLayout(vi.getId());
                    }
                    showButtonLayout();
                    UIHandler.sendEmptyMessageDelayed(HIDE_LAYOUT, 5 * 1000);
                    break;
                default:
                    break;
            }
            return true;
        }

    }

    private final int HIDE_LAYOUT = 0x001;
    private Handler UIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HIDE_LAYOUT:
                    hideButtonLayout();
                    break;
            }
        }
    };

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            width = right - left;
            height = bottom - top;
//            Log.e(TAG, "#################changed!!" + width + " " + height);
            LayoutParams lp = new LayoutParams(width, height);
            if (videoCellView != null) {
                videoCellView.setLayoutParams(lp);
            }

            if (picView != null) {
                picView.setLayoutParams(lp);
            }

            if (ctrlView != null) {
                ctrlView.setLayoutParams(lp);
            }
        }
    }
}
