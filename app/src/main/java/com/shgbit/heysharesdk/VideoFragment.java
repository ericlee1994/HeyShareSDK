package com.shgbit.heysharesdk;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hssdk.sdk.HeyShareSDK;

import java.util.List;

public class VideoFragment extends Fragment{

    private VideoView mVideoView;
    private Button button;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mVideoView = view.findViewById(R.id.video_view);
        button = view.findViewById(R.id.btn_hangup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeyShareSDK.getInstance().quitMeeting();
            }
        });
        checkPermission();

    }

    public void releaseResource() {

        mVideoView.stopRender();
        mVideoView.destroy();

//        NemoSDK.getInstance().releaseCamera();


    }

    public void onVideoDataSourceChange(List<MemberInfo> memberInfos){
        if (mVideoView != null) {
            mVideoView.setLayoutInfos(memberInfos);
        }
    }

    public void requestLocalFrame(){
        if (mVideoView != null) {
            mVideoView.requestLocalFrame();
        }
    }

    private void checkPermission() {
        if (!(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                !(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 0);
        } else if (!(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        } else if (!(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
        }
    }
}
