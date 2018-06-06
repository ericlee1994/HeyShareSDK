package com.shgbit.hsuimodule.activity;

import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hssdk.sdk.HeyShareSDK;
import com.shgbit.hsuimodule.bean.DisplayModeEnum;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class VideoPresenter implements VideoContract.Presenter{

    private final VideoContract.View mVideoView;
    private final VideoRepository mVideoRepository;

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
            public void onVideosLoaded(ArrayList<MemberInfo> mScreenList, ArrayList<MemberInfo> mOtherList, ArrayList<MemberInfo> mUnjoinedList) {

            }

            @Override
            public void onNoVideosAvailable() {

            }
        });
    }
}
