package com.shgbit.hsuimodule.activity;

import android.content.Intent;
import android.util.Log;

import com.ainemo.sdk.otf.NemoSDK;
import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hssdk.bean.Status;
import com.shgbit.hssdk.manager.MeetingInfoManager;
import com.shgbit.hssdk.sdk.HeyShareSDK;
import com.shgbit.hsuimodule.R;
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

    private boolean foregroundCamera = true;
    private int cameraId = 1;

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
        mVideoView.showBottomLayout();
        mVideoView.updatePopView(mScreenList, mOtherList, mUnjoinedList);
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

    final String BTN_SHOW_PERSON = "btn_show_person";
    final String BTN_ADD_PERSON = "btn_add_person";
    final String BTN_BAN_MIC = "btn_ban_mic";
    final String BTN_CLOSE_CAMERA = "btn_voice_mode";
    final String BTN_VOICE_MODE = "btn_audio_mode";
    final String BTN_SWITCH_CAMERA = "btn_switch_camera";
    final String BTN_DISPLAY_MODE = "btn_display_mode";
    final String BTN_HANGUP = "btn_hangup";
    final String BTN_UPLOAD_PIC = "btn_uploadpic";
    final String BTN_OPEN_PIC = "btn_openpic";
    final String BTN_PIZHU = "btn_pizhu";

    @Override
    public void clickMenuBtn(String type) {
        if (BTN_SHOW_PERSON.equals(type)) {
            mVideoView.hideBottomLayout();
        } else {
            if (BTN_ADD_PERSON.equals(type)) {
//                Log.i(TAG, "[user operation]click add person");
//                if (hssdkContactListener != null) {
//                    boolean flag = hssdkContactListener.contact(mContext, R.id.video_fragment);
//                    if (!flag){
//                        syntony.startAddressList(true, "horizontal", false, null, null);
//                        syntony.setExCallBack(videoCallBack);
//                    }
//                }else {
//                    syntony.startAddressList(true, "horizontal", false, null, null);
//                    syntony.setExCallBack(videoCallBack);
//                }
            } else if (BTN_BAN_MIC.equals(type)) {
                Log.i(TAG, "[user operation]click muteMic btn:" + Common.ISMUTEMIC);

                Common.ISMUTEMIC = !Common.ISMUTEMIC;
                HeyShareSDK.getInstance().video().audioMute(Common.ISMUTEMIC);
            } else if (BTN_CLOSE_CAMERA.equals(type)) {
                Log.i(TAG, "[user operation]click off camera");

                Common.ISMUTECAMERA = !Common.ISMUTECAMERA;
                HeyShareSDK.getInstance().video().videoMute(Common.ISMUTECAMERA);
            } else if (BTN_VOICE_MODE.equals(type)) {
                Log.i(TAG, "[user operation]click into Voice Mode");

                Common.ISAUDIOMODE = !Common.ISAUDIOMODE;
                HeyShareSDK.getInstance().video().audioMode(Common.ISAUDIOMODE);
                if (!Common.ISAUDIOMODE){
                    HeyShareSDK.getInstance().video().videoMute(Common.ISMUTECAMERA);
                }
            } else if (BTN_SWITCH_CAMERA.equals(type)) {
                Log.i(TAG, "[user operation]click switch camera");

                if (Common.ISMUTECAMERA) {
                    return;
                }
                foregroundCamera = !foregroundCamera;
                cameraId = foregroundCamera ? 1 : 0;
                if(cameraId != 0 && cameraId !=1)
                {
                    foregroundCamera = !foregroundCamera;
                    this.cameraId = foregroundCamera ? 1 : 0;
                }
                else{
                    this.cameraId = cameraId;
                }
                HeyShareSDK.getInstance().video().switchCamera(cameraId);

            } else if (BTN_DISPLAY_MODE.equals(type)) {
                Log.i(TAG, "[user operation]click display mode");
                mVideoView.showModeDialog();
            } else if (BTN_HANGUP.equals(type)) {
                Log.i(TAG, "[user operation]click hangup btn");
                String hangupInfo;
                if (Common.isRecording){
                    hangupInfo = "如果退出会议，将终止录像，是否退出？";
                }else {
                    hangupInfo = "确定要退出会议吗？";
                }
                mVideoView.showDialog(hangupInfo, VCDialog.DialogType.Handup);
            } else if (BTN_UPLOAD_PIC.equals(type)) {
//                Log.i(TAG, "[user operation]click upload pic btn");
//                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("image/*");
//                startActivityForResult(intent, 2);
            } else if (BTN_OPEN_PIC.equals(type)) {
//                Log.i(TAG, "[user operation]click open pic btn");
//                openPic = !openPic;
//                if (openPic) {
//                    requestCtrl.downloadPic();
//                } else {
//                    MeetingInfoManager.getInstance().PicShare(false, null);
//                }
            } else if (BTN_PIZHU.equals(type)) {
//                Log.i(TAG, "[user operation]click pizhu btn");
//                if (mScreenList.size() > 0 && mScreenList.get(0) != null) {
//
//                    clickPizhu = true;
//                    String codeName = codeName(mScreenList);
//                    for (int i = 0; i < mScreenList.size(); i++) {
//                        if ((hasComment && curResource.equals(mScreenList.get(i).getResId())) || !hasComment){
//                            if (hasComment) {
//                                MeetingInfoManager.getInstance().CommetModeChange();
//                            }
//                            if (lastMode.equals(DISPLAY_MODE.NOT_FULL_ONEFIVE)) {
//                                lastModeFlag = DISPLAY_MODE.NOT_FULL_ONEFIVE;
//                                changeMode(DISPLAY_MODE.FULL);
//
//                            } else if (lastMode.equals(DISPLAY_MODE.NOT_FULL_QUARTER)) {
//                                lastModeFlag = DISPLAY_MODE.NOT_FULL_QUARTER;
//                                changeMode(DISPLAY_MODE.FULL);
//                            }
//                            if (hasComment) {
//                                mUIHandler.sendEmptyMessage(ENTER_COMMENT);
//                            }
//                            Log.e(TAG, "share resource :" + codeName(mScreenList));
//                            requestCtrl.postCmtEnter(codeName);
//                            break;
//                        }
//                    }
//                }
            }
            mVideoView.showBottomLayout();
        }
    }

    @Override
    public void clickMenuPerson(MemberInfo memberInfo) {
        if (memberInfo.getStatus().equals(Status.JOINED)) {
            HeyShareSDK.getInstance().video().popUpDown(memberInfo);
        }else {
            String[] user = {memberInfo.getId()};
            HeyShareSDK.getInstance().inviteUsers(Common.meetingId, user);
            HeyShareSDK.getInstance().video().stateChange(user, Status.INVITING);
        }
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
