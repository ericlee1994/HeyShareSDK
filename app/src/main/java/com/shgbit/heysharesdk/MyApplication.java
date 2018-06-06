package com.shgbit.heysharesdk;

import android.app.Application;

import com.shgbit.hssdk.sdk.HeyShareSDK;

public class MyApplication extends Application {

    private String mServerIP = "http://www.shgbitcloud.com:4005";

    @Override
    public void onCreate() {
        super.onCreate();
        HeyShareSDK.getInstance().init(this, mServerIP);
    }
}
