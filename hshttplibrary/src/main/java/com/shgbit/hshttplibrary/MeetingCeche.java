package com.shgbit.hshttplibrary;

import android.text.format.Time;
import android.util.Log;


import com.shgbit.hshttplibrary.json.Meeting;
import com.shgbit.hshttplibrary.tool.ComparatorTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MeetingCeche {
	private final String TAG = "MeetingCeche";
	private static MeetingCeche instance;
	
	private HashMap<String,ArrayList<Meeting>> mMapMeetings;
	private ArrayList<Meeting> mMeetings;
	
	public MeetingCeche() {
	}
	
	public static MeetingCeche getInstance () {
		if (instance == null) {
			instance = new MeetingCeche();
		}
		return instance;
	}
	
	public void finalize () {
		mMeetings = null;
		mMapMeetings = null;
		instance = null;
	}
	
	public boolean setMeetings (Meeting[] meetings) {
		if (mMeetings == null && meetings == null) {
			return false;
		}

		if (mMeetings == null || meetings == null) {
			copyData(meetings);
			return true;
		}

		if (mMeetings.size() != meetings.length) {
			copyData(meetings);
			return true;
		} else {
			boolean isneedupdate = false;
			for (int i = 0; i < mMeetings.size(); i++) {
				if (mMeetings.get(i) == null && meetings[i] == null) {
					continue;
				}
				if (mMeetings.get(i) == null || meetings[i] == null) {
					isneedupdate = true;
					break;
				}
				if (mMeetings.get(i).getMeetingId().equals(meetings[i].getMeetingId())) {
					if (mMeetings.get(i).getStatus().equals(meetings[i].getStatus())) {
						continue;
					} else {
						isneedupdate = true;
						break;
					}
				} else {
					isneedupdate = true;
					break;
				}
			}

			if (isneedupdate) {
				copyData(meetings);
				return true;
			} else {
				return false;
			}
		}
	}

	private void copyData (Meeting[] meetings) {
		if (meetings == null) {
			mMeetings = null;
		} else {
			Arrays.sort(meetings,new ComparatorTime());

			if (mMeetings == null) {
				mMeetings = new ArrayList<>();
			} else {
				mMeetings.clear();
			}

			for (int i = 0; i < meetings.length; i++) {
				if (meetings[i] == null) {
					continue;
				}
				if (meetings[i].getStatus().equalsIgnoreCase("end") == true) {
					continue;
				} else {
					mMeetings.add(meetings[i]);
				}
			}
		}

		copyMapData();
	}

	private void copyMapData () {
		if (mMapMeetings == null) {
			mMapMeetings = new HashMap<>();
		} else {
			mMapMeetings.clear();
		}

		if (mMeetings == null) {
			return;
		}

		for (int i = 0; i < mMeetings.size(); i++) {
			if (mMeetings.get(i) == null) {
				continue;
			}
			String key = getKey(mMeetings.get(i).getStartTime());
			if (key == null || key.equals("")) {
				continue;
			}
			if (mMapMeetings.containsKey(key)) {
				mMapMeetings.get(key).add(mMeetings.get(i));
			} else {
				ArrayList<Meeting> list = new ArrayList<>();
				list.add(mMeetings.get(i));
				mMapMeetings.put(key,list);
			}
		}
	}

	private String getKey (String value) {
		String key = "";
		long time = getTimeMillis(value);
		Time t = new Time();
		t.set(time);
		Time t2 = new Time();
		t2.setToNow();
		t2.hour = 23;
		t2.minute = 59;
		t2.second = 59;

		if (Time.compare(t,t2) <= 0) {
			key = "" + t2.year + t2.month + t2.monthDay;
		} else {
			key = "" + t.year + t.month + t.monthDay;
		}

		return key;
	}
	
	public Meeting getMeeting (String meetingId) {
		if (meetingId == null || meetingId.equals("")) {
			return null;
		}
		
		if (mMeetings == null || mMeetings.size() <= 0) {
			return null;
		}
		
		try {
			for (int i = 0; i < mMeetings.size(); i++) {
				if (mMeetings.get(i).getMeetingId().equals(meetingId)) {
					return mMeetings.get(i);
				}
			}
		} catch (Throwable e) {
			Log.e(TAG, "getMeeting Throwable: " + e.toString());
		}
		
		return null;
	}
	
	public ArrayList<Meeting> getDayOfMeetings (int year, int month, int day) {
		if (mMapMeetings == null) {
			return new ArrayList<>();
		}

		String key = "" + year + month + day;

		if (mMapMeetings.containsKey(key)) {
			return mMapMeetings.get(key);
		} else {
			return new ArrayList<>();
		}
	}
	
	public boolean checkHasMeeting (int year, int month, int day) {
		if (mMapMeetings == null) {
			return false;
		}

		String key = "" + year + month + day;

		if (mMapMeetings.containsKey(key)) {
			return true;
		} else {
			return false;
		}
	}

	private long getTimeMillis(String timeStr) {
		SimpleDateFormat sDateSDF;
		if (timeStr.contains("/") == true){
			if (timeStr.contains("T") == true) {
				timeStr = timeStr.replace("Z", " UTC");
				sDateSDF = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss.SSS Z");
			} else {
				sDateSDF = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA);
			}
		}else {
			if (timeStr.contains("T") == true) {
				timeStr = timeStr.replace("Z", " UTC");
				sDateSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
			} else {
				sDateSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
			}
		}
		long l = 0;
		Date d;
		try {
			d = sDateSDF.parse(timeStr);
			l = d.getTime();

		} catch (Exception e) {

		}
		return l;
	}
}
