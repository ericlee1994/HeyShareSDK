package com.shgbit.hsuimodule.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.shgbit.hsuimodule.R;
import com.shgbit.hsuimodule.util.ActivityUtils;

public class VideoActivity extends AppCompatActivity {

    public VideoPresenter mVideoPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);

        VideoFragment videoFragment =
                (VideoFragment) getSupportFragmentManager().findFragmentById(R.id.video_fragment);
        if (videoFragment == null) {
            videoFragment = VideoFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), videoFragment, R.id.video_fragment);
        }

        mVideoPresenter = new VideoPresenter();
    }

}
