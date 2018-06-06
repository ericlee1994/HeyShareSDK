package com.shgbit.android.hsaddress.fragment;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shgbit.android.hsaddress.R;
import com.shgbit.android.hsaddress.bean.CancelInviteInfo;
import com.shgbit.android.hsaddress.bean.Meeting;
import com.shgbit.android.hsaddress.bean.MeetingDetail;
import com.shgbit.android.hsaddress.bean.QuiteMeetingInfo;
import com.shgbit.android.hsaddress.bean.TimeoutInfo;
import com.shgbit.android.hsaddress.bean.User;
import com.shgbit.android.hsaddress.bean.UserOrganization;
import com.shgbit.android.hsaddress.callback.ExternalCallBack;
import com.shgbit.android.hsaddress.callback.InternalCallBack;
import com.shgbit.android.hsaddress.sdk.Common;


public class PersonalMeetingFragment extends Fragment {

	private final String Tag = "PersonalMeetingFragment";
	private Meeting meeting;
	private Button mButton;
	private TextView mCallView;
	private TextView mNameView;
	private MyThread thread;
	private boolean mHeartBeatThreadLooper = false;
	private boolean mIsFinishHeartbeat = false;
	private Toast mToast;
	private UserOrganization mPerson;
	private MediaPlayer mMediaPlayer = null;
	private Context mContext;
	private Toast toast;
	private User[] mUsers;
	private boolean isSuccess = false;
	private boolean isPause=false;
	private boolean isBack = false;
	private String LoginName;
	private InternalCallBack mInCallBack;
	private ExternalCallBack mExCallBack;
	private String mResult;
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_personal, null);
		mContext = getActivity();
		initView(v);
		return v;
	}
	private void initView(View v) {
		mCallView = (TextView) v.findViewById(R.id.personal_call);
		mNameView = (TextView) v.findViewById(R.id.personal_meeting_name);
		mButton = (Button) v.findViewById(R.id.personal_meeting_end);
		mCallView.setText(getActivity().getResources().getString(R.string.creating));
		mCallView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 45);
		mNameView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Common.SCREENWIDTH / 30);
		if (mPerson != null) {
			mNameView.setText(mPerson.getDisplayName());
		}
		mButton.setOnClickListener(mClickListener);

