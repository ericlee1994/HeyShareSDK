package com.shgbit.hssdk.callback;

import com.ainemo.sdk.otf.LoginResponseData;
import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hssdk.sdk.VideoCtrl;

import java.util.ArrayList;

public interface VideoStateCallback {
    void onConnectFailed(String err);
    void onConnectSuccess(LoginResponseData loginResponseData);
    void onCallFailed(String err);
    void onCallStateChange(VideoCtrl.VideoCallState state, String s);
    void onMemberChanged(ArrayList<MemberInfo> mScreen, ArrayList<MemberInfo> mOther, ArrayList<MemberInfo> mUnjoined, int duration, boolean isMemberContent, String meetingName);
    void onMemberSizeChanged(int joinedSize, int memberSize);
}
