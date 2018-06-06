package com.shgbit.hsuimodule.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shgbit.hssdk.bean.STATUS;
import com.shgbit.hssdk.bean.VI;
import com.shgbit.hssdk.sdk.Common;
import com.shgbit.hssdk.sdk.HeyShareSDK;
import com.shgbit.hsuimodule.R;
import com.shgbit.hsuimodule.activity.OldVideoActivity;


/**
 * Created by Eric on 2017/7/18.
 */

public class StatusView extends LinearLayout {

    private static final String TAG = "StatusView";

    private TextView tv_status;
    private Button btn_invite_again;
    private Button btn_invite_cancel;
    private Button btn_call_cancel;
    private LinearLayout topLayout;
    private LinearLayout bottomLayout;
    private LinearLayout bLeftLayout;
    private LinearLayout bRightLayout;
    private String vId;
    private VI vi;

    public void setVId(String vId, VI vi) {
        this.vId = vId;
        this.vi = vi;
    }

    public StatusView(Context context) {
        super(context);
        initView(context);
    }

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void initView(Context context){


        tv_status = new TextView(context);
        btn_invite_again = new Button(context);
        btn_invite_cancel = new Button(context);
        btn_call_cancel = new Button(context);
        topLayout = new LinearLayout(context);
        bottomLayout = new LinearLayout(context);
        topLayout = new LinearLayout(context);
        bLeftLayout = new LinearLayout(context);
        bRightLayout = new LinearLayout(context);
        setOrientation(VERTICAL);


        MySelector selector = new MySelector();
        Drawable invite_Normal = getResources().getDrawable(R.drawable.btn_invite_again);
        Drawable invite_Pressed = getResources().getDrawable(R.drawable.btn_invite_again_pre);
        Drawable close_Normal = getResources().getDrawable(R.drawable.btn_close_view);
        Drawable close_Pressed = getResources().getDrawable(R.drawable.btn_close_view_pre);
        Drawable cancel_Normal = getResources().getDrawable(R.drawable.btn_call_cancel);
        Drawable cancel_Pressed = getResources().getDrawable(R.drawable.btn_call_cancel_pre);


        topLayout.setOrientation(VERTICAL);
        topLayout.addView(tv_status);

        bottomLayout.setOrientation(HORIZONTAL);
        bottomLayout.addView(bLeftLayout);
        bottomLayout.addView(bRightLayout);
        bottomLayout.addView(btn_call_cancel);
        bLeftLayout.addView(btn_invite_again);
        bRightLayout.addView(btn_invite_cancel);

        tv_status.setTextColor(Color.BLACK);
        tv_status.setText("");

        btn_call_cancel.setVisibility(INVISIBLE);

        btn_invite_again.setBackgroundDrawable(selector.newSelector(context, invite_Normal, invite_Pressed));
        btn_invite_cancel.setBackgroundDrawable(selector.newSelector(context, close_Normal, close_Pressed));
        btn_call_cancel.setBackgroundDrawable(selector.newSelector(context, cancel_Normal, cancel_Pressed));

        btn_invite_again.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btn_invite_again" + vId);
                String[] user = {vId};
                OldVideoActivity.reInvited(user);
//                MeetingInfoManager.getInstance().StateChange(user, STATUS.INVITING);
                HeyShareSDK.getInstance().stateChange(user, STATUS.INVITING);
            }
        });

        btn_invite_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btn_invite_cancel" + vId);
                String[] user = {vId};
//                MeetingInfoManager.getInstance().StateChange(user, STATUS.WAITING);
                HeyShareSDK.getInstance().stateChange(user, STATUS.WAITING);
            }
        });

        btn_call_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btn_call_cancel" + vId);

                String[] user = {vId};
//                MeetingInfoManager.getInstance().StateChange(user, STATUS.WAITING);
                HeyShareSDK.getInstance().stateChange(user, STATUS.WAITING);
