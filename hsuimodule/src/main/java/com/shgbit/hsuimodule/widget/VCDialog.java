package com.shgbit.hsuimodule.widget;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

import com.shgbit.hshttplibrary.json.InvitedMeeting;
import com.shgbit.hssdk.sdk.Common;
import com.shgbit.hsuimodule.R;

public class VCDialog extends Dialog {
	private final String TAG = "VCDialog";
	public enum DialogType{Normal,Invite,Message,Handup,Version,Exit,Log,Zip,Upload,UploadSuc,UploadFail,ErrorHangup,Delete,Recall,ReStart,UncatchError,Dis,CleanPZ}
	private DialogType mType = DialogType.Normal;

	private TextView mTxtTital;
	private TextView mTxtMeetingName1;
	private TextView mTxtMeetingName2;
	private TextView mTxtMeetingId1;
	private TextView mTxtMeetingId2;
	private TextView mTxtMeetingTime1;
	private TextView mTxtMeetingTime2;
	private TextView mTxtInviter1;
	private TextView mTxtInviter2;
	private TextView mTxtMeetingNum1;
	private TextView mTxtMeetingNum2;
	private TextView mTxtMeetingMember1;
	private TextView mTxtMeetingMember2;
	private TextView mTxtOk;
	private TextView mTxtCancle;
	private ImageView mImgInviter;
	private ProgressBar mProgressBar;
	private LinearLayout mLlytBg;
	private LinearLayout mLlytImg;
	private LinearLayout mLlytPro;
	private LinearLayout mLlytName;
	private LinearLayout mLlytId;
	private LinearLayout mLlytTime;
	private LinearLayout mLlytNum;
	private LinearLayout mLlytMember;
	private LinearLayout mLlytBtn;
	private Space mSpaceBtn;
	
	private String mTital, mBtn1String, mBtn2String;
	
	private Object mContent;
	
	private Context mContext;
	private MediaPlayer mMediaPlayer = null;

	public VCDialog(Context context, DialogType type) {
		super(context);
		mType = type;
		mContext = context;
	}
	
	public VCDialog(Context context, int theme, DialogType type) {
		super(context, theme);
		mType = type;
		mContext = context;
	}
	
	public void setDisplayWords (String tital, String btn1, String btn2) {
		mTital = tital;
		mBtn1String = btn1;
		mBtn2String = btn2;
	}
	
	public void setContent (Object content) {
		mContent = content;
	}
	
	public Object getContent () {
		return mContent;
	}

	public void setProgress (Object tag) {
		if (mType == DialogType.Upload) {
			int pro = (Integer) tag;
			mProgressBar.setProgress(pro);
			mTxtInviter2.setText(pro + "%");
		}
	}
	
	@Override
	public void show() {
		if (mType == DialogType.Invite) {
//			if (VCNotification.isBackground(mContext) == false) {
				initMediaPlayer();
//			}
			mHandler.sendEmptyMessageDelayed(0x222, 30 * 1000);
		}
		super.show();
	}
	
	@Override
	public void dismiss() {
		if (mType == DialogType.Invite) {
			if (mMediaPlayer != null) {
				mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
		}
		super.dismiss();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_hint);
		initView();
		
		Window win = getWindow();
		WindowManager.LayoutParams lp = win.getAttributes();
		win.setGravity(Gravity.CENTER);
		if (mType == DialogType.Handup || mType == DialogType.CleanPZ){
			lp.width = (int) (Common.SCREENHEIGHT*0.7);
			lp.height = (int) (Common.SCREENHEIGHT*0.4);
		} else {
			lp.width = (int) (Common.SCREENHEIGHT*0.8);
			lp.height = (int) (Common.SCREENHEIGHT*0.8);
		}
		win.setAttributes(lp);
	}
	
