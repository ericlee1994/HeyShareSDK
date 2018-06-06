package com.shgbit.appui;

import android.app.Application;

import com.shgbit.hsuimodule.sdk.HSVideoSDK;

public class MyApplication extends Application{

    private String mServerIP = "http://www.shgbitcloud.com:4004";

    @Override
    public void onCreate() {
        super.onCreate();
        HSVideoSDK.getInstance().init(mServerIP, this);
    }
}