//                CancelInviteInfo cl = new CancelInviteInfo();
//                cl.setMeetingId(VideoActivity.mRecallMeeting.getId());
//                cl.setInvitedUser(vi.getRemoteName());
//                VideoActivity.vcApplication.getInteractManager().cancelMeeting(cl);
//                ServerInteractManager.getInstance().cancleMeeting(cl);
                HeyShareSDK.getInstance().cancelInvite(OldVideoActivity.mRecallMeeting.getId(), vi.getRemoteName());

            }
        });

        addView(topLayout);
        addView(bottomLayout);
    }

    public void setStatusText(String statusText){
        tv_status.setText(statusText);
    }

    public void hideBottomBtn(){
//        bottomLayout.setVisibility(INVISIBLE);
        btn_call_cancel.setVisibility(VISIBLE);
        bLeftLayout.setVisibility(GONE);
        bRightLayout.setVisibility(GONE);
    }

    public void showBottomBtn(){
        bottomLayout.setVisibility(VISIBLE);
        btn_call_cancel.setVisibility(GONE);
        bLeftLayout.setVisibility(VISIBLE);
        bRightLayout.setVisibility(VISIBLE);
    }

    public void setLayout(int width, int height) {
        LayoutParams topLP = new LayoutParams(width, (int)(0.55 * height));
        topLayout.setLayoutParams(topLP);
        topLayout.setGravity(Gravity.BOTTOM);
        topLP.gravity = Gravity.CENTER_VERTICAL;

        LayoutParams bottomLP = new LayoutParams(width,(int)(0.45 * height));
        bottomLayout.setLayoutParams(bottomLP);
        bottomLayout.setGravity(Gravity.CENTER);
        bottomLayout.setOrientation(HORIZONTAL);

        LayoutParams bLeftLP = new LayoutParams((int)(0.5 * width),(int)(0.45 * height));
        bLeftLayout.setLayoutParams(bLeftLP);
        bLeftLayout.setGravity(Gravity.CENTER);

        LayoutParams bRightLP = new LayoutParams((int)(0.5 * width),(int)(0.45 * height));
        bRightLayout.setLayoutParams(bRightLP);
        bRightLayout.setGravity(Gravity.CENTER);

        LayoutParams tvLP = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvLP.gravity = Gravity.CENTER;
        tv_status.setGravity(Gravity.CENTER);
        tv_status.setLayoutParams(tvLP);
        tv_status.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENHEIGHT / 50);

        LayoutParams leftLP = new LayoutParams((int)(0.4 * width), (int)(0.15 * width));
        leftLP.leftMargin = Common.SCREENWIDTH /40;
        leftLP.rightMargin = Common.SCREENWIDTH/80;
        leftLP.topMargin = Common.SCREENHEIGHT / 100;
        leftLP.bottomMargin = Common.SCREENHEIGHT / 90;
        leftLP.gravity = Gravity.CENTER;
        btn_invite_again.setGravity(Gravity.CENTER);

        btn_invite_again.setLayoutParams(leftLP);

        LayoutParams rightLP = new LayoutParams((int)(0.4 * width), (int)(0.15 * width));
        rightLP.leftMargin = Common.SCREENWIDTH / 80;
        rightLP.rightMargin = Common.SCREENWIDTH / 40;
        rightLP.topMargin = Common.SCREENHEIGHT / 100;
        rightLP.bottomMargin = Common.SCREENHEIGHT / 90;
        rightLP.gravity = Gravity.CENTER;
        btn_invite_cancel.setGravity(Gravity.CENTER);

        btn_invite_cancel.setLayoutParams(rightLP);

        LayoutParams centerLP = new LayoutParams((int)(0.4 * width), (int)(0.15 * width));
        centerLP.gravity = Gravity.CENTER;
        btn_call_cancel.setGravity(Gravity.CENTER);

        btn_call_cancel.setLayoutParams(centerLP);
    }

    public class MySelector {
        public StateListDrawable newSelector(Context context, Drawable idNormal, Drawable idPressed) {
            StateListDrawable bg = new StateListDrawable();
            Drawable noraml = idNormal;
            Drawable pressed = idPressed;
            bg.addState(new int[] {android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
            bg.addState(new int[] {android.R.attr.state_enabled}, noraml);
            bg.addState(new int[] {}, noraml);
            return bg;
        }
    }
}
