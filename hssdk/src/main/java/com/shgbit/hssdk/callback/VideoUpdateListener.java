package com.shgbit.hssdk.callback;

import com.shgbit.hssdk.bean.MemberInfo;

import java.util.ArrayList;

public interface VideoUpdateListener {
    void onMemberChanged(ArrayList<MemberInfo> mScreen, ArrayList<MemberInfo> mOther, ArrayList<MemberInfo> mUnjoined, int duration, boolean isMemberContent, String meetingName);
    void onMemberSizeChanged(int joinedSize, int totalSize);
}
