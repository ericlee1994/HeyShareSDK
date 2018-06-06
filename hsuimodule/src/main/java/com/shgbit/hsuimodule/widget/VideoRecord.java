package com.shgbit.hsuimodule.widget;


import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.shgbit.hshttplibrary.callback.ServerRecordCallback;
import com.shgbit.hshttplibrary.json.Meeting;
import com.shgbit.hshttplibrary.json.MeetingRecord;
import com.shgbit.hssdk.bean.CurrentMeetingInfo;
import com.shgbit.hssdk.sdk.Common;
import com.shgbit.hssdk.sdk.HeyShareSDK;
import com.shgbit.hsuimodule.callback.IVideoRecordCallBack;


/**
 * Created by Administrator on 2017/10/25 0025.
 */

public class VideoRecord {
    private static final String TAG = "VideoRecord";

    private long startTime = 0;

    private Context mContext;

    private QueryStatusThread thread;

    private IVideoRecordCallBack iVideoRecordCallBack;

    private static VideoRecord mCollector;

    public VideoRecord (Context context) {
        mContext = context;
//        ServerInteractManager.getInstance().setServerRecordCallback(mCallback);
        HeyShareSDK.getInstance().setServerRecordCallback(mCallback);
    }

    public static VideoRecord getInstance(Context context) {
        if (mCollector == null) {
            mCollector = new VideoRecord(context.getApplicationContext());
        }
        return mCollector;
    }

    public void finish () {
        try {
            if (thread != null) {
                thread.join(100);
                thread.interrupt();
                thread = null;
            }
            mCollector = null;
//            ServerInteractManager.getInstance().removeServerRecordCallback(mCallback);
            HeyShareSDK.getInstance().removeServerRecordCallback(mCallback);
        } catch (Throwable e) {
            Log.e(TAG, "finalize Throwable:" + e.toString());
        }
    }

    public void setCallBack(IVideoRecordCallBack iV) {
        iVideoRecordCallBack = iV;
    }

    public long getStartTime () {
        return startTime;
    }

    public void startQueryStatusThread() {
        try {
            if (thread != null) {
                thread.join(100);
                thread.interrupt();
                thread = null;
            }

            thread = new QueryStatusThread();
            thread.start();
        } catch (Throwable e) {
            Log.e(TAG, "startThread Throwable:" + e.toString());
        }
    }

    public void startRecord (MeetingRecord meetingRecord) {
//        ServerInteractManager.getInstance().startRecord(meetingRecord);
        HeyShareSDK.getInstance().startRecord(meetingRecord.getMeetingId());
    }

    public void endRecord (MeetingRecord meetingRecord) {
//        ServerInteractManager.getInstance().endRecord(meetingRecord);
        HeyShareSDK.getInstance().endRecord(meetingRecord.getMeetingId());
    }

    private ServerRecordCallback mCallback = new ServerRecordCallback() {
        @Override
        public void startRecord(boolean result, String err) {
            if (iVideoRecordCallBack != null) {
                iVideoRecordCallBack.startRecord(result, err);
                if (result) {
                    startTime = SystemClock.elapsedRealtime();
                }
            }
        }

        @Override
        public void endRecord(boolean result, String err) {
            if (iVideoRecordCallBack != null) {
                iVideoRecordCallBack.endRecord(result, err);
                if (result) {
                    startTime = 0;
                }
            }
        }
    };

    private class QueryStatusThread extends Thread {
        @Override
        public void run() {
            try {
                String result = "";
                try {
                    result = HeyShareSDK.getInstance().getSyncCurrentMeeting();
                } catch (Throwable e) {
                    Log.e(TAG, "httpGet Throwable: " + e.toString());
                }

                CurrentMeetingInfo cmi = null;
                try {
                    cmi = new Gson().fromJson(result, CurrentMeetingInfo.class);
                } catch (Throwable e) {
                    Log.e(TAG, "parse CurrentMeetingInfo Throwable: " + e.toString());
                }

                if (iVideoRecordCallBack != null) {
                    if (cmi != null && cmi.getMeeting() != null) {
                        Meeting meeting = cmi.getMeeting();
                        if (meeting.getCreatedUser().getUserName().equals(Common.USERNAME) == true) {
                            iVideoRecordCallBack.initRecord(true, meeting.getRecord() == null?"":meeting.getRecord().getStatus(),meeting.getMeetingId());
                        } else {
                            iVideoRecordCallBack.initRecord(false, "", "");
                        }
                    }
                }
            } catch (Throwable e) {
                Log.e(TAG, "QueryStatusThread Throwable:" + e.toString());
            }
        }
    }
}