	private void initView () {
		mTxtTital = (TextView) findViewById(R.id.txt_tital_dialog);
		mTxtMeetingName1 = (TextView) findViewById(R.id.txt_meetingname1_dialog);
		mTxtMeetingName2 = (TextView) findViewById(R.id.txt_meetingname2_dialog);
		mTxtMeetingId1 = (TextView) findViewById(R.id.txt_meetingid1_dialog);
		mTxtMeetingId2 = (TextView) findViewById(R.id.txt_meetingid2_dialog);
		mTxtMeetingTime1 = (TextView) findViewById(R.id.txt_meetingtime1_dialog);
		mTxtMeetingTime2 = (TextView) findViewById(R.id.txt_meetingtime2_dialog);
		mTxtMeetingNum1 = (TextView) findViewById(R.id.txt_meetingnum1_dialog);
		mTxtMeetingNum2 = (TextView) findViewById(R.id.txt_meetingnum2_dialog);
		mTxtMeetingMember1 = (TextView) findViewById(R.id.txt_meetingmember1_dialog);
		mTxtMeetingMember2 = (TextView) findViewById(R.id.txt_meetingmember2_dialog);
		mImgInviter = (ImageView) findViewById(R.id.img_inviter_dialog);
		mTxtInviter1 = (TextView) findViewById(R.id.txt_meetinginviter1_dialog);
		mTxtInviter2 = (TextView) findViewById(R.id.txt_meetinginviter2_dialog);
		mTxtOk = (TextView) findViewById(R.id.txt_end_ok_dialog);
		mTxtCancle = (TextView) findViewById(R.id.txt_cancle_dialog);
		mLlytBg = (LinearLayout) findViewById(R.id.llyt_bg_dialog);
		mLlytImg = (LinearLayout) findViewById(R.id.llyt_img_dialog);
		mLlytBtn = (LinearLayout) findViewById(R.id.llyt_btn_dialog);
		mSpaceBtn = (Space) findViewById(R.id.space_dialog);
		mLlytName = (LinearLayout) findViewById(R.id.llyt_name_dialog);
		mLlytId = (LinearLayout) findViewById(R.id.llyt_id_dialog);
		mLlytTime = (LinearLayout) findViewById(R.id.llyt_time_dialog);
		mLlytNum = (LinearLayout) findViewById(R.id.llyt_num_dialog);
		mLlytMember = (LinearLayout) findViewById(R.id.llyt_member_dialog);
		mLlytPro = (LinearLayout) findViewById(R.id.llyt_pro_dialog);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_dialog);
		
		mTxtOk.setOnClickListener(mClickListener);
		mTxtCancle.setOnClickListener(mClickListener);
		
