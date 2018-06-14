package com.shgbit.hsuimodule.activity;

import android.content.Context;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {
    public static VideoRepository provideVideoRepository(Context context) {
        checkNotNull(context);
        return VideoRepository.getInstance(VideoDataSource.getInstance());
    }
}
