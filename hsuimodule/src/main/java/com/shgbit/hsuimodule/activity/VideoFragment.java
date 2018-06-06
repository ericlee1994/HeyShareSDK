package com.shgbit.hsuimodule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shgbit.hsuimodule.R;
import com.shgbit.hsuimodule.bean.DisplayModeEnum;
import com.shgbit.hsuimodule.util.Common;
import com.shgbit.hsuimodule.widget.MyVideoVIew;
import com.shgbit.hsuimodule.widget.PopupOldView;
import com.shgbit.hsuimodule.widget.TitleLayout;

import static com.google.common.base.Preconditions.checkNotNull;


public class VideoFragment extends Fragment implements VideoContract.View {

    private static final String TAG = "VideoFragment";

    private VideoContract.Presenter mPresenter;

    private RelativeLayout mWholeLayout;
    private MyVideoVIew videoView;
    private TitleLayout mTopLayout;
    private LinearLayout mBottomLayout;
    private PopupOldView popupView;
    private DisplayModeEnum lastMode;
    private DisplayModeEnum lastModeFlag;
    private boolean isModeChange = false;

    private boolean muteCamera = false;
    private boolean muteMic = true;
    private boolean isAudioMode = false;

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(VideoContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_video, container, false);

//        frameLayout = findViewById(R.id.video_fragment);
        mWholeLayout = root.findViewById(R.id.whole_layout);
        lastMode = DisplayModeEnum.FULL_FOUR;
        lastModeFlag = DisplayModeEnum.NOT_FULL_ONEFIVE;
        mPresenter.changeDisplayMode(DisplayModeEnum.NOT_FULL_ONEFIVE);

        mPresenter.connectNemo(Common.USERNAME, Common.PASSWORD);

        return root;

    }


    @Override
    public void showDisplayMode(DisplayModeEnum displayModeEnum) {
        Log.i(TAG, "change mode to " + displayModeEnum.toString());
        if (lastMode.equals(displayModeEnum)) {
            return;
        }
        isModeChange = true;
        mWholeLayout.removeAllViews();
        if (videoView != null) {
            videoView.destroy();
            videoView.removeAllViews();
            videoView = null;
        }
        if (displayModeEnum.equals(DisplayModeEnum.NOT_FULL_ONEFIVE)
                || displayModeEnum.equals(DisplayModeEnum.NOT_FULL_QUARTER)) {
            mTopLayout = new TitleLayout(getContext());
            mBottomLayout = new LinearLayout(getContext());
            popupView = new PopupOldView(getContext(), muteMic, muteCamera, isAudioMode);
            videoView = new MyVideoVIew(getContext(), displayModeEnum);


//            dismissPopup();
            mPresenter.hidePopView();
            mWholeLayout.addView(mTopLayout);
            mWholeLayout.addView(videoView);
            mWholeLayout.addView(popupView);
            mWholeLayout.addView(mBottomLayout);

            videoView.setiVideoViewCallBack(mVideoViewListener);
            mTopLayout.setITitleCallBack(mTitleListener);
            RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT / 10);
            mTopLayout.setLayoutParams(topParams);
            mTopLayout.setOrientation(LinearLayout.HORIZONTAL);

            RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT * 5 / 6);
            videoParams.topMargin = Common.SCREENHEIGHT / 10;
            videoView.setLayoutParams(videoParams);

            RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT / 20);
            bottomParams.topMargin = Common.SCREENHEIGHT * 19 / 20;
            mBottomLayout.setLayoutParams(bottomParams);
            mBottomLayout.setBackgroundResource(R.drawable.btn_arrow_up);
            mBottomLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.clickBottomLayout();
                }
            });

            RelativeLayout.LayoutParams popParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT);
            popupView.setLayoutParams(popParams);

        } else if (displayModeEnum.equals(DisplayModeEnum.FULL_PIP)) {
            videoView = new MyVideoVIew(getContext(), displayModeEnum);
            videoView.setiVideoViewCallBack(mVideoViewListener);
            RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT);
            videoView.setLayoutParams(videoParams);
            mWholeLayout.addView(videoView);
        } else if (displayModeEnum.equals(DisplayModeEnum.FULL)) {
            videoView = new MyVideoVIew(getContext(), displayModeEnum);
            videoView.setiVideoViewCallBack(mVideoViewListener);
            RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            videoView.setLayoutParams(videoParams);
            mWholeLayout.addView(videoView);
        } else if (displayModeEnum.equals(DisplayModeEnum.FULL_PIP_SIX)) {
            videoView = new MyVideoVIew(getContext(), displayModeEnum);
            popupView = new PopupOldView(this, muteMic, muteCamera, audioMode);
            mBottomLayout = new LinearLayout(getContext());
            mPresenter.hidePopView();
            videoView.setiVideoViewCallBack(mVideoViewListener);

            mWholeLayout.addView(videoView);
            mWholeLayout.addView(popupView);
            mWholeLayout.addView(mBottomLayout);

            RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT);
            videoView.setLayoutParams(videoParams);

            RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT / 20);
            bottomParams.topMargin = Common.SCREENHEIGHT * 19 / 20;

            mBottomLayout.setLayoutParams(bottomParams);
            mBottomLayout.setBackgroundResource(R.drawable.btn_arrow_up);
            mBottomLayout.setOnClickListener(mClickListener);

            RelativeLayout.LayoutParams popParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Common.SCREENHEIGHT);
            popupView.setLayoutParams(popParams);

        }
        lastMode = displayModeEnum;
        videoView.requestLocalFrame();
//        if (mainView != null) {
//            videoView.setPizhu(mainView);
//        }
    }

    @Override
    public void hidePopView() {
        if (popupView != null) {
            popupView.setVisibility(View.INVISIBLE);
            popupView.dismissPopupView();
        }
    }

    @Override
    public void showBottomLayout() {
        mUIHandler.sendEmptyMessage(SHOW_HIDE_LAYOUT);
        popupView.setMeetingUserData(mScreenList, mOtherList, mUnjoinedList);
    }
}
