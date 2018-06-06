package com.shgbit.hsuimodule.callback;


/**
 * Created by Administrator on 2017/11/1 0001.
 */

public interface IVideoRecordCallBack {
    void initRecord(boolean isShow, String status, String meetingId);
    void startRecord(boolean result, String error);
    void endRecord(boolean result, String error);
}
