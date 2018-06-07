package com.shgbit.hsuimodule.activity;

import android.util.Log;

import com.ainemo.sdk.otf.NemoSDK;
import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hssdk.bean.Status;
import com.shgbit.hssdk.manager.MeetingInfoManager;
import com.shgbit.hssdk.sdk.HeyShareSDK;
import com.shgbit.hsuimodule.bean.DisplayModeEnum;
import com.shgbit.hsuimodule.bean.InvitedMeeting;
import com.shgbit.hsuimodule.bean.User;
import com.shgbit.hsuimodule.bean.VideoInfo;
import com.shgbit.hsuimodule.util.Common;
import com.shgbit.hsuimodule.widget.VCDialog;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class VideoPresenter implements VideoContract.Presenter{

    private static final String TAG = "VideoPresenter";

    private final VideoContract.View mVideoView;
    private final VideoRepository mVideoRepository;

    private ArrayList<MemberInfo> mScreenList;
    private ArrayList<MemberInfo> mOtherList;
    private ArrayList<MemberInfo> mUnjoinedList;
    private String[] mUsers;

    private String inviteMeetingId;
    private String otherMeetingInviter;

    public VideoPresenter(VideoRepository videoRepository, VideoContract.View videoView){
        mVideoView = checkNotNull(videoView, "videoView cannot be null!");
        mVideoRepository = checkNotNull(videoRepository, "videoRepository cannot be null!");
        mVideoView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void connectNemo(String UserName, String Password) {
        checkNotNull(UserName, "UserName cannot be null!");
        checkNotNull(Password, "Password cannot be null");
        HeyShareSDK.getInstance().video().connect(UserName, Password);
    }

    @Override
    public void changeDisplayMode(DisplayModeEnum displayModeEnum) {
        mVideoView.showDisplayMode(displayModeEnum);
    }

    @Override
    public void hidePopView() {
        mVideoView.hidePopView();
    }

    @Override
    public void clickBottomLayout() {

    }

    @Override
    public void getVideoInfos() {
        mVideoRepository.getVideos(new VideosDataSource.LoadVideosCallback() {
            @Override
            public void onVideosLoaded(ArrayList<MemberInfo> mScreen, ArrayList<MemberInfo> mOther, ArrayList<MemberInfo> mUnjoined) {

                if (mScreen != null && mScreen.size() > 0) {
                    if (!mScreen.get(0).getDataSourceID().equalsIgnoreCase(NemoSDK.getLocalVideoStreamID())) {
                        HeyShareSDK.getInstance().video().forceLayout(mScreen.get(0).getParticipantId());
                    }
                }

                if (mScreenList == null) {
                    mScreenList = new ArrayList<>();
                }
                mScreenList.clear();
                for (int i = 0; i < mScreen.size(); i++) {
                    mScreenList.add(new MemberInfo(mScreen.get(i)));
                }

                if (mOtherList == null) {
                    mOtherList = new ArrayList<>();
                }
                mOtherList.clear();
                for (int i = 0; i < mOther.size(); i++) {
                    mOtherList.add(new MemberInfo(mOther.get(i)));
                }

                if (mUnjoinedList == null) {
                    mUnjoinedList = new ArrayList<>();
                }
                mUnjoinedList.clear();
                for (int i = 0; i < mUnjoined.size(); i++) {
                    mUnjoinedList.add(new MemberInfo(mUnjoined.get(i)));
                }
            }

            @Override
            public void onNoVideosAvailable() {

            }
        });
    }

    @Override
    public void exchangeScreen(VideoInfo v0, VideoInfo v1) {
        MemberInfo memberInfo0 = new MemberInfo();
        memberInfo0.setId(v0.getId());
        memberInfo0.setSessionType(v0.getSessionType());
        MemberInfo memberInfo1 = new MemberInfo();
        memberInfo1.setId(v1.getId());
        memberInfo1.setSessionType(v1.getSessionType());
        HeyShareSDK.getInstance().video().screenExchange(memberInfo0, memberInfo1);
    }

    @Override
    public void setDialog(Object content, VCDialog.DialogType type) {

        mVideoView.showDialog(content, type);
    }

    @Override
    public void refuseInvite() {
        HeyShareSDK.getInstance().refuseInviting(inviteMeetingId, otherMeetingInviter);
    }

    @Override
    public void getInviteUsers(User[] users) {
        mVideoRepository.getInviteUsers(new VideosDataSource.InviteUsersCallback() {
            @Override
            public void onInviteUsers(User[] users) {
                String[] userName = new String[users.length];
//                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < users.length; i++) {
                    userName[i] = users[i].getUserName();
//                    arrayList.add(users[i].getUserName());
//                    mUserInvites = new String[arrayList.size()];
//                    arrayList.toArray(mUserInvites);
                    mUsers = userName;
                }
            }
        });
    }

    @Override
    public void getInviteMeeting() {
        mVideoRepository.getInviteMeeting(new VideosDataSource.InviteMeetingCallback() {
            @Override
            public void onInviteMeeting(InvitedMeeting invitedMeeting) {
                inviteMeetingId = invitedMeeting.getMeetingId();
                otherMeetingInviter = invitedMeeting.getInviter();
            }
        });
    }

    @Override
    public void hangUp() {
        if (mUnjoinedList != null) {
            for (int i = 0; i < mUnjoinedList.size(); i++) {
                if (mUnjoinedList.get(i).getStatus().equals(Status.INVITING)) {
                    if (mUsers != null || mUsers.length > 0) {
                        for (int j = 0; j < mUsers.length; j++) {
                            if (mUnjoinedList.get(i).getId().equals(mUsers[j])) {
                                String[] user1 = {mUnjoinedList.get(i).getId()};
                                HeyShareSDK.getInstance().video().stateChange(user1, Status.WAITING);
                                HeyShareSDK.getInstance().video().cancelInviting(Common.meetingId, mUnjoinedList.get(i).getId());
                                Log.i(TAG, "cancel invite:" + mUnjoinedList.get(i).getId());
                            }
                        }
                    }
                }
            }
        }
        HeyShareSDK.getInstance().video().handupMeeting();
        HeyShareSDK.getInstance().video().quitMeeting(Common.USERNAME, Common.meetingId);
    }

    @Override
    public void finish() {
        HeyShareSDK.getInstance().video().switchCamera(1);
        HeyShareSDK.getInstance().video().finalize();
        mVideoView.onFinish();
    }
}
