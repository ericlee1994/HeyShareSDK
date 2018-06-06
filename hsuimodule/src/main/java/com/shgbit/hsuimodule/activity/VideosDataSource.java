package com.shgbit.hsuimodule.activity;

import com.shgbit.hssdk.bean.MemberInfo;

import java.util.ArrayList;

public interface VideosDataSource {
    interface LoadVideosCallback {
        void onVideosLoaded(ArrayList<MemberInfo> mScreenList, ArrayList<MemberInfo> mOtherList, ArrayList<MemberInfo> mUnjoinedList);
        void onNoVideosAvailable();
    }
    void getVideos(LoadVideosCallback loadVideosCallback);
}
