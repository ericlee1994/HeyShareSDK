package com.shgbit.hsuimodule.activity;


import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hsuimodule.BasePresenter;
import com.shgbit.hsuimodule.BaseView;
import com.shgbit.hsuimodule.bean.DisplayModeEnum;
import com.shgbit.hsuimodule.bean.InvitedMeeting;
import com.shgbit.hsuimodule.bean.User;
import com.shgbit.hsuimodule.bean.VideoInfo;
import com.shgbit.hsuimodule.widget.VCDialog;

import java.util.ArrayList;

public interface VideoContract {

    interface View extends BaseView<Presenter> {
        void showDisplayMode(DisplayModeEnum displayModeEnum);
        void hidePopView();
        void showBottomLayout();
        void onVideoList(ArrayList<VideoInfo> mScreenList, ArrayList<VideoInfo> mOtherList, ArrayList<VideoInfo> mUnjoinedList);
        void showDialog(Object content, VCDialog.DialogType type);
        void showModeDialog();
        void updatePopView(ArrayList<MemberInfo> mScreenList, ArrayList<MemberInfo> mOtherList, ArrayList<MemberInfo> mUnjoinedList);
        void hideBottomLayout();
        void startRecord(boolean result, String error);
        void endRecord(boolean result, String error);
        void getRecordTime(long time);
        void initRecord(boolean isShow, String status, String meetingId);
        void onFinish();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);
        void connectNemo(String UserName, String Password);
        void changeDisplayMode(DisplayModeEnum displayModeEnum);
        void hidePopView();
        void clickBottomLayout();
        void getVideoInfos();
        void exchangeScreen(VideoInfo v0, VideoInfo v1);
        void setDialog(Object content, VCDialog.DialogType type);
        void refuseInvite();
        void getInviteUsers(User[] users);
        void getInviteMeeting();
        void clickMenuBtn(String type);
        void clickMenuPerson(MemberInfo memberInfo);
        void startRecord();
        void endRecord();
        void getRecord();
        void getRecordTime(long time);
        void startRecordThread();
        void endRecordThread();
        void hangUp();
        void finish();
    }
}