		mTxtTital.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 30);
		mTxtMeetingName1.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 55);
		mTxtMeetingName2.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 40);
		mTxtMeetingId1.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 55);
		mTxtMeetingId2.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 55);
		mTxtMeetingTime1.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 55);
		mTxtMeetingTime2.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 55);
		mTxtMeetingNum1.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 55);
		mTxtMeetingNum2.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 55);
		mTxtMeetingMember1.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 55);
		mTxtMeetingMember2.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 55);
		mTxtInviter1.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 55);
		mTxtInviter2.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 40);
		mTxtOk.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 40);
		mTxtCancle.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 40);
		
		mTxtTital.setText(mTital);
		mTxtOk.setText(mBtn1String);
		mTxtCancle.setText(mBtn2String);
		
		try {
			if (mType == DialogType.Handup || mType == DialogType.CleanPZ){
				mTxtTital.setText(mContext.getString(R.string.tital_tip));
				mTxtOk.setText(mContext.getString(R.string.tital_ok));
				mTxtCancle.setText(mContext.getString(R.string.tital_cancel));
				mTxtInviter2.setText((String)mContent);
				mLlytBg.setBackgroundResource(R.drawable.bg_dialog_h);
				findViewById(R.id.llyt_content_dialog).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2.5f));
				findViewById(R.id.space_bottom_dialog).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
			} else if (mType == DialogType.Normal || mType == DialogType.Exit || mType == DialogType.ErrorHangup || mType == DialogType.Delete || mType == DialogType.ReStart) {
				mTxtTital.setText(mContext.getString(R.string.tital_tip));
				mTxtOk.setText(mContext.getString(R.string.tital_ok));
				mTxtCancle.setText(mContext.getString(R.string.tital_cancel));
				mTxtInviter2.setText((String)mContent);
			} else if (mType == DialogType.Invite) {
				mTxtTital.setText(mContext.getString(R.string.tital_invite));
				mTxtMeetingName2.setText(((InvitedMeeting)mContent).getMeetingName());
				mTxtMeetingId2.setText(mContext.getString(R.string.tital_meetingid) + ((InvitedMeeting)mContent).getMeetingId());
				mTxtInviter2.setText(((InvitedMeeting)mContent).getInviterDisplayName());
				mTxtOk.setBackgroundResource(R.drawable.btn_accept);
				mTxtCancle.setBackgroundResource(R.drawable.btn_refuse);
				mLlytName.setVisibility(View.VISIBLE);
				mLlytId.setVisibility(View.VISIBLE);
				mTxtMeetingId1.setVisibility(View.GONE);
				mLlytImg.setVisibility(View.VISIBLE);
				mImgInviter.setImageResource(R.drawable.inviter);
			}
			 else if (mType == DialogType.Recall) {
				mTxtTital.setText(mContext.getString(R.string.tital_tip));
				mTxtOk.setText(mContext.getString(R.string.tital_ok));
				mTxtCancle.setText(mContext.getString(R.string.tital_cancel));
				mTxtInviter2.setText(R.string.disconnect);
			} else if (mType == DialogType.UncatchError) {
				mTxtTital.setText(mContext.getString(R.string.error_back));
				mTxtOk.setText(mContext.getString(R.string.tital_log));
				mTxtCancle.setText(mContext.getString(R.string.tital_cancel));
				mTxtInviter2.setText((String)mContent);
				mTxtInviter2.setGravity(Gravity.CENTER_VERTICAL);
			}
		} catch (Throwable e) {
			Log.e(TAG, "mTxtContent settext Throwable: " + e.toString());
		}

	}

	private View.OnClickListener mClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			int id = arg0.getId();
			if (id == R.id.txt_end_ok_dialog) {
				dismiss();
				if (mCallback != null) {
					mCallback.onOk(mType, mContent);
				}
			}else if (id == R.id.txt_cancle_dialog){
				dismiss();
				if (mCallback != null) {
					mCallback.onCancel(mType, mContent);
				}
			}
//			switch (arg0.getId()) {
//			case R.id.txt_ok_dialog:
//				dismiss();
//				if (mCallback != null) {
//					mCallback.onOk(mType, mContent);
//				}
//				break;
//			case R.id.txt_cancle_dialog:
//				dismiss();
//				if (mCallback != null) {
//					mCallback.onCancel(mType, mContent);
//				}
//				break;
//			default:
//				break;
//			}
		}};
		
		private Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (isShowing()) {
					dismiss();
				}
				super.handleMessage(msg);
			}
			
		};
		
		private void initMediaPlayer() {
			try {
				if (mMediaPlayer == null) {
					mMediaPlayer = MediaPlayer.create(mContext, R.raw.invitesound);
				}

				mMediaPlayer.start();
				mMediaPlayer.setLooping(true);
				
				mMediaPlayer.setOnErrorListener(new OnErrorListener() {
					
					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						Log.e(TAG, "MediaPlayer - Error code: " + what + ", Extra code: " + extra);
						return false;
					}
				});
			} catch (Throwable e) {
				Log.e(TAG, "initMediaPlayer Throwable:" + e.toString());
			}
		}
		
		private DialogCallback mCallback;
		public void setDialogCallback (DialogCallback callback) {
			mCallback = callback;
		}
		public interface DialogCallback {
			void onOk(DialogType type, Object object);
			void onCancel(DialogType type, Object object);
		}

}
