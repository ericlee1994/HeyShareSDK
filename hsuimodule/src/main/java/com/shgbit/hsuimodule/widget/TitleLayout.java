package com.shgbit.hsuimodule.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shgbit.hsuimodule.R;
import com.shgbit.hsuimodule.callback.ITitleCallBack;
import com.shgbit.hsuimodule.util.Common;

import static com.shgbit.hsuimodule.activity.OldVideoActivity.mRecallMeeting;

/**
 * Created by Eric on 2017/9/27.
 */

public class TitleLayout extends LinearLayout {

    private ImageView mBackImg;
    private TextView mTitleTxt;
    private TextView mConferenceIDTxt;
    private TextView mConferenceTimeTxt;
    private CustomImageView mClockView;
    private Context context;
    private LinearLayout mLeftLayout;
    private LinearLayout mRightLayout;
    private LinearLayout mSpaceLayout;
    private LinearLayout mTitleLayout;

    private ITitleCallBack iTitleCallBack;

    private int mScreenWidth = Common.SCREENWIDTH;
    private int mScreenHeight = Common.SCREENHEIGHT;

    public TitleLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        mBackImg = new ImageView(context);
        mTitleTxt = new TextView(context);
//        mMemberNumTxt = new TextView(context);
        mConferenceIDTxt = new TextView(context);
        mConferenceTimeTxt = new TextView(context);
        mClockView = new CustomImageView(context);
        mLeftLayout = new LinearLayout(context);
        mRightLayout = new LinearLayout(context);
        mSpaceLayout = new LinearLayout(context);
        mTitleLayout = new LinearLayout(context);

        mBackImg.setImageResource(R.drawable.btn_back);
        mBackImg.setOnClickListener(mClickListener);

        mTitleTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENHEIGHT / 25);
        mTitleTxt.setGravity(Gravity.LEFT);
        mTitleTxt.setTextColor(getResources().getColor(R.color.white_color));
        mTitleTxt.setText(getResources().getString(R.string.meeting_name) + mRecallMeeting.getName() + "(0/0)");
        mTitleTxt.setSingleLine(true);
        mTitleTxt.setEllipsize(TextUtils.TruncateAt.MIDDLE);

        mConferenceIDTxt.setText(getResources().getString(R.string.conference_id) + mRecallMeeting.getId());
        mConferenceIDTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENHEIGHT / 25);
        mConferenceIDTxt.setGravity(Gravity.CENTER);
        mConferenceIDTxt.setTextColor(getResources().getColor(R.color.white_color));
        mConferenceIDTxt.setSingleLine(true);
        mConferenceIDTxt.setEllipsize(TextUtils.TruncateAt.END);

        mConferenceTimeTxt.setText(getResources().getString(R.string.meeting_time));
        mConferenceTimeTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENHEIGHT / 25);
        mConferenceTimeTxt.setGravity(Gravity.CENTER);
        mConferenceTimeTxt.setTextColor(getResources().getColor(R.color.white_color));

        LayoutParams imgParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        imgParams.weight = 1;
        imgParams.gravity = Gravity.CENTER;
        mBackImg.setLayoutParams(imgParams);

        LayoutParams titleParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        titleParams.weight = 6;
        titleParams.gravity = Gravity.CENTER;
        mTitleTxt.setLayoutParams(titleParams);

        LayoutParams titlelayoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        titlelayoutParams.weight = 32;
        mTitleLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mTitleLayout.setLayoutParams(titlelayoutParams);

        LayoutParams spaceParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        spaceParams.weight = 1;
        mSpaceLayout.setLayoutParams(spaceParams);

        LayoutParams cIDParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        cIDParams.weight = 20;
        cIDParams.gravity = Gravity.CENTER;
        mConferenceIDTxt.setLayoutParams(cIDParams);

        LayoutParams timeParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        timeParams.weight = 2;
        timeParams.gravity = Gravity.CENTER;
        mConferenceTimeTxt.setLayoutParams(timeParams);


        LayoutParams clockParams = new LayoutParams(0, mScreenHeight / 12);
        clockParams.weight = 2;
        clockParams.gravity = Gravity.CENTER;
        clockParams.leftMargin = mScreenWidth / 80;
        clockParams.rightMargin = mScreenWidth / 80;
        mClockView.setLayoutParams(clockParams);

        LayoutParams leftParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        leftParams.weight = 9;
        mLeftLayout.setLayoutParams(leftParams);

        LayoutParams rightParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        rightParams.weight = 3;
        mRightLayout.setLayoutParams(rightParams);

        mLeftLayout.addView(mTitleLayout);
        mLeftLayout.addView(mSpaceLayout);
        mLeftLayout.addView(mConferenceIDTxt);

        mRightLayout.addView(mConferenceTimeTxt);
        mRightLayout.addView(mClockView);

        mTitleLayout.addView(mBackImg);
        mTitleLayout.addView(mTitleTxt);


        addView(mLeftLayout);
        addView(mRightLayout);
    }

    public void setITitleCallBack(ITitleCallBack iTitleCallBack) {
        this.iTitleCallBack = iTitleCallBack;
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            iTitleCallBack.clickBackBtn();
        }
    };



    public void setmTitleTxt (String titleTxt, String numTxt) {
        mTitleTxt.setText(getResources().getString(R.string.meeting_name) + titleTxt + "(" + numTxt + ")");
    }

    public void setmMemberNumTxt (String numTxt) {
//        mMemberNumTxt.setText( "(" + numTxt + ")");
    }

    public void setmConferenceIDTxt (String conferenceIDTxt) {
        mConferenceIDTxt.setText(getResources().getString(R.string.conference_id) + conferenceIDTxt);
    }

    public void setmClockView (int time) {
        mClockView.startTime(time);
    }

    public void finishClock(){
        mClockView.stopTime();
    }
}
