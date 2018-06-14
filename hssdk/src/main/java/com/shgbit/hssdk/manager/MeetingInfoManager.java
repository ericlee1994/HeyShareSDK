package com.shgbit.hssdk.manager;

import android.util.Log;

import com.ainemo.sdk.otf.VideoInfo;
import com.google.gson.Gson;
import com.shgbit.hshttplibrary.ServerInteractManager;
import com.shgbit.hssdk.bean.Cmd;
import com.shgbit.hssdk.bean.Command;
import com.shgbit.hssdk.bean.CurrentMeetingInfo;
import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hssdk.bean.Net_Status;
import com.shgbit.hssdk.bean.Status;
import com.shgbit.hssdk.bean.SessionType;
import com.shgbit.hssdk.callback.VideoUpdateListener;
import com.shgbit.hssdk.json.User;
import com.shgbit.hssdk.sdk.VideoCtrl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeetingInfoManager {

	private String TAG = "MeetingInfoManager";
	private static volatile MeetingInfoManager mInstance;

	private boolean mMeetingInfoThreadLoop = false;
	private MeetingInfoThread mThread;

//	private List<MemberInfo> mTotalMember;
	private ArrayList<MemberInfo> mScreenMember;
	private ArrayList<MemberInfo> mOtherMember;
	private ArrayList<MemberInfo> mUnjoinedMember;

	private ArrayList<Command> mCommand;
	private ArrayList<MemberInfo> mNemoData;
	private ArrayList<MemberInfo> mServerData;
	private User[] mServerUsers;

	private boolean mNemoUpdate;

	private int SCREEN_NUMBER = 6;

	private int mDuration = 0;
	private String mMeetingName;
	private String mUserName;

	private int mTotalSize = 0;
	private int mJoinSize = 0;

	private boolean isAudioMode;
	private boolean isExChange;
	private boolean isPopDown;
	private boolean isPopUp;
	private boolean isPopUpDown;

	private boolean isMemberContent;
	private boolean isUvc;

	private MeetingInfoManager(){}

//	private static MeetingInfoManager getInstance() {
//		if (mInstance == null) {
//			mInstance = new MeetingInfoManager();
//		}
//		return mInstance;
//	}

//	public void destory() {
//		if (mInstance != null) {
//			mInstance.finalize();
//			mInstance = null;
//		}
//	}

	public static void registerInstance() {
		if (mInstance == null) {
			mInstance = new MeetingInfoManager();
		}

		VideoCtrl.setMeetingInfoManager(mInstance);
	}

	public void init(String userName){
		Log.i(TAG, "MeetingInfoManager init userName=" + userName);

		mUserName = userName;

		mNemoUpdate = false;
		isAudioMode = false;
		isExChange = false;
		isPopDown = false;
		isPopUp = false;
		isPopUpDown = false;
		isMemberContent = false;

		mScreenMember = new ArrayList<>();
		mOtherMember = new ArrayList<>();
		mUnjoinedMember = new ArrayList<>();

		mNemoData = new ArrayList<>();
		mServerData = new ArrayList<>();

		mCommand = new ArrayList<>();

//		mTotalMember = new ArrayList<>();

		mMeetingInfoThreadLoop = true;
		mThread = new MeetingInfoThread();
		mThread.start();
	}

	public void reset() {
		mNemoUpdate = false;
		isAudioMode = false;
		isExChange = false;
		isPopDown = false;
		isPopUp = false;
		isPopUpDown = false;
		isMemberContent = false;

		mScreenMember.clear();
		mOtherMember.clear();
		mUnjoinedMember.clear();
		mNemoData.clear();
		mServerData.clear();
		mCommand.clear();
//		mTotalMember.clear();

	}

	public void finalize() {

		try {
			mMeetingInfoThreadLoop = false;
			if (mThread != null) {
//				mThread.join(100);
				mThread.interrupt();
				mThread = null;
			}

			if (mScreenMember != null){
				mScreenMember.clear();
				mScreenMember = null;
			}
			if (mOtherMember != null){
				mOtherMember.clear();
				mOtherMember = null;
			}
			if (mUnjoinedMember != null){
				mUnjoinedMember.clear();
				mUnjoinedMember = null;
			}

			if (mServerData != null){
				mServerData.clear();
				mServerData = null;
			}
			if (mNemoData != null){
				mNemoData.clear();
				mNemoData = null;
			}

			if (mCommand != null){
				mCommand.clear();
				mCommand = null;
			}

//			if (mTotalMember != null){
//				mTotalMember.clear();
//				mTotalMember = null;
//			}

			if (mInstance != null) {
				mInstance = null;
			}
		} catch (Throwable e) {
			Log.e(TAG, "Finalize Throwable:" + e.toString());
		}

	}

	private class MeetingInfoThread extends Thread {

		@Override
		public void run() {

			try {
				while (mMeetingInfoThreadLoop) {
					mNemoUpdate = false;

					if (!getCurrentMeeting()){
						Log.e(TAG, "GetCurrentMeeting is null");
						if (mServerData != null && mServerData.size() > 0){
							for (MemberInfo memberInfo : mServerData){
								memberInfo.setNet_status(Net_Status.LOST);
								memberInfo.setDataSourceID("");
							}
						}
					}
					if (mMeetingInfoUpdateListener != null) {
						mMeetingInfoUpdateListener.onMemberSizeChanged(mJoinSize, mTotalSize);
					}
					List<MemberInfo> members = mergeServerNemo(mServerData, mNemoData);
					checkUnJoinedList(members);

					if (checkinList(members)) {
						callBack();
					}

					if (operateCommand()) {
						callBack();
					}

					try {

						for (int i = 0; i < 100; i++) {

							if (mNemoUpdate) {
								Log.i(TAG, "Found nemo update");
								break;
							}

							if (mCommand != null && mCommand.size() > 0) {
								Log.i(TAG, "Found new command");
								break;
							}

							if (!mMeetingInfoThreadLoop) {
								return;
							}

							Thread.sleep(50);
						}
					} catch (Throwable e) {
						Log.e(TAG, "MeetingInfoThread Throwable1:" + e.getMessage());
					}
				}
			} catch (Throwable e){
				Log.e(TAG, "MeetingInfoThread Throwable2:" + e.toString());
			}

		}
	}

	private boolean getCurrentMeeting() {

		CurrentMeetingInfo cmi = null;
		try {
			String result = ServerInteractManager.getInstance().getSyncCurrentMeeting();

			cmi = new Gson().fromJson(result, CurrentMeetingInfo.class);

			if (cmi == null || cmi.getMeeting() == null || cmi.getMeeting().getUsers() == null) {
				Log.e(TAG, "---GetCurrentMeeting--- result:" + result);
				return false;
			}

			mServerUsers = cmi.getMeeting().getUsers();
			mDuration = cmi.getMeeting().getDuration();
			mMeetingName = cmi.getMeeting().getMeetingName();
			mTotalSize = mServerUsers.length;
			mServerData.clear();
			mJoinSize = 0;
			Log.i(TAG, "---GetCurrentMeeting---,totalSize=" + mTotalSize + ",meetingName="+ mMeetingName);

			for (int i = 0; i < mServerUsers.length; i++) {
				boolean isJoin = false;
				User item = mServerUsers[i];
				if (item.getStatus() == null){
					if (isMemberInList(mUnjoinedMember,item.getUserName()) == -1){
						MemberInfo noStatus = new MemberInfo();
						noStatus.setId(item.getUserName());
						noStatus.setDisplayName(item.getDisplayName());
						noStatus.setStatus(Status.WAITING);
						noStatus.setNet_status(Net_Status.NULL);
						mUnjoinedMember.add(noStatus);
					}
				} else {
					// joined
					if (item.getSessionType().getContentOnlyState() != null){
						if (item.getSessionType().getContentOnlyState().getStatus().equals(JOINED)){
							isJoin = true;
							MemberInfo m = new MemberInfo();
							m.setId(item.getUserName());
							m.setDisplayName(item.getDisplayName());
							m.setStatus(Status.JOINED);
							m.setSessionType(SessionType.CONTENTONLY);
							m.setNet_status(Net_Status.LOST);
							if (item.getUserName().equals(mUserName)) {
								m.setLocal(true);
							}

							mServerData.add(m);
						}
					}
					if (item.getSessionType().getMobileState() != null){
						if (item.getSessionType().getMobileState().getStatus().equals(JOINED)){
							isJoin = true;
							MemberInfo m = new MemberInfo();
							m.setId(item.getUserName());
							m.setDisplayName(item.getDisplayName());
							m.setStatus(Status.JOINED);
							m.setSessionType(SessionType.MOBILE);
							m.setNet_status(Net_Status.LOST);
							if (item.getUserName().equals(mUserName)) {
								m.setLocal(true);
								m.setDataSourceID("LocalPreviewID");
								m.setNet_status(Net_Status.NORMAL);
							}

							mServerData.add(m);
						}
					}
					if (item.getSessionType().getPCState() != null){
						if (item.getSessionType().getPCState().getStatus().equals(JOINED)){
							isJoin = true;
							MemberInfo m = new MemberInfo();
							m.setId(item.getUserName());
							m.setDisplayName(item.getDisplayName());
							m.setStatus(Status.JOINED);
							m.setSessionType(SessionType.PC);
							m.setNet_status(Net_Status.LOST);
							if (item.getUserName().equals(mUserName)) {
								m.setLocal(true);
							}

							mServerData.add(m);
						}
					}
					if (isJoin){
						// joined
						mJoinSize++;
					} else {
						// not join .
						if (isMemberInList(mUnjoinedMember,item.getUserName()) == -1){
							MemberInfo unJoined = new MemberInfo();
							unJoined.setId(item.getUserName());
							unJoined.setDisplayName(item.getDisplayName());
//							unJoined.setStatus(changeToStatus(item.getStatus()));
							unJoined.setStatus(Status.WAITING);
							unJoined.setNet_status(Net_Status.NULL);
							mUnjoinedMember.add(unJoined);
						}
					}
				}
			}
			Log.i(TAG, "---GetCurrentMeeting---,mServerData.size=" + mServerData.size());

			//mServerData 把自己放最后一个(自己在第一个的情况下)
			if (mServerData.size() > 1){
				if (mServerData.get(0).getId().equals(mUserName) && mServerData.get(0).getSessionType() == SessionType.MOBILE){
					mServerData.add(mServerData.get(0));
					mServerData.remove(0);
				}
			}

		} catch (Throwable e) {
			Log.e(TAG, "GetCurrentMeeting Throwable: " + e.toString());
		}

		return true;
	}

	public void nemoChange(List<VideoInfo> videoInfos, String localSId) {

		try {
			if (videoInfos != null) {
				Log.i(TAG, "---NemoChange---,videoinfo.size=" + videoInfos.size());
				for (int i = 0; i < videoInfos.size(); i++) {
					VideoInfo item = videoInfos.get(i);
                    Log.i(TAG, "---NemoChange---,i=" + i + ",RemoteName:" + item.getRemoteName()  + ",isContent=" + item.isContent() + ",DataSource=" + item.getDataSourceID()  + ",isVideoMute=" + item.isVideoMute());
				}
			}

			mNemoData.clear();

			if (videoInfos != null) {
				for (VideoInfo videoInfo : videoInfos) {
					String remoteName = videoInfo.getRemoteName();
					String id = null;
					String sessionType = null;

					if (remoteName.contains("?t_")) {
						String[] rns = remoteName.split("[?]t_");
						id = rns[0];
						sessionType = rns[1];
					} else {
						id = videoInfo.getRemoteName();
						sessionType = UNKNOW;
					}

					MemberInfo m = new MemberInfo();
					m.setLocal(false);
					m.setId(id);
					m.setRemoteName(id);
					m.setParticipantId(videoInfo.getParticipantId());
					m.setDataSourceID(videoInfo.getDataSourceID());
					m.setContent(videoInfo.isContent());
					m.setAudioMute(videoInfo.isAudioMute());
					m.setVideoMute(videoInfo.isVideoMute());
					m.setSessionType(changeToSessionType(sessionType));
					//关闭视频
					if (videoInfo.isVideoMute()){
						m.setNet_status(Net_Status.VIDEO_MUTE);
					}

					if (videoInfo.isContent()) {
						if (sessionType.equals(PC)){
//							m.setSessionType(changeToSessionType("pc_content"));
							m.setSessionType(SessionType.PC_CONTENT);
						}
//						m.setId(id);
//						m.setDisplayName(getDisplayName(id));
					} else {
						if (m.getSessionType() == SessionType.CONTENTONLY){
							m.setNet_status(Net_Status.CONTENTONLY_UNSEND);
//							m.setId(id);
//							m.setDisplayName(getDisplayName(id));
//						} else {
//							m.setId(id);
						}
					}

					mNemoData.add(m);

					//sb join,mUnjoinedMember remove
					int index = isMemberInList(mUnjoinedMember, id);
					if (index != -1){
						mUnjoinedMember.remove(index);
					}
//					Log.i(TAG, "mNemoData.add("+ m.getRemoteName() +")");
				}
			}

			MemberInfo m = new MemberInfo();
			m.setLocal(true);
			m.setParticipantId(-1);
			m.setDataSourceID(localSId);
			m.setId(mUserName);
			m.setRemoteName(mUserName);
			m.setContent(false);
			m.setAudioMute(false);
			m.setVideoMute(false);
			m.setUvc(isUvc);
			m.setSessionType(SessionType.MOBILE);

			mNemoData.add(m);
			ArrayList<String> ids = new ArrayList<>();
			for (MemberInfo memberInfo : mNemoData){
				if (memberInfo.getSessionType() == SessionType.CONTENTONLY && memberInfo.getDataSourceID() != null && !memberInfo.getDataSourceID().isEmpty()){
					ids.add(memberInfo.getId());
				}
			}
			if (ids != null && ids.size() > 0){
				for (String id : ids){
					for (int i = 0; i < mNemoData.size(); i++){
						if (id.equalsIgnoreCase(mNemoData.get(i).getId())){
							if (mNemoData.get(i).getDataSourceID() == null || mNemoData.get(i).getDataSourceID().isEmpty()){
								mNemoData.remove(i);
								i--;
							}
						}

					}
				}
			}
			Log.i(TAG, "---NemoChange---,mNemoData.size=" + mNemoData.size());
			mNemoUpdate = true;

		} catch (Throwable e) {
			Log.e(TAG, "NemoChange Throwable:" + e.toString());
		}
	}

	private List<MemberInfo> mergeServerNemo(List<MemberInfo> server, List<MemberInfo> nemo) {

		try {
			List<MemberInfo> result = new ArrayList<>();
			if (server != null && server.size() > 0){
				result.addAll(server);
			}
			Log.i(TAG, "---MergeServerNemo---,result.size()=" + result.size());
			for (int i = 0; i < nemo.size(); i++) {
				MemberInfo n = nemo.get(i);
				if (n.getNet_status() != Net_Status.VIDEO_MUTE
						&& n.getNet_status() != Net_Status.CONTENTONLY_UNSEND
						&& n.getNet_status() != Net_Status.AUDIO_MODE){
					if (n.getDataSourceID() != null && !n.getDataSourceID().isEmpty()) {
						n.setNet_status(Net_Status.NORMAL);
					} else {
						n.setNet_status(Net_Status.LOADING);
					}
				}

				int index = isMemberInList(result, n);
				if (index == -1) {
					if (n.getDisplayName() == null || n.getDisplayName().isEmpty()) {
						if (n.getSessionType().equals(SessionType.UNKNOW)){
								n.setDisplayName(n.getRemoteName());
							} else {
								n.setDisplayName(getDisplayName(n.getRemoteName()));
							}
					}
					n.setStatus(Status.JOINED);
					result.add(n);
					Log.i(TAG, "---MergeServerNemo---add nemo=" + n.getDisplayName() + ",type=" + n.getSessionType());
				} else {
					MemberInfo s = result.get(index);
					s.setLocal(n.isLocal());
					if (s.isLocal()){
						s.setUvc(isUvc);
					}
					s.setAudioMute(n.isAudioMute());
					s.setContent(n.isContent());
					s.setDataSourceID(n.getDataSourceID());
					s.setParticipantId(n.getParticipantId());
					s.setRemoteName(n.getRemoteName());
					s.setVideoMute(n.isVideoMute());
					s.setNet_status(n.getNet_status());
					Log.i(TAG, "---MergeServerNemo---exist nemo=" + s.getDisplayName() + ",type=" + s.getSessionType());
				}
			}
			Log.i(TAG, "---MergeServerNemo---,result.size="+result.size());
			//语音模式
			if (isAudioMode){
				for (int i = 0;i < result.size(); i++){
					if (result.get(i).getStatus().equals(Status.JOINED)){
						result.get(i).setNet_status(Net_Status.AUDIO_MODE);
					}
				}
			}

			return result;

		} catch (Throwable e) {
			Log.e(TAG, "MergeServerNemo Throwable:" + e.toString());
		}

		return null;
	}

	private void checkUnJoinedList(List<MemberInfo> list){
		if (list == null || mUnjoinedMember == null || mUnjoinedMember.size() <= 0) {
			return;
		}
		for (int i = mUnjoinedMember.size() - 1;  i >= 0; i--){
			int index = isMemberInList(list, mUnjoinedMember.get(i).getId());
			if (index != -1){
				mUnjoinedMember.remove(i);
			}
		}
	}

	private boolean checkinList(List<MemberInfo> list) {

		boolean needUpdate = false;

		if (list == null || mScreenMember == null || mOtherMember == null) {
			return false;
		}
		Log.i(TAG, "---CheckinList---");
		try {
			for (int i = 0; i < mScreenMember.size(); i++) {
				int index = isMemberInList(list, mScreenMember.get(i));
//				Log.e(TAG, "CheckinList index=" + index + ",screen id=" + mScreenMember.get(i).getId() + ",sessiontype=" + mScreenMember.get(i).getSessionType()  + ",screen status=" + mScreenMember.get(i).getStatus());
				if (index == -1){
					// Screen Member exit
					if (mScreenMember.get(i).getStatus().equals(Status.JOINED)) {
						mScreenMember.remove(i);
						i--;
						needUpdate = true;
					}
				} else {
					// Update Screen Member
					if (updateInfo(mScreenMember.get(i), list.get(index))) {
						needUpdate = true;
					}
				}
			}

			for (int i = 0; i < mOtherMember.size(); i++) {
				int index = isMemberInList(list, mOtherMember.get(i));
				if (index == -1){
					// Other Member exit
					if (mOtherMember.get(i).getStatus().equals(Status.JOINED)) {
						mOtherMember.remove(i);
						i--;
						needUpdate = true;
					}
				} else {
					// Update Other Member
					if (updateInfo(mOtherMember.get(i), list.get(index))) {
						needUpdate = true;
					}
				}
			}

			for (int i = 0; i < list.size(); i++) {

				MemberInfo item = list.get(i);

				int index_screen = isMemberInList(mScreenMember, item);
				int index_other = isMemberInList(mOtherMember, item);
				int screen_len = mScreenMember.size();

				// New Member
				if (index_screen == -1 && index_other == -1) {
					Log.i(TAG, "---CheckinList---,New Member,i="+i + ",id=" + item.getId());
					if (item.isContent()) {
						mScreenMember.add(0, item);
						while (mScreenMember.size() > SCREEN_NUMBER) {
							MemberInfo info = mScreenMember.get(mScreenMember.size() - 1);
							mScreenMember.remove(mScreenMember.size() - 1);
							mOtherMember.add(0, info);
						}
						needUpdate = true;
					} else {
						if (screen_len < SCREEN_NUMBER) {
							mScreenMember.add(item);
//							Log.i(TAG, "mScreenMember.add(" + item.getUserName() + ")");
							needUpdate = true;
						} else {
							mOtherMember.add(item);
//							Log.i(TAG, "mOtherMember.add(" + item.getUserName() + ")");
							needUpdate = true;
						}
					}
				}
			}
		} catch (Throwable e) {
			Log.e(TAG, "CheckinList exception:" + e.toString());
		}

		return needUpdate;
	}

	private boolean operateCommand() {
		boolean needUpdate = false;

		if (mCommand == null || mCommand.size() <= 0){
			return false;
		}

		while (mCommand.size() > 0) {
			try {
				Command cmd = mCommand.get(0);
				Object[] args = cmd.getArgs();
				Log.i(TAG, "---OperateCommand---,cmd=" + cmd.getName());
				if (cmd.getName().equals(Cmd.SCREEN_EXCHANGE)) {

					int index1, index2;
					if (args[0] == null || ((String) args[0]).isEmpty()) {
						index1 = 0;
					} else {
						index1 = isMemberInList(mScreenMember, (String) args[0], (SessionType) args[2]);
					}
					index2 = isMemberInList(mScreenMember, (String) args[1], (SessionType) args[3]);

					if (index1 != -1 && index2 != -1) {
						Collections.swap(mScreenMember, index1, index2);
						isExChange = true;
						needUpdate = true;
					}

				} else if (cmd.getName().equals(Cmd.MODE_CHANGE)) {
					if (mScreenMember.size() != 0 || mOtherMember.size() != 0 || mUnjoinedMember.size() != 0){
						SCREEN_NUMBER = (Integer) args[0];
						getModeList();
						needUpdate = true;
					}
				} else if (cmd.getName().equals(Cmd.POP_DOWN)) {

					int index = isMemberInList(mScreenMember, (String) args[0], (SessionType) args[1]);

					if (index != -1) {
						MemberInfo m = mScreenMember.get(index);
						mOtherMember.add(m);
						mScreenMember.remove(index);
						isPopDown = true;
						needUpdate = true;
					}

				} else if (cmd.getName().equals(Cmd.POP_UP)) {

					int index = isMemberInList(mOtherMember, (String) args[0], (SessionType) args[1]);

					if (index != -1 && mScreenMember.size() < SCREEN_NUMBER) {
						MemberInfo m = mOtherMember.get(index);
						mScreenMember.add(m);
						mOtherMember.remove(index);
						isPopUp = true;
						needUpdate = true;
					}

				} else if(cmd.getName().equals(Cmd.Pop_UpDown)){

					if (isMemberInList(mOtherMember,(String)args[0], (SessionType) args[1]) == -1){
						//popdown
						int indexDown = isMemberInList(mScreenMember, (String) args[0], (SessionType) args[1]);

						if (indexDown != -1) {
							MemberInfo m = mScreenMember.get(indexDown);
							mOtherMember.add(m);
							mScreenMember.remove(indexDown);
							isPopUpDown = true;
							needUpdate = true;
						}
					}else {
						//popup
						int indexUp = isMemberInList(mScreenMember, (String) args[0], (SessionType) args[1]);

						if (indexUp == -1 && mScreenMember.size() < SCREEN_NUMBER) {
							MemberInfo m = mOtherMember.get(indexUp);
							mScreenMember.add(m);
							mOtherMember.remove(indexUp);
							isPopUpDown = true;
							needUpdate = true;
						}
					}

				} else if (cmd.getName().equals(Cmd.AUDIO_MUTE)) {
					boolean audioMute = (Boolean) args[0];
					for (int i = 0; i < mScreenMember.size(); i++) {
						if (mScreenMember.get(i).isLocal()) {
							mScreenMember.get(i).setAudioMute(audioMute);
							needUpdate = true;
							break;
						}
					}

					for (int i = 0; i < mOtherMember.size(); i++) {
						if (mOtherMember.get(i).isLocal()) {
							mOtherMember.get(i).setAudioMute(audioMute);
							needUpdate = true;
							break;
						}
					}

				} else if (cmd.getName().equals(Cmd.VIDEO_MUTE)) {
					boolean videoMute = (Boolean) args[0];
					for (int i = 0; i < mScreenMember.size(); i++) {
						if (mScreenMember.get(i).isLocal()) {
							mScreenMember.get(i).setVideoMute(videoMute);
							if (isAudioMode){
								mScreenMember.get(i).setNet_status(Net_Status.AUDIO_MODE);
							} else {
								if (videoMute){
									mScreenMember.get(i).setNet_status(Net_Status.VIDEO_MUTE);
								} else {
									mScreenMember.get(i).setNet_status(Net_Status.NORMAL);
								}
							}
							needUpdate = true;
							break;
						}
					}

					for (int i = 0; i < mOtherMember.size(); i++) {
						if (mOtherMember.get(i).isLocal()) {
							mOtherMember.get(i).setVideoMute(videoMute);
							if (isAudioMode){
								mOtherMember.get(i).setNet_status(Net_Status.AUDIO_MODE);
							} else {
								if (videoMute){
									mOtherMember.get(i).setNet_status(Net_Status.VIDEO_MUTE);
								} else {
									mOtherMember.get(i).setNet_status(Net_Status.NORMAL);
								}
							}
							needUpdate = true;
							break;
						}
					}

				} else if(cmd.getName().equals(Cmd.AUDIO_MODE)){
					isAudioMode = (Boolean) args[0];
					for (int i = 0; i < mScreenMember.size(); i++) {
						if (mScreenMember.get(i).getStatus() == Status.JOINED && isAudioMode){
							mScreenMember.get(i).setNet_status(Net_Status.AUDIO_MODE);
							needUpdate = true;
						}
					}
					for (int i = 0; i < mOtherMember.size(); i++) {
						if (mOtherMember.get(i).getStatus() == Status.JOINED && isAudioMode){
							mOtherMember.get(i).setNet_status(Net_Status.AUDIO_MODE);
							needUpdate = true;
						}
					}
				} else if (cmd.getName().equals(Cmd.STATE_CHANGE)) {
					String id = (String) args[0];
					Status status = (Status) args[1];
					int indexScreen = isMemberInList(mScreenMember, id);
					int indexOther = isMemberInList(mOtherMember, id);

					if (indexScreen == -1 && indexOther == -1) {
						if (status == Status.INVITING){
							// not join
							MemberInfo m = new MemberInfo();
							m.setId(id);
							m.setRemoteName(id);
							m.setDisplayName(getDisplayName(id));
							m.setStatus(status);
							m.setNet_status(Net_Status.NULL);

							if (mScreenMember.size() < SCREEN_NUMBER) {
								mScreenMember.add(m);
							} else {
								mOtherMember.add(m);
							}
							if (isMemberInList(mUnjoinedMember, id) == -1){
								mUnjoinedMember.add(m);
							}
							Log.i(TAG, "---OperateCommand---,mScreenMember.size=" + mScreenMember.size() +
									",mOtherMember.size=" + mOtherMember.size() + ",mUnjoinedMember.size=" + mUnjoinedMember.size());
							needUpdate = true;
						}

					} else if (indexScreen != -1 && indexOther == -1) {
						// inside 4 (not join)
						MemberInfo m = mScreenMember.get(indexScreen);
						if (status != Status.JOINED){
							m.setStatus(status);
							if (status == Status.WAITING){
								mScreenMember.remove(indexScreen);
							}
							needUpdate = true;
						}

//						if (status.equals(Status.WAITING)) {
////							m.setStatus(changeToStatus("waiting"));
//							m.setStatus(Status.WAITING);
//							mScreenMember.remove(indexScreen);
//							needUpdate = true;
//						} else if (status.equals(Status.TIMEOUT)) {
////							m.setStatus(changeToStatus("invitetimeout"));
//							m.setStatus(Status.TIMEOUT);
//							needUpdate = true;
//						} else if (status.equals(Status.BUSY)) {
////							m.setStatus(changeToStatus("busy"));
//							m.setStatus(Status.BUSY);
//							needUpdate = true;
//						} else if (status == Status.INVITING) {
////							m.setStatus(changeToStatus("inviting"));
//							m.setStatus(Status.INVITING);
//							needUpdate = true;
//						}
					} else if (indexScreen == -1 && indexOther != -1) {
						// inside other (not join)
						MemberInfo m = mOtherMember.get(indexOther);
						if (status != Status.JOINED){
							m.setStatus(status);
							if (status == Status.WAITING){
								mOtherMember.remove(indexOther);
							} else if (status == Status.INVITING){
								if (mScreenMember.size() < SCREEN_NUMBER){
									mScreenMember.add(m);
									mOtherMember.remove(indexOther);
								}
							}
							needUpdate = true;
						}
//						if (status.equals(Status.WAITING)) {
////							m.setStatus(changeToStatus("waiting"));
//							m.setStatus(Status.WAITING);
//							mOtherMember.remove(indexOther);
//							needUpdate = true;
//						} else if (status.equals(Status.TIMEOUT)) {
////							m.setStatus(changeToStatus("invitetimeout"));
//							m.setStatus(Status.TIMEOUT);
//							needUpdate = true;
//						} else if (status.equals(Status.BUSY)) {
////							m.setStatus(changeToStatus("busy"));
//							m.setStatus(Status.BUSY);
//							needUpdate = true;
//						} else if (status.equals(Status.INVITING)) {
////							m.setStatus(changeToStatus("inviting"));
//							m.setStatus(Status.INVITING);
//							if (mScreenMember.size() < 6){
//								mScreenMember.add(m);
//								mOtherMember.remove(indexOther);
//							}
//							needUpdate = true;
//						}
					}

					int unJoined = isMemberInList(mUnjoinedMember, id);
					if (unJoined != -1){
						mUnjoinedMember.get(unJoined).setStatus(status);
						needUpdate = true;
					}

				} else if (cmd.getName().equals(Cmd.CAMERA_CHANGE)) {
					for (int i = 0; i < mScreenMember.size(); i++) {
						if (mScreenMember.get(i).isLocal()) {
							mScreenMember.get(i).setUvc((Boolean) args[0]);
							needUpdate = true;
							break;
						}
					}

					for (int i = 0; i < mOtherMember.size(); i++) {
						if (mOtherMember.get(i).isLocal()) {
							mOtherMember.get(i).setUvc((Boolean) args[0]);
							needUpdate = true;
							break;
						}
					}

				}
			} catch (Throwable e) {
				Log.e(TAG, "OperateCommand Exception=" + e.toString());
			}

			mCommand.remove(0);
		}

		return needUpdate;
	}

	private void callBack() {
		if (mMeetingInfoUpdateListener != null && mScreenMember != null && mOtherMember != null && mUnjoinedMember != null) {
			Log.i(TAG, "Callback ============== " + ",duration = " + mDuration + ", meetingName=" + mMeetingName);

			if (!isExChange && !isPopDown && !isPopUp && !isPopUpDown) {
//				isExChange = false;
//				isPopDown = false;
//				isPopUp = false;
//				isPopUpDown = false;
				mScreenMember.addAll(mOtherMember);

				for (int i = 0; i < mScreenMember.size(); i++){
					MemberInfo item = mScreenMember.get(i);
					if (!item.isContent() && item.getNet_status() == Net_Status.NORMAL) {
						mScreenMember.remove(i);
						mScreenMember.add(0, item);
						break;
					}
				}

				for (int i = 0; i < mScreenMember.size(); i++){
					MemberInfo item = mScreenMember.get(i);
					if (!item.isContent() && item.getNet_status() == Net_Status.NORMAL
							&& !item.isLocal()) {
						mScreenMember.remove(i);
						mScreenMember.add(0, item);
						break;
					}
				}
				for (int i = 0; i < mScreenMember.size(); i++){
					MemberInfo item = mScreenMember.get(i);
					if (item.isContent()) {
						mScreenMember.remove(i);
						mScreenMember.add(0, item);
						break;
					}
				}

				mOtherMember.clear();
				while (mScreenMember.size() > SCREEN_NUMBER) {
					MemberInfo item = mScreenMember.get(SCREEN_NUMBER);
					mScreenMember.remove(SCREEN_NUMBER);
					mOtherMember.add(item);
				}
			} else {
				isExChange = false;
				isPopDown = false;
				isPopUp = false;
				isPopUpDown = false;
			}

			//check isContent
			isMemberContent = false;

			for (int i = 0; i < mScreenMember.size(); i++) {
				MemberInfo item = mScreenMember.get(i);
				if (item.isContent()){
					isMemberContent = true;
				}
				if (item.isLocal()){
					item.setUvc(isUvc);
				}
				Log.i(TAG, "mScreen = " + i + " ,Id=" + item.getId() + " ,Status=" + item.getStatus() + ",sessionType=" + item.getSessionType() + ",Displayname=" + item.getDisplayName() + ",Net_Status=" + item.getNet_status() + ",dataSourceId=" + item.getDataSourceID());
				Log.i(TAG, "CallBack-Screen-Uvc = " + item.isUvc());
			}
			for (int i = 0; i < mOtherMember.size(); i++) {
				MemberInfo item = mOtherMember.get(i);
				if (item.isContent()){
					isMemberContent = true;
				}
				if (item.isLocal()){
					item.setUvc(isUvc);
				}
				Log.i(TAG, "mOther = " + i + " ,Id=" + item.getId() + " ,Status=" + item.getStatus() + ",sessionType=" + item.getSessionType() + ",NET_STATUS=" + item.getNet_status() + ",DataSourceId=" + item.getDataSourceID());
				Log.i(TAG, "CallBack-other-Uvc = " + item.isUvc());
			}
			Log.i(TAG, "----------------- ");
			for (int i = 0; i < mUnjoinedMember.size(); i++) {
				MemberInfo item = mUnjoinedMember.get(i);
				Log.i(TAG, "mUnjoinedMember = " + i + " ,Id=" + item.getId() + " ,Status=" + item.getStatus() + ",sessionType=" + item.getSessionType() + ",NET_STATUS=" + item.getNet_status() + ",DataSourceId=" + item.getDataSourceID());
			}
			Log.i(TAG, "----------------- ");

			mMeetingInfoUpdateListener.onMemberChanged(mScreenMember, mOtherMember, mUnjoinedMember, mDuration , isMemberContent, mMeetingName);
		}
	}

	public void screenExchange(MemberInfo m1, MemberInfo m2) {
		Log.i(TAG, "***screenExchange*** id1=" + m1.getId() +",id2=" + m2.getId() + ",type1=" + m1.getSessionType() + ",type2=" + m2.getSessionType());
		Command cmd = new Command();
		cmd.setName(Cmd.SCREEN_EXCHANGE);
		Object[] args = new Object[4];
		args[0] = m1.getId();
		args[1] = m2.getId();
		args[2] = m1.getSessionType();
		args[3] = m2.getSessionType();

		cmd.setArgs(args);

		mCommand.add(cmd);

	}

	public void stateChange(String[] users, Status status) {
		String userId;
		if (users != null && users.length > 0) {
			Log.i(TAG, "***stateChange*** users=" + users.length);
			for (int i = 0; i < users.length; i++) {
				userId = users[i];
				Command cmd = new Command();
				cmd.setName(Cmd.STATE_CHANGE);
				Object[] args = new Object[2];
				args[0] = userId;
				args[1] = status;
				cmd.setArgs(args);

				Log.i(TAG, "i=" + i + ",user=" + userId + ",status=" + status);
				mCommand.add(cmd);
			}
		}
	}

	public void modeChange(int screenNumber) {
		Log.i(TAG, "***modeChange*** screenNumber=" + screenNumber);
		Command cmd = new Command();
		cmd.setName(Cmd.MODE_CHANGE);
		Object[] args = new Object[1];
		args[0] = screenNumber;
		cmd.setArgs(args);

		mCommand.add(cmd);
	}

	public void popDown(MemberInfo m) {
		Log.i(TAG, "***popDown*** id=" + m.getId() + ",type=" + m.getSessionType());
		Command cmd = new Command();
		cmd.setName(Cmd.POP_DOWN);
		Object[] args = new Object[2];
		args[0] = m.getId();
		args[1] = m.getSessionType();
		cmd.setArgs(args);

		mCommand.add(cmd);
	}

	public void popUp(MemberInfo m) {
		Log.i(TAG, "***popUp*** id=" + m.getId() + ",type=" + m.getSessionType());
		Command cmd = new Command();
		cmd.setName(Cmd.POP_UP);
		Object[] args = new Object[2];
		args[0] = m.getId();
		args[1] = m.getSessionType();
		cmd.setArgs(args);

		mCommand.add(cmd);
	}

	public void popUpDown(MemberInfo m){
		Log.i(TAG, "***popUpDown*** id=" + m.getId() + ",type=" + m.getSessionType());
		Command cmd = new Command();
		cmd.setName(Cmd.Pop_UpDown);

		String id = m.getId();
		Object[] args = new Object[2];
		args[0] = id;
		args[1] = m.getSessionType();
		cmd.setArgs(args);

		mCommand.add(cmd);
	}

	public void audioMute(boolean mute) {
		Log.i(TAG, "***audioMute*** mute=" + mute);
		Command cmd = new Command();
		cmd.setName(Cmd.AUDIO_MUTE);
		Object[] args = new Object[1];
		args[0] = mute;
		cmd.setArgs(args);

		mCommand.add(cmd);
	}

	public void videoMute(boolean mute) {
		Log.i(TAG, "***videoMute*** mute=" + mute);
		Command cmd = new Command();
		cmd.setName(Cmd.VIDEO_MUTE);
		Object[] args = new Object[1];
		args[0] = mute;
		cmd.setArgs(args);

		mCommand.add(cmd);
	}

	public void audioMode(boolean audioMode){
		Log.i(TAG, "***audioMode*** audioMode=" + audioMode);
		Command cmd = new Command();
		cmd.setName(Cmd.AUDIO_MODE);
		Object[] args = new Object[1];
		args[0] = audioMode;
		cmd.setArgs(args);

		mCommand.add(cmd);
	}

	//isUvc为true：USB摄像头
	public void cameraChange(boolean isUvc) {
		Log.i(TAG, "***cameraChange*** isUvc=" + isUvc);
		this.isUvc = isUvc;
		Command cmd = new Command();
		cmd.setName(Cmd.CAMERA_CHANGE);
		Object[] args = new Object[1];
		args[0] = isUvc;
		cmd.setArgs(args);

		mCommand.add(cmd);
	}

	private void getModeList() {
		ArrayList<MemberInfo> totalMember = new ArrayList<>();
		totalMember.addAll(mScreenMember);
		totalMember.addAll(mOtherMember);

		int mTotalSize = totalMember.size();
		mScreenMember.clear();
		mOtherMember.clear();

		if (mTotalSize <= SCREEN_NUMBER) {
			for (int i = 0; i < mTotalSize; i++) {
				mScreenMember.add(totalMember.get(i));
			}
		} else {
			for (int i = 0; i < SCREEN_NUMBER; i++) {
				mScreenMember.add(totalMember.get(i));
			}
			for (int j = SCREEN_NUMBER; j < mTotalSize; j++) {
				mOtherMember.add(totalMember.get(j));
			}
		}
	}

	private final String JOINED = "joined";
//	private final String BUSY = "busy";
//	private final String INVITING = "inviting";
//	private final String TIMEOUT = "invitetimeout";
//	private final String WAITING = "waiting";
//	private Status changeToStatus(String status) {
//		if (status.equals(BUSY)) {
//			return Status.BUSY;
//		} else if (status.equals(INVITING)) {
//			return Status.INVITING;
//		} else if (status.equals(TIMEOUT)) {
//			return Status.TIMEOUT;
//		} else if (status.equals(WAITING)) {
//			return Status.WAITING;
//		} else if (status.equals(JOINED)) {
//			return Status.JOINED;
//		}
//		return null;
//	}

	private final String ALL = "all";
	private final String CONTENTONLY = "contentonly";
	private final String MOBILE = "mobile";
	private final String PC = "pc";
	private final String UNKNOW = "unknow";
	private final String PC_CONTENT = "pc_content";
	private SessionType changeToSessionType(String st) {
		if (st.equals(ALL)) {
			return SessionType.ALL;
		} else if (st.equals(CONTENTONLY)) {
			return SessionType.CONTENTONLY;
		} else if (st.equals(MOBILE)) {
			return SessionType.MOBILE;
		} else if (st.equals(PC)) {
			return SessionType.PC;
		} else if (st.equals(UNKNOW)) {
			return SessionType.UNKNOW;
		} else if (st.equals(PC_CONTENT)) {
			return SessionType.PC_CONTENT;
		}
		return null;
	}

	private int isMemberInList(List<MemberInfo> list, MemberInfo m) {
		int index = -1;
		if (m.getSessionType() == SessionType.ALL){
			index = isMemberInList(list, m.getId());
		} else {
			index = isMemberInList(list, m.getId(), m.getSessionType());
		}
		return index;
	}

	private int isMemberInList(List<MemberInfo> list, String id, SessionType sessionType) {

		for (int i = 0; i < list.size(); i++) {
			MemberInfo item = list.get(i);

			if (item.getId().equals(id)) {
				if (item.getSessionType() == sessionType){
					return i;
				}
			}
		}
		return -1;
	}

	private int isMemberInList(List<MemberInfo> list, String id) {

		for (int i = 0; i < list.size(); i++) {
			MemberInfo item = list.get(i);

			if (item.getId().equals(id)) {
				return i;
			}
		}
		return -1;
	}

	private String getDisplayName(String username) {
		String displayName = "";
		for(User user : mServerUsers){
			if(user.getUserName().equalsIgnoreCase(username)){
				displayName = user.getDisplayName();
				break;
			}
		}
		return displayName;
	}

	private boolean updateInfo(MemberInfo info, MemberInfo newInfo) {

		boolean needUpdate = false;

		try {

			if (info.isContent() != newInfo.isContent()) {
				info.setContent(newInfo.isContent());
				needUpdate = true;
			}

			if (info.getParticipantId() != newInfo.getParticipantId()) {
				info.setParticipantId(newInfo.getParticipantId());
				needUpdate = true;
			}

			if (info.isAudioMute() != newInfo.isAudioMute() && !info.isLocal()) {
				info.setAudioMute(newInfo.isAudioMute());
				needUpdate = true;
			}

			if (!info.getStatus().equals(newInfo.getStatus())) {
				info.setStatus(newInfo.getStatus());
				needUpdate = true;
			}

			if (info.getSessionType() != newInfo.getSessionType()) {
				info.setSessionType(newInfo.getSessionType());
				needUpdate = true;
			}

			if (info.isVideoMute() != newInfo.isVideoMute() && !info.isLocal()) {
				info.setVideoMute(newInfo.isVideoMute());
				needUpdate = true;
			}

			if (info.isLocal() != newInfo.isLocal()) {
				info.setLocal(newInfo.isLocal());
				needUpdate = true;
			}

			if (info.isUvc() != newInfo.isUvc() && !info.isLocal()) {
				info.setUvc(newInfo.isUvc());
				needUpdate = true;
			}

			if (info.getNet_status() != newInfo.getNet_status() && !(info.isLocal() && info.getNet_status() == Net_Status.VIDEO_MUTE)) {
				info.setNet_status(newInfo.getNet_status());
				needUpdate = true;
			}

			if (newInfo.getDataSourceID() != null) {
				if (!newInfo.getDataSourceID().equals(info.getDataSourceID())) {
					info.setDataSourceID(newInfo.getDataSourceID());
					needUpdate = true;
				}
			} else {
				if (info.getDataSourceID() != null) {
					info.setDataSourceID(newInfo.getDataSourceID());
					needUpdate = true;
				}
			}

		} catch (Throwable e) {
			Log.e(TAG, "UpdateInfo Throwable:" + e.toString());
		}

		return needUpdate;
	}

	public void setMeetingInfoUpdateListener(VideoUpdateListener listener) {
		mMeetingInfoUpdateListener = listener;
	}

	private VideoUpdateListener mMeetingInfoUpdateListener = null;

}