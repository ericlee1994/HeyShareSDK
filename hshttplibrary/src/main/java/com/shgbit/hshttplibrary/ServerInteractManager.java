package com.shgbit.hshttplibrary;

import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shgbit.hshttplibrary.callback.ServerAddressCallback;
import com.shgbit.hshttplibrary.callback.ServerConfigCallback;
import com.shgbit.hshttplibrary.callback.ServerInteractCallback;
import com.shgbit.hshttplibrary.callback.ServerRecordCallback;
import com.shgbit.hshttplibrary.json.AddToGroupInfo;
import com.shgbit.hshttplibrary.json.BusyMeetingInfo;
import com.shgbit.hshttplibrary.json.CancelInviteInfo;
import com.shgbit.hshttplibrary.json.CheckPwdResponse;
import com.shgbit.hshttplibrary.json.Contacts;
import com.shgbit.hshttplibrary.json.CreateGroupInfo;
import com.shgbit.hshttplibrary.json.CreateInfo;
import com.shgbit.hshttplibrary.json.CreateMeetingInfo;
import com.shgbit.hshttplibrary.json.DeleFrmGroupInfo;
import com.shgbit.hshttplibrary.json.DeleteGroupInfo;
import com.shgbit.hshttplibrary.json.DeleteInfo;
import com.shgbit.hshttplibrary.json.EndMeetingInfo;
import com.shgbit.hshttplibrary.json.EndYunDeskInfo;
import com.shgbit.hshttplibrary.json.EndYunDeskResponse;
import com.shgbit.hshttplibrary.json.FrequentContactsInfoSet;
import com.shgbit.hshttplibrary.json.FrequentContactsPost;
import com.shgbit.hshttplibrary.json.Group;
import com.shgbit.hshttplibrary.json.HotFixConfig;
import com.shgbit.hshttplibrary.json.InviteCancledInfo;
import com.shgbit.hshttplibrary.json.InviteMeetingInfo;
import com.shgbit.hshttplibrary.json.InvitedMeeting;
import com.shgbit.hshttplibrary.json.JoinMeetingInfo;
import com.shgbit.hshttplibrary.json.KickoutInfo;
import com.shgbit.hshttplibrary.json.LoginInfo;
import com.shgbit.hshttplibrary.json.LoginResponse;
import com.shgbit.hshttplibrary.json.LogoutInfo;
import com.shgbit.hshttplibrary.json.MainImage;
import com.shgbit.hshttplibrary.json.MeetingDetail;
import com.shgbit.hshttplibrary.json.MeetingInfo;
import com.shgbit.hshttplibrary.json.MeetingRecord;
import com.shgbit.hshttplibrary.json.Online;
import com.shgbit.hshttplibrary.json.OnlineUser;
import com.shgbit.hshttplibrary.json.Organization;
import com.shgbit.hshttplibrary.json.PushConfig;
import com.shgbit.hshttplibrary.json.QueryGroupInfo;
import com.shgbit.hshttplibrary.json.QueryGroupResponse;
import com.shgbit.hshttplibrary.json.QuiteMeetingInfo;
import com.shgbit.hshttplibrary.json.RefuseInfo;
import com.shgbit.hshttplibrary.json.ReserveInfo;
import com.shgbit.hshttplibrary.json.ReserveMeetingInfo;
import com.shgbit.hshttplibrary.json.ReserveRepsonse;
import com.shgbit.hshttplibrary.json.Result;
import com.shgbit.hshttplibrary.json.SendInfo;
import com.shgbit.hshttplibrary.json.StartYunDeskInfo;
import com.shgbit.hshttplibrary.json.StartYunDeskResponse;
import com.shgbit.hshttplibrary.json.SyncPidInfo;
import com.shgbit.hshttplibrary.json.SystemConfig;
import com.shgbit.hshttplibrary.json.TimeoutInfo;
import com.shgbit.hshttplibrary.json.UpdateGroupInfo;
import com.shgbit.hshttplibrary.json.UserInfo;
import com.shgbit.hshttplibrary.json.ValidateInfo;
import com.shgbit.hshttplibrary.json.XiaoYuConfig;
import com.shgbit.hshttplibrary.tool.GBUE1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerInteractManager {
	private final String TAG = "ServerInteractManager";
	private final String SecretKey = "c3230d18-599c-486f-94e4-615921ca5e46";
	private final String SessionType = "MobileState";
	
	public enum INTERACTTYPE {LOGIN,LOGINOUT,CONTACTS,ONLINE,GFCUSER,CREATE,JOIN,INVITE,KICKOUT,QUITE,END,STARTYUN,ENDYUN,RESERVE
		,DELETE,UPDATE,BUSY,CONFIG,MEETING,CHECKPWD,MOTIFYPWD,PFCUSER,CANCLEINVITE,SYNCPID,SENDMSG,CREATEGROUP,DELETEGROUP,UPDATEGROUP
		,QUERYGROUP,ADDTOGROUP,DELEFRMGROUP,START,STOP,VALIDATE,MAINIMAGE}

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private OkHttpClient mOkHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
	
	private HeartBeatThread mHeartBeatThread;
	private boolean mHeartBeatThreadLooper = false;
	private boolean mIsFinishHeartbeat = false;

	private long timeStamp = 0;

	private String mServiceUrl = "";
	private String mSessionId = "";
	private String mUserName = "";

	private String addrString;
	private OnlineUser[] onlineUsers;

	private InvitedMeeting mInvitedMeeting;

	public static ServerInteractManager instance;

	private ServerConfigCallback mConfigCallback;
	public void setServerConfigCallback (ServerConfigCallback callback) {
		mConfigCallback = callback;
	}

	private ServerAddressCallback mAddressCallback;
	public void setServerAddressCallback (ServerAddressCallback callback) {
		mAddressCallback  = callback;
	}

	private ServerInteractCallback mInteractCallback;
	public void setServerInteractCallback (ServerInteractCallback callback) {
		mInteractCallback = callback;
	}

	public ServerInteractManager() {

	}

	public static ServerInteractManager getInstance () {
		if (instance == null) {
			instance = new ServerInteractManager();
		}
		return instance;
	}

	public void init (String serviceurl) {
		mServiceUrl = serviceurl;
		getSystemConfig();
	}

	public void finalize () {
		try {
			if (mHeartBeatThread != null) {
				mHeartBeatThreadLooper = false;
				mIsFinishHeartbeat = true;
				mHeartBeatThread.join(100);
				mHeartBeatThread.interrupt();
				mHeartBeatThread = null;
			}

			onlineUsers = null;
			mInvitedMeeting = null;

			mConfigCallback = null;
			mInteractCallback = null;
			mAddressCallback = null;

			instance = null;
		} catch (Throwable e) {
			Log.e(TAG, "finalize Throwable: " + e.toString());
		}
	}

	private void startHeartBeat () {
		try {
			if (mHeartBeatThread != null) {
				mHeartBeatThreadLooper = false;
				mIsFinishHeartbeat = true;
				mHeartBeatThread.join(100);
				mHeartBeatThread.interrupt();
				mHeartBeatThread = null;
			}
		} catch (Throwable e) {
			Log.e(TAG, "finalize heartbeat Throwable:" + e.toString());
		}
			
		mHeartBeatThread = new HeartBeatThread();
		mHeartBeatThreadLooper = true;
		mIsFinishHeartbeat = false;
		mHeartBeatThread.start();
	}
	
	private void getSystemConfig () {
		String url = mServiceUrl + "/settings/v1";
		new GetTask(url, INTERACTTYPE.CONFIG).execute();
	}
	
	private void getContacts () {
		String url = mServiceUrl + "/contacts?sessionId=" + mSessionId;
		new GetTask(url, INTERACTTYPE.CONTACTS).execute();
	}
	
	private void getOnline () {
		String url = mServiceUrl + "/user/online";
		new GetTask(url, INTERACTTYPE.ONLINE).execute();
	}

	public void login (LoginInfo li) {
		if (li == null) {
			return;
		}
		mUserName = li.getUserName();

		String url = mServiceUrl + "/login";
		String data = getsecritString(li, SecretKey);

		JsonObject object = new JsonObject();
		try {
			object.addProperty("data", data);
			object.addProperty("sessionType", SessionType);
		} catch (Throwable e) {
			Log.e(TAG, "Add JSONObject Throwable: " + e.toString());
		}
		
		new PostTask(url, object.toString(), INTERACTTYPE.LOGIN).execute();
	}
	
	public void checkPwd(LoginInfo li){
		if (li == null) {
			return;
		}

		String url = mServiceUrl + "/user/password/validation";
		String data = getsecritString(li, SecretKey);

		JsonObject object = new JsonObject();
		try {
			object.addProperty("data", data);
			object.addProperty("sessionId", mSessionId);
		} catch (Throwable e) {
			Log.e(TAG, "Add JSONObject Throwable: " + e.toString());
		}
		
		new PostTask(url, object.toString(), INTERACTTYPE.CHECKPWD).execute();
	}
	
	public void motifyPwd(LoginInfo li){
		if (li == null) {
			return;
		}

		String url = mServiceUrl + "/user/password/update";
		String data = getsecritString(li, SecretKey);

		JsonObject object = new JsonObject();
		try {
			object.addProperty("data", data);
			object.addProperty("sessionId", mSessionId);
		} catch (Throwable e) {
			Log.e(TAG, "Add JSONObject Throwable: " + e.toString());
		}
		
		new PostTask(url, object.toString(), INTERACTTYPE.MOTIFYPWD).execute();
	}
	
	private String getsecritString (LoginInfo li, String secret) {
		JsonObject jObject = new JsonObject();
		try {
			jObject.addProperty("userName", li.getUserName());
			jObject.addProperty("password", li.getPassword());
		} catch (Throwable e) {
			Log.e(TAG, "Add JSONObject Throwable: " + e.toString());
		}
		return GBUE1.encode(jObject.toString(), secret);
	}
	
	public void logout () {
		LogoutInfo logoutInfo = new LogoutInfo();
		logoutInfo.setSessionId(mSessionId);
		logoutInfo.setSessionType(SessionType);
		
		String url = mServiceUrl + "/logout";
		
		new PostTask(url, new Gson().toJson(logoutInfo), INTERACTTYPE.LOGINOUT).execute();
	}
	
	public void createMeeting (CreateMeetingInfo cmi) {
		if (cmi != null) {
			cmi.setSessionType(SessionType);
			cmi.setSessionId(mSessionId);
		}
		
		String url = mServiceUrl + "/meeting/creating";
		
		new PostTask(url, new Gson().toJson(cmi), INTERACTTYPE.CREATE).execute();
	}
	
	public void joinMeeting (JoinMeetingInfo jmi) {
		if (jmi != null) {
			jmi.setSessionType(SessionType);
			jmi.setSessionId(mSessionId);
		}
		
		String url = mServiceUrl + "/meeting/joining";
		
		new PostTask(url, new Gson().toJson(jmi), INTERACTTYPE.JOIN).execute();
	}

	public void getContactUser () {
		String url = mServiceUrl + "/user/contact/favorites?sessionId="+ mSessionId;
		new GetTask(url, INTERACTTYPE.GFCUSER).execute();
	}
	
	public void FrequentContacts (FrequentContactsPost fmi) {
		if (fmi != null) {
			fmi.setSessionId(mSessionId);
		}
		String url = mServiceUrl + "/user/contact/favorites";
		
		new PostTask(url, new Gson().toJson(fmi), INTERACTTYPE.PFCUSER).execute();
	}
	
	public void inviteMeeting (InviteMeetingInfo imi) {
		if (imi != null) {
			imi.setSessionId(mSessionId);
			imi.setSessionType(SessionType);
		}
		
		String url = mServiceUrl + "/meeting/inviting";
		
		new PostTask(url, new Gson().toJson(imi), INTERACTTYPE.INVITE).execute();
	}
	
	public void kickoutMeeting (KickoutInfo ki) {
		if (ki != null) {
			ki.setSessionId(mSessionId);
		}
		
		String url = mServiceUrl + "/meeting/kickout";
		
		new PostTask(url, new Gson().toJson(ki), INTERACTTYPE.KICKOUT).execute();
	}
	
	public void quiteMeeting (QuiteMeetingInfo qmi) {
		if (qmi != null) {
			qmi.setSessionType(SessionType);
			qmi.setSessionId(mSessionId);
		}
		
		String url = mServiceUrl + "/meeting/quiting";
		
		new PostTask(url, new Gson().toJson(qmi), INTERACTTYPE.QUITE).execute();
	}
	
	public void endMeeting (EndMeetingInfo emi) {
		if (emi != null) {
			emi.setSessionType(SessionType);
			emi.setSessionId(mSessionId);
		}
		
		String url = mServiceUrl + "/meeting/end";
		
		new PostTask(url, new Gson().toJson(emi), INTERACTTYPE.END).execute();
	}
	
	public void startYunDesktop (StartYunDeskInfo syi) {
		if (syi != null) {
			syi.setSessionId(mSessionId);
		}
		
		String url = mServiceUrl + "/meeting/yunDesktop/starting";
		
		new PostTask(url, new Gson().toJson(syi), INTERACTTYPE.STARTYUN).execute();
	}
	
	public void endYunDesktop (EndYunDeskInfo ei) {
		if (ei != null) {
			ei.setSessionId(mSessionId);
		}
		
		String url = mServiceUrl + "/meeting/yunDesktop/end";
		
		new PostTask(url, new Gson().toJson(ei), INTERACTTYPE.ENDYUN).execute();
	}
	
	public void reserveMeeting (ReserveInfo ri) {
		if (ri != null) {
			ri.setSessionType(SessionType);
			ri.setSessionId(mSessionId);
		}
		
		String url = mServiceUrl + "/meeting/reserving";
		
		new PostTask(url, new Gson().toJson(ri), INTERACTTYPE.RESERVE).execute();
	}
	
	public void deleteMeeting (DeleteInfo di) {
		if (di != null) {
			di.setSessionType(SessionType);
			di.setSessionId(mSessionId);
		}
		
		String url = mServiceUrl + "/meeting/delete";
		
		new PostTask(url, new Gson().toJson(di), INTERACTTYPE.DELETE).execute();
	}
	
	public void updateMeeting (ReserveMeetingInfo rmi) {
		if (rmi != null) {
			rmi.setSessionType(SessionType);
			rmi.setSessionId(mSessionId);
		}
		
		String url = mServiceUrl + "/meeting/update";
		
		new PostTask(url, new Gson().toJson(rmi), INTERACTTYPE.UPDATE).execute();
	}
	
	public void busyMeeting (BusyMeetingInfo bmi) {
		if (bmi != null) {
			bmi.setSessionType(SessionType);
			bmi.setSessionId(mSessionId);
		}
		
		String url = mServiceUrl + "/meeting/busy";
		
		new PostTask(url, new Gson().toJson(bmi), INTERACTTYPE.BUSY).execute();
	}
	
	public void getMeeting (String meetingId) {
		String url = mServiceUrl + "/meeting/" + meetingId;
		new GetTask(url, INTERACTTYPE.MEETING).execute();
	}
	public String getSyncCurrentMeeting () {
		String url = mServiceUrl + "/currentMeeting?userName=" + mUserName + "&sessionId=" +
				mSessionId + "&sessionType=" + SessionType;

		return httpGet(url);
	}

	public String getSyncGetMeeting(String meetingId){
		String url = mServiceUrl + "/meeting/" + meetingId;
		return  httpGet(url);
	}
	
	public void cancleMeeting (CancelInviteInfo cii) {
		if (cii != null) {
			cii.setSessionType(SessionType);
			cii.setSessionId(mSessionId);
		}
		
		String url = mServiceUrl + "/meeting/inviting/cancel";
		
		new PostTask(url, new Gson().toJson(cii), INTERACTTYPE.CANCLEINVITE).execute();
	}
	
	public void syncPid (SyncPidInfo spi) {
		if (spi != null) {
			spi.setSessionId(mSessionId);
			spi.setSessionType(SessionType);
		}

		String url = mServiceUrl + "/meeting/syncpid";
		
		new PostTask(url, new Gson().toJson(spi), INTERACTTYPE.SYNCPID).execute();
	}

	public void sendMessage (SendInfo si) {
		if (si != null) {
			si.setSessionId(mSessionId);
		}

		String url = mServiceUrl + "/meeting/sms/send";

		new PostTask(url, new Gson().toJson(si), INTERACTTYPE.SENDMSG).execute();
	}

	public void createGroup (CreateGroupInfo cgi) {
		if (cgi != null) {
			cgi.setSessionId(mSessionId);
		}

		String url = mServiceUrl + "/group/create";

		new PostTask(url, new Gson().toJson(cgi), INTERACTTYPE.CREATEGROUP).execute();
	}

	public void deleteGroup (DeleteGroupInfo dgi) {
		if (dgi != null) {
			dgi.setSessionId(mSessionId);
		}

		String url = mServiceUrl + "/group/delete";

		new PostTask(url, new Gson().toJson(dgi), INTERACTTYPE.DELETEGROUP).execute();
	}

	public void updateGroup (UpdateGroupInfo ugi) {
		if (ugi != null) {
			ugi.setSessionId(mSessionId);
		}

		String url = mServiceUrl + "/group/update";

		new PostTask(url, new Gson().toJson(ugi), INTERACTTYPE.UPDATEGROUP).execute();
	}

	public void queryGroup (QueryGroupInfo qgi) {
		if (qgi != null) {
			qgi.setSessionId(mSessionId);
		}

		String url = mServiceUrl + "/group/query";

		new PostTask(url, new Gson().toJson(qgi), INTERACTTYPE.QUERYGROUP).execute();
	}

	public void addToGroup (AddToGroupInfo atgi) {
		if (atgi != null) {
			atgi.setSessionId(mSessionId);
		}

		String url = mServiceUrl + "/group/members/add";

		new PostTask(url, new Gson().toJson(atgi), INTERACTTYPE.ADDTOGROUP).execute();
	}

	public void deleFrmGroup (DeleFrmGroupInfo dfgi) {
		if (dfgi != null) {
			dfgi.setSessionId(mSessionId);
		}

		String url = mServiceUrl + "/group/members/delete";

		new PostTask(url, new Gson().toJson(dfgi), INTERACTTYPE.DELEFRMGROUP).execute();
	}

	public void startRecord (MeetingRecord mr) {
		if (mr != null) {
			mr.setSessionId(mSessionId);
		}

		String url = mServiceUrl + "/meeting/record/start";
		new PostTask(url, new Gson().toJson(mr), INTERACTTYPE.START).execute();
	}

	public void endRecord (MeetingRecord mr) {
		if (mr != null) {
			mr.setSessionId(mSessionId);
		}

		String url = mServiceUrl + "/meeting/record/end";
		new PostTask(url, new Gson().toJson(mr), INTERACTTYPE.STOP).execute();
	}

	public void validate (ValidateInfo vi) {
		String url = "http://172.17.16.211:3000/pkg/permission";
		new PostTask(url, new Gson().toJson(vi), INTERACTTYPE.VALIDATE).execute();
	}

	public  void setMainImage (MainImage mi) {
		if (mi != null) {
			mi.setSessionId(mSessionId);
		}
		String url = mServiceUrl + "/meeting/mainImage";
		new PostTask(url, new Gson().toJson(mi), INTERACTTYPE.MAINIMAGE).execute();
	}

	private String getTimeStr2(long time) {
		SimpleDateFormat sTimeSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
		return sTimeSDF.format(time);
	}

	public String getAddrString(){
		return addrString;
	}

	private class HeartBeatThread extends Thread {

		@Override
		public void run() {
			try {
				long start = 0;
				long end = 0;
				while (mHeartBeatThreadLooper) {
					Log.i(TAG, "HeartBeatThread is running !!!");
					start = System.currentTimeMillis();
					
					 Time time = new Time();
					 time.setToNow();
					 String starttime = getTimeStr2(time.toMillis(false));
					 time.year +=1;
					 time.hour = 23;
					 time.minute = 59;
					 String endTime = getTimeStr2(time.toMillis(false));

					 String url = mServiceUrl + "/meeting?userName=" + mUserName + "&sessionId=" + mSessionId
							 + "&startTime=" + starttime + "&endTime=" + endTime + "&sessionType=" + SessionType;
					 
					 try {
						 String response = httpGet(url);
						 if (response != null && response.equals("") == false) {
							 MeetingInfo mi = new Gson().fromJson(response, MeetingInfo.class);
							 if (mi != null && mi.getResult().equalsIgnoreCase("success") == true) {
								 boolean update = MeetingCeche.getInstance().setMeetings(mi.getMeetings());
								 if (update && mInteractCallback != null) {
									 mInteractCallback.onMeetings();
								 }
							 }
						 }
					 } catch (Throwable e) {
						 Log.e(TAG, "Get meetings Throwable: " + e.toString());
					 }
					 
					 String url2 = mServiceUrl + "/heartbeat?userName=" + mUserName + "&sessionId=" + mSessionId
							 + (timeStamp == 0 ? "":"&timeStamp=" + timeStamp);
					 
					 try {
						 String response = httpGet(url2);
						 JSONObject jsonObject = new JSONObject(response);
						 if (jsonObject.has("heartbeatEvents")) {
							 JSONArray jsonArray = jsonObject.getJSONArray("heartbeatEvents");
							 if (jsonArray != null) {
								 ArrayList<RefuseInfo> refuselist = new ArrayList<>();
								 ArrayList<TimeoutInfo> timeoutlist = new ArrayList<>();
								 ArrayList<InvitedMeeting> invitelist = new ArrayList<>();
							 	for (int i = 0; i < jsonArray.length(); i++) {
									if (jsonArray.getJSONObject(i).has("eventName") == false) {
										continue;
									}
									String eventName = jsonArray.getJSONObject(i).getString("eventName");

									if (eventName.equalsIgnoreCase("meetingJoined")) {

									} else if (eventName.equalsIgnoreCase("meetingBusied")) {
										refuselist.add(new Gson().fromJson(jsonArray.getJSONObject(i).getString("eventParams"),RefuseInfo.class));
									} else if (eventName.equalsIgnoreCase("meetingInvitingTimeout")) {
										timeoutlist.add(new Gson().fromJson(jsonArray.getJSONObject(i).getString("eventParams"), TimeoutInfo.class));
									} else if (eventName.equalsIgnoreCase("meetingInvited")) {
										invitelist.add(new Gson().fromJson(jsonArray.getJSONObject(i).getString("eventParams"), InvitedMeeting.class));
									} else if (eventName.equalsIgnoreCase("byKicking")) {

									} else if (eventName.equalsIgnoreCase("meetingKickouted")) {

									} else if (eventName.equalsIgnoreCase("meetingQuited")) {

									} else if (eventName.equalsIgnoreCase("meetingEnded")) {

									} else if (eventName.equalsIgnoreCase("yunStarted")) {

									} else if (eventName.equalsIgnoreCase("yunEnded")) {

									} else if (eventName.equalsIgnoreCase("whiteStarted")) {

									} else if (eventName.equalsIgnoreCase("whiteEnded")) {

									} else if (eventName.equalsIgnoreCase("fileResultNoted")) {

									} else if (eventName.equalsIgnoreCase("onlineChange")) {

									} else if (eventName.equalsIgnoreCase("differentPlaceLogin")) {
										if (mInteractCallback != null) {
											mInteractCallback.eventDifferentPlaceLogin();
										}
									} else if (eventName.equalsIgnoreCase("meetingInvitingTimeout")) {

									} else if (eventName.equalsIgnoreCase("meetingChanged")) {

									} else if (eventName.equalsIgnoreCase("invitingCancel")) {
										if (mInteractCallback != null) {
											mInteractCallback.eventInvitingCancle(new Gson().fromJson(jsonArray.getJSONObject(i).getString("eventParams"), InviteCancledInfo.class));
										}
									}

									if (jsonArray.getJSONObject(i).has("timeStamp")) {
										timeStamp = jsonArray.getJSONObject(i).getLong("timeStamp");
									}
								}

								 if (refuselist.size() > 0 || timeoutlist.size() > 0) {
							 		if (mInteractCallback != null) {
							 			mInteractCallback.eventUserStateChanged(refuselist.toArray(new RefuseInfo[0]), timeoutlist.toArray(new TimeoutInfo[0]));
									}
								 }

								 if (invitelist.size() > 0) {
							 		mInvitedMeeting = invitelist.get(invitelist.size()-1);
									 if (mInvitedMeeting != null) {
										 getMeeting(mInvitedMeeting.getMeetingId());
									 }
								 }
							 }
						 }
					 } catch (Throwable e) {
						 Log.e(TAG, "HeartBeatThread Throwable1:" + e.toString());
					 }
					 
					 getOnline();

					end = System.currentTimeMillis();
					 
					mIsFinishHeartbeat = false;
					int count = 10 - (int)((end-start)/1000);
					for (int i = 0; i < count; i++) {
						if (mIsFinishHeartbeat) {
							mIsFinishHeartbeat = false;
							break;
						}
						Thread.sleep(1000);
					}
				}
			} catch (Throwable e) {
				Log.e(TAG, "HeartBeatThread Throwable2:" + e.toString());
			}
			super.run();
		}
	}
	
	private class PostTask extends AsyncTask<Void, Void, String> {
		private String mUrl = "";
		private String mObject;
		private INTERACTTYPE mType;
		public PostTask (String url, String object, INTERACTTYPE type) {
			mUrl = url;
			mObject = object;
			mType = type;
		}

		@Override
		protected String doInBackground(Void... arg0) {
			try {
				return httpPost(mUrl,mObject);
			} catch (Throwable e) {
				Log.e(TAG, "doInBackground Throwable: " + e.toString());
				return "";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				switch (mType) {
				case LOGIN:
					LoginResponse lr = null;

					try {
						lr = new Gson().fromJson(result, LoginResponse.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse LoginResponse Throwable: " + e.toString());
					}

					if (lr == null || lr.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onLogin(false, lr == null?"unknow error":lr.getFailedMessage(), null);
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onLogin(true, "", lr.getUser());
						}

						mSessionId = lr.getSessionId();
						Log.i(TAG, "login success:mSessionId=" + mSessionId);
						getContacts();
						getContactUser();
						queryGroup(new QueryGroupInfo());
						startHeartBeat();
					}
					break;
				case CHECKPWD:
					CheckPwdResponse cpwd = null;

					try {
						cpwd = new Gson().fromJson(result, CheckPwdResponse.class);

					} catch (Throwable e) {
						Log.e(TAG, "parse CheckPwdResponse Throwable: " + e.toString());
					}

					if (cpwd == null || cpwd.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onCheckPwd(false, cpwd == null?"check failed":cpwd.getFailedMessage());
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onCheckPwd(true, "");
						}
					}
					break;
				case MOTIFYPWD:
					CheckPwdResponse cp = null;

					try {
						cp = new Gson().fromJson(result, CheckPwdResponse.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse MotifyPwdResponse Throwable: " + e.toString());
					}

					if (cp == null || cp.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onMotifyPwd(false, cp == null?"motify failed":cp.getFailedMessage());
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onMotifyPwd(true, "");
						}
					}
					break;
				case LOGINOUT:
					Result r = null;

					try {
						r = new Gson().fromJson(result, Result.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (r == null || r.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onLogout(false, r == null?"unknow error":r.getFailedMessage());
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onLogout(true,"");
						}
					}
					break;
				case CREATE:
					CreateInfo ci = null;

					try {
						ci = new Gson().fromJson(result, CreateInfo.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse CreateInfo Throwable: " + e.toString());
					}

					if (ci == null || ci.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onCreateMeeting(false, ci == null?"unknow error":ci.getFailedMessage(), null);
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onCreateMeeting(true, "", ci.getMeeting());
						}
					}
					break;
				case JOIN:
					Result r2 = null;

					try {
						r2 = new Gson().fromJson(result, Result.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (r2 == null || r2.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onJoinMeeting(false, r2 == null?"unknow error":r2.getFailedMessage());
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onJoinMeeting(true,"");
						}
					}
					break;
				case INVITE:
					Result r3 = null;

					try {
						r3 = new Gson().fromJson(result, Result.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (r3 == null || r3.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onInviteMeeting(false, r3 == null?"unknow error":r3.getFailedMessage());
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onInviteMeeting(true, "");
						}
					}
					break;
				case KICKOUT:
					Result r4 = null;

					try {
						r4 = new Gson().fromJson(result, Result.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (r4 == null || r4.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onKickoutMeeting(false, r4 == null?"unknow error":r4.getFailedMessage());
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onKickoutMeeting(true, "");
						}
					}
					break;
				case QUITE:
					Result r5 = null;

					try {
						r5 = new Gson().fromJson(result, Result.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (r5 == null || r5.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onQuiteMeeting(false, r5 == null?"unknow error":r5.getFailedMessage());
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onQuiteMeeting(true, "");
						}
					}
					break;
				case END:
					Result r6 = null;

					try {
						r6 = new Gson().fromJson(result, Result.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (r6 == null || r6.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onEndMeeting(false, r6 == null?"unknow error":r6.getFailedMessage());
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onEndMeeting(true, "");
						}
					}
					break;
				case STARTYUN:
					StartYunDeskResponse syr = null;

					try {
						syr = new Gson().fromJson(result, StartYunDeskResponse.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse StartYunDeskResponse Throwable: " + e.toString());
					}

					if (syr == null || syr.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onStartYunDesk(false, syr == null?"unknow error":syr.getFailedMessage(), null);
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onStartYunDesk(true, "", syr.getYunDesktop());
						}
					}
					break;
				case ENDYUN:
					EndYunDeskResponse eyr = null;

					try {
						eyr = new Gson().fromJson(result, EndYunDeskResponse.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse EndYunDeskResponse Throwable: " + e.toString());
					}

					if (eyr == null || eyr.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onEndYunDesk(false, eyr == null?"unknow error":eyr.getFailedMessage(), null);
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onEndYunDesk(true, "", eyr.getYunDesktop());
						}
					}
					break;
				case RESERVE:
					ReserveRepsonse rr = null;

					try {
						rr = new Gson().fromJson(result, ReserveRepsonse.class);
					}catch (Throwable e) {
						Log.e(TAG, "parse ReserveRepsonse Throwable: " + e.toString());
					}

					if (rr == null || rr.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onReserveMeeting(false, rr == null?"unknow error":rr.getFailedMessage(), null);
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onReserveMeeting(true, "", rr.getMeeting());
						}
					}
					break;
				case DELETE:
					Result r7 = null;

					try {
						r7 = new Gson().fromJson(result, Result.class);
					}catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (r7 == null || r7.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onDeleteMeeting(false, r7 == null?"unknow error":r7.getFailedMessage());
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onDeleteMeeting(true, "");
						}
					}
					break;
				case UPDATE:
					Result r8 = null;

					try {
						r8 = new Gson().fromJson(result, Result.class);
					}catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (r8 == null || r8.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onUpdateMeeting(false, r8 == null?"unknow error":r8.getFailedMessage());
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onUpdateMeeting(true, "");
						}
					}
					break;
				case BUSY:
					Result r9 = null;

					try {
						r9 = new Gson().fromJson(result, Result.class);
					}catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (r9 == null || r9.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onBusyMeeting(false, r9 == null?"unknow error":r9.getFailedMessage());
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onBusyMeeting(true, "");
						}
					}
					break;
				case CANCLEINVITE:
					Log.i(TAG, "cancle invite result: " + result);
                    break;
				case PFCUSER:
					Result r10 = null;

					try {
						r10 = new Gson().fromJson(result, Result.class);
					}catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (r10 == null || r10.getResult().equalsIgnoreCase("failed") == true) {
						if (mAddressCallback != null) {
							mAddressCallback.onPostContactUser(false, r10 == null?"unknow error":r10.getFailedMessage());
						}
					} else {
						if (mAddressCallback != null) {
							mAddressCallback.onPostContactUser(true, "");
							getContactUser();
						}
					}
					break;
				case SENDMSG:
					Log.i(TAG, "send message result: " + result);
					break;
				case CREATEGROUP:
					Result r11 = null;
					try {
						r11 = new Gson().fromJson(result, Result.class);
					}catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (r11 == null || r11.getResult().equalsIgnoreCase("failed") == true) {
						if (mAddressCallback != null) {
							mAddressCallback.onCreateGroup(false,r11 == null?"unknow error":r11.getFailedMessage());
						}
					} else {
						if (mAddressCallback != null) {
							mAddressCallback.onCreateGroup(true, "");
						}
						queryGroup(new QueryGroupInfo());
					}
					break;
				case DELETEGROUP:
					Result r12 = null;

					try {
						r12 = new Gson().fromJson(result, Result.class);
					}catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (r12 == null || r12.getResult().equalsIgnoreCase("failed") == true) {
						if (mAddressCallback != null) {
							mAddressCallback.onDeleteGroup(false, r12 == null?"unknow error":r12.getFailedMessage());
						}
					} else {
						if (mAddressCallback != null) {
							mAddressCallback.onDeleteGroup(true, "");
						}
						queryGroup(new QueryGroupInfo());
					}
					break;
				case UPDATEGROUP:
					Result r13 = null;

					try {
						r13 = new Gson().fromJson(result, Result.class);
					}catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (r13 == null || r13.getResult().equalsIgnoreCase("failed") == true) {
						if (mAddressCallback != null) {
							mAddressCallback.onUpdateGroup(false, r13 == null?"unknow error":r13.getFailedMessage());
						}
					} else {
						if (mAddressCallback != null) {
							mAddressCallback.onUpdateGroup(true, "");
						}
						queryGroup(new QueryGroupInfo());
					}
					break;
				case QUERYGROUP:
					QueryGroupResponse qgr = null;

					try {
						qgr = new Gson().fromJson(result, QueryGroupResponse.class);
					}catch (Throwable e) {
						Log.e(TAG, "parse QueryGroupResponse Throwable: " + e.toString());
					}

					if (qgr == null || qgr.getResult().equalsIgnoreCase("failed") == true) {
						Log.e(TAG, "query group Throwable: " + qgr == null?"unknow error":qgr.getFailedMessage());
					} else {
						AddressCeche.getInstance().setGroups(qgr.getGroups());
					}
					break;
				case ADDTOGROUP:
					Result r14 = null;

					try {
						r14 = new Gson().fromJson(result, Result.class);
					}catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}


					if (r14 == null || r14.getResult().equalsIgnoreCase("failed") == true) {
						if (mAddressCallback != null) {
							mAddressCallback.onAddToGroup(false, r14 == null?"unknow error":r14.getFailedMessage());
						}
					} else {
						if (mAddressCallback != null) {
							mAddressCallback.onAddToGroup(true, "");
						}
						queryGroup(new QueryGroupInfo());
					}
					break;
				case DELEFRMGROUP:
					Result r15 = null;

					try {
						r15 = new Gson().fromJson(result, Result.class);
					}catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (r15 == null || r15.getResult().equalsIgnoreCase("failed") == true) {
						if (mAddressCallback != null) {
							mAddressCallback.onDeleFrmGroup(false, r15 == null?"unknow error":r15.getFailedMessage());
						}
					} else {
						if (mAddressCallback != null) {
							mAddressCallback.onDeleFrmGroup(true, "");
						}
						queryGroup(new QueryGroupInfo());
					}
					break;
				case START:
					Result result1= null;
					try {
						result1 = new Gson().fromJson(result, Result.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (result1 == null || result1.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.startRecord(false, result1 == null?"unknow error":result1.getFailedMessage());
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.startRecord(true, "");
						}
					}
					break;
				case STOP:
					Result result2 = null;
					try {
						result2 = new Gson().fromJson(result, Result.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (result2 == null || result2.getResult().equalsIgnoreCase("failed") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.endRecord(false, result2 == null?"unknow error":result2.getFailedMessage());
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.endRecord(true, "");
						}
					}
					break;
				case VALIDATE:
					Result result3 = null;
					try {
						result3 = new Gson().fromJson(result, Result.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (result3 == null || result3.getResult().equalsIgnoreCase("false") == true) {
						if (mInteractCallback != null) {
							mInteractCallback.onValidate(false, result3 == null?"unknow error":result3.getFailedMessage());
						}
					} else {
						if (mInteractCallback != null) {
							mInteractCallback.onValidate(true, "");
						}
					}
					break;
				case MAINIMAGE:
					Result result4 = null;
					try {
						result4 = new Gson().fromJson(result, Result.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse Result Throwable: " + e.toString());
					}

					if (result4 == null) {
						Log.e(TAG,"#### set MAINIMAGE = null");
					} else {
						Log.e(TAG,"#### set MAINIMAGE:" + result4.getResult());
					}

					break;
				default:
					break;
				}
			} catch (Throwable e) {
				Log.e(TAG, mType + "onPostExecute Throwable:" + e.toString());
			}
		}
	}
	
	private String httpGet (String url) {
		String result = "";
		try {
			Request request = new Request.Builder().url(url).build();
			Response response = mOkHttpClient.newCall(request).execute();
			result = response.body().string();
		}catch (Throwable e) {
			Log.e(TAG, "httpGet Throwable:" + e.toString());
			if (e.toString().toLowerCase().contains("timeout")) {
				Result re = new Result();
				re.setResult("failed");
				re.setFailedMessage("Timeout");
				result = new Gson().toJson(re);
			}
		}
		return result;
	}
	private String httpPost (String url, String json) {
		String result = "";
		try {
			RequestBody body = RequestBody.create(JSON, json);
	        Request request = new Request.Builder().url(url).post(body).build();
	        Response response = mOkHttpClient.newCall(request).execute();
	        result = response.body().string();
		}catch (Throwable e) {
			Log.e(TAG, "httpPost Throwable:" + e.toString());
			if (e.toString().toLowerCase().contains("timeout")) {
				Result re = new Result();
				re.setResult("failed");
				re.setFailedMessage("Timeout");
				result = new Gson().toJson(re);
			}
		}
		return result;
	} 
	
	private class GetTask extends AsyncTask<Void,Void,String> {
		private String mUrl = "";
		private INTERACTTYPE mType;
		public GetTask(String url, INTERACTTYPE type) {
			mUrl = url;
			mType = type;
		}              

		@Override
		protected String doInBackground(Void... arg0) {
			try {
				
				return httpGet(mUrl);
				
				
			} catch (Throwable e) {
				Log.e(TAG, "doInBackground Throwable: " + e.toString());
			}
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				switch (mType) {
				case CONFIG:
					SystemConfig config = null;
					try {
						config = new Gson().fromJson(result, SystemConfig.class);
					}catch (Throwable e){
						Log.e(TAG, "parse Serverconfig Throwable: " + e.toString());
					}
					
					XiaoYuConfig xiyuConfig = null;
					HotFixConfig hotfixConfig = null;
					PushConfig pushConfig = null;
					Log.e(TAG, "config:" + config.getResult());
					if (config != null && config.getResult().equalsIgnoreCase("success") == true) {
						if (config.getServiceSettings() != null) {
							xiyuConfig = config.getServiceSettings().getXiaoyuConfig();
							hotfixConfig = config.getServiceSettings().getGAHFSConfig();
							pushConfig = config.getServiceSettings().getGBPushConfig();
						}
					}

					if (mConfigCallback != null) {
						mConfigCallback.configXiaoyu(xiyuConfig);
						mConfigCallback.configHotfix(hotfixConfig);
						mConfigCallback.configPush(pushConfig);
					}

					break;
				case CONTACTS:
					Contacts contacts = null;
					addrString = result;
					try {
						contacts = new Gson().fromJson(result, Contacts.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse Contacts Throwable: " + e.toString());
					}
					
					if (contacts != null && contacts.getResult().equalsIgnoreCase("success") == true) {
						AddressCeche.getInstance().setaddressData(contacts.getOrganization(),contacts.getUsers());
					} else {
						Log.e(TAG, "get Contacts Throwable: " + contacts == null?"unknow error":contacts.getFailedMessage());
					}
					break;
				case ONLINE:
					Online online = null;
					try {
						online = new Gson().fromJson(result, Online.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse Online Throwable: " + e.toString());
					}
					
					if (online != null && online.getResult().equalsIgnoreCase("success") == true) {
						if (online.getOnlineUsers() == null || online.getOnlineUsers().length <= 0) {
							break;
						}
						
						boolean needChange = false;
						if (onlineUsers == null || onlineUsers.length <= 0) {
							needChange = true;
							onlineUsers = online.getOnlineUsers();
							
						} else {
							if (online.getOnlineUsers().length != onlineUsers.length) {
								needChange = true;
								onlineUsers = online.getOnlineUsers();
							} else{
								for (int i = 0; i < onlineUsers.length; i++) {
									boolean isSame = false; 
									for (int j = 0; j < online.getOnlineUsers().length; j++) {
										if (onlineUsers[i].getUserName().equals(online.getOnlineUsers()[j].getUserName()) == true) {
											if (onlineUsers[i].getStatus().equals(online.getOnlineUsers()[j].getStatus()) == true) {
												isSame = true;
												break;
											}
										}
									}
									if (!isSame) {
										needChange = true;
										onlineUsers = online.getOnlineUsers();
										break;
									}
								}
							}
						}
						
						if (needChange) {
							AddressCeche.getInstance().setOnlineUsers(onlineUsers);
						}
					}
					break;
				case MEETING:
					MeetingDetail md = null;
					try {
						md = new Gson().fromJson(result, MeetingDetail.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse currentmeeting Throwable: " + e.toString());
					}
					
					if (md != null && md.getResult().equalsIgnoreCase("success") == true) {
						if (md.getMeeting() != null && md.getMeeting().getStatus().equalsIgnoreCase("end") == false) {
							if (mInteractCallback != null) {
								mInvitedMeeting.setType(md.getMeeting().getType());
								mInteractCallback.eventInvitedMeeting(mInvitedMeeting);
							}
						}
					}
					break;
				case GFCUSER:
					FrequentContactsInfoSet fc = null;

					try {
						fc = new Gson().fromJson(result, FrequentContactsInfoSet.class);
					} catch (Throwable e) {
						Log.e(TAG, "parse frequentcontacts Throwable: " + e.toString());
					}
					if (fc != null && fc.getResult().equalsIgnoreCase("success") == true) {
						AddressCeche.getInstance().setContactUsers(fc.getFavorites());
					} else {
						Log.e(TAG, "get frequentcontacts Throwable: " + fc == null?"unknow error":fc.getFailedMessage());
					}
					break;
				default:
					break;
				}
			} catch (Throwable e) {
				Log.e(TAG, mType + "onPostExecute Throwable: " + e.toString());
			}
		}
	}
}