//		if (getActivity() instanceof FragmentInteractCallback) {
//			((FragmentInteractCallback) getActivity()).onInvitedUsers(mUsers, true);
//		}
		mInCallBack.onInvitedUsers(mUsers,true);
	}
	public void setExCallBack(ExternalCallBack mExCallBack){
		this.mExCallBack = mExCallBack;
	}
	public void setInCallBack(InternalCallBack mInCallBack){
		this.mInCallBack = mInCallBack;
	}

	 private OnClickListener mClickListener = new OnClickListener() {
    	@Override
		public void onClick(View arg0) {
            if(isSuccess){
				isBack = true;
				CancelInviteInfo cl = new CancelInviteInfo();
				cl.setMeetingId(meeting.getMeetingId());
				cl.setInvitedUser(mPerson.getUserName());
				mExCallBack.onCancelMeeting(cl);
				QuiteMeetingInfo qmInfo = new QuiteMeetingInfo();
				qmInfo.setMeetingId(meeting.getMeetingId());
				mExCallBack.onQuiteMeeting(qmInfo);
				try {
					Finalize ();
				} catch (Throwable e) {
					Log.e(Tag,"mClickListener Throwable: " + "");
				}
				getActivity().getSupportFragmentManager().popBackStack();
//				Syntony.getInstance().des3();
			}else {
				getActivity().getSupportFragmentManager().popBackStack();
//				Syntony.getInstance().des3();
			}
		}
	 };
	private void initVideo(){
    	try {
    		if (mMediaPlayer == null) {
    			mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.callsound);
    		}

    		mMediaPlayer.setLooping(true);
    		
    		mMediaPlayer.setOnErrorListener(new OnErrorListener() {
    			
    			@Override
    			public boolean onError(MediaPlayer mp, int what, int extra) {
    				Log.e(Tag, "MediaPlayer - Error code: " + what + ", Extra code: " + extra);
    				return false;
    			}
    		});
    	} catch (Throwable e) {
    		Log.e(Tag, "initMediaPlayer Throwable:" + e.toString());
    	}
    }

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
		switch (msg.what) {
		case 1:
			toast= Toast.makeText(mContext, mContext.getResources().getString(R.string.tips_33), 1999);
			toast.show();
			sendEmptyMessageDelayed(3, 2000);
			break;
		case 2:
			toast= Toast.makeText(mContext, mContext.getResources().getString(R.string.tips_34), 1999);
			toast.show();
			sendEmptyMessageDelayed(3, 2000);
		    break;
		case 3:
			if(isPause == false){
				((FragmentActivity) mContext).getSupportFragmentManager().popBackStack();
			}
			break;
		default:
			break;
		}
		super.handleMessage(msg);
		}
	};

	@Override
	public void onPause() {
		super.onPause();
		 if (mMediaPlayer != null) {
				mMediaPlayer.pause();
		 }
		 if(toast !=null){
			toast.cancel();
			isPause = true;
  		 }
	}
	@Override
	public void onResume() {
		super.onResume();
		 if (mMediaPlayer != null) {
		       mMediaPlayer.start();
		 }
		 if(toast !=null){
			 toast.cancel();
			 ((FragmentActivity) mContext).getSupportFragmentManager().popBackStack();
		 }
	}
	
	public void setMeetingData(User[] user, UserOrganization userOrganization, String name,InternalCallBack mInCallBack) {
		if (user == null && userOrganization==null ) {
			return;
		}
		mUsers =user;

		LoginName = name;

		mPerson =userOrganization;

		this.mInCallBack = mInCallBack;
	}

	public void setCalling(Meeting pmeeting, boolean isOK){
		if(pmeeting == null){
			return;
		}
		mCallView.setText(getActivity().getResources().getString(R.string.calling));
		meeting = pmeeting;
        isSuccess = isOK;
		initVideo();
		startThread();
	}

	public void showToast(){
		toast = Toast.makeText(mContext, mContext.getResources().getString(R.string.tips_3), Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public void setTimeOut(TimeoutInfo[] timeoutInfos){
		if(isBack == false){
			if(timeoutInfos !=null){
				for(int i=0;i<timeoutInfos.length;i++){
			    	if(timeoutInfos[i].getMeetingId().equals(meeting.getMeetingId())){
			    		if(timeoutInfos[i].getInviter().equals(mPerson.getUserName())){
			    			QuiteMeetingInfo qmInfo = new QuiteMeetingInfo();
			    			qmInfo.setMeetingId(meeting.getMeetingId());
			    			mExCallBack.onQuiteMeeting(qmInfo);
			    			Message msg = new Message();
					    	msg.what = 1;
					    	mHandler.sendMessage(msg);
			    		}
			    	}
			    }
			}
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Finalize ();
	}

	private void startThread() {
		try {
			if (thread != null) {
				mHeartBeatThreadLooper = false;
				mIsFinishHeartbeat = true;
				thread.join(100);
				thread.interrupt();
				mMediaPlayer.stop();
				thread = null;
			}

			thread = new MyThread();
			mHeartBeatThreadLooper = true;
			mIsFinishHeartbeat = false;
			thread.start();
			mMediaPlayer.start();
		} catch (Throwable e) {
			Log.e(Tag, "startThread Throwable:" + "");
		}
	}
	
	public void Finalize () {
		try {
			if (thread != null) {
				mHeartBeatThreadLooper = false;
				mIsFinishHeartbeat = true;
				thread.join(100);
				thread.interrupt();
				thread = null;
			}
		    if (mMediaPlayer != null) {
				mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
			} catch (Throwable e) {
				Log.e(Tag, "finalize Throwable: " + "");
			}
	}

	public void  setResult(String result){
		if(result != null){
			mResult = result;
		}
	}

	public class MyThread extends Thread {
		@Override
		public void run() {
			while (mHeartBeatThreadLooper) {
				if(meeting ==null){
					return;
				}

//				String result = "";
				try {
					mExCallBack.onSyncGetMeeting(meeting.getMeetingId());
				} catch (Throwable e) {
					Log.e(Tag,"MyThread Throwable: " + "");
				}

				MeetingDetail md = null;
				try {
					if(mResult != null){
						md = new Gson().fromJson(mResult, MeetingDetail.class);
					}
				} catch (Throwable e) {
					Log.e(Tag,"MyThread Throwable: " + "");
				}
				if(md != null && mPerson != null){
					if (md.getMeeting().getUsers() != null) {
						for (User user : md.getMeeting().getUsers()) {
							if (user.getUserName().equals(mPerson.getUserName())) {
								Log.e(Tag,"user:"+user.getDisplayName()+"----user status:"+user.getSessionType().getMobileState().getStatus());
								if (user.getSessionType().getMobileState().getStatus().equalsIgnoreCase("joined")) {
//									Intent intent = new Intent();
//									intent.setClass(getActivity(),VideoActivity.class);
//									intent.putExtra("number",meeting.getMeetingId());
//									intent.putExtra("password",meeting.getPassword());
//									intent.putExtra("meetingName",meeting.getMeetingName());
//									intent.putExtra("username",LoginName);
//									getActivity().startActivity(intent);
									mExCallBack.onStartVideo();
									mIsFinishHeartbeat = true;
									getActivity().getSupportFragmentManager().popBackStack();
								}
								if(user.getSessionType().getMobileState().getStatus().equalsIgnoreCase("busy")){
									QuiteMeetingInfo qmInfo = new QuiteMeetingInfo();
									qmInfo.setMeetingId(meeting.getMeetingId());
									mExCallBack.onQuiteMeeting(qmInfo);
									Message msg = new Message();
							    	msg.what = 2;
							    	mHandler.sendMessage(msg);
								}
							}
				        }
					}
				}
				try {
					thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

