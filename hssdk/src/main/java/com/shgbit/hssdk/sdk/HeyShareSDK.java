package com.shgbit.hssdk.sdk;

import android.content.Context;

import com.shgbit.hshttplibrary.AddressCeche;
import com.shgbit.hshttplibrary.MeetingCeche;
import com.shgbit.hshttplibrary.ServerInteractManager;
import com.shgbit.hshttplibrary.callback.ServerConfigCallback;
import com.shgbit.hshttplibrary.json.HotFixConfig;
import com.shgbit.hshttplibrary.json.PushConfig;
import com.shgbit.hshttplibrary.json.XiaoYuConfig;

public class HeyShareSDK {
    private static CommonCtrl commonCtrl;
    private static AddressCtrl addressCtrl;
    private static VideoCtrl videoCtrl;

    private static Context mContext;

    public static void Init(Context context, String IP){
        mContext = context;
        ServerInteractManager.getInstance().init(IP);
        ServerInteractManager.getInstance().setServerConfigCallback(new ServerConfigCallback() {
            @Override
            public void configXiaoyu(XiaoYuConfig config) {
                video().initNemo(mContext, config);
            }

            @Override
            public void configHotfix(HotFixConfig config) {

            }

            @Override
            public void configPush(PushConfig config) {

            }
        });
    }

    public static void Finalize(){
        ServerInteractManager.getInstance().finalize();
        MeetingCeche.getInstance().finalize();
        AddressCeche.getInstance().finalize();
        video().shutdownNemo();
        videoCtrl = null;
        commonCtrl = null;
        addressCtrl = null;
    }

    public static CommonCtrl common () {
        if (commonCtrl == null) {
            CommonCtrl.registerInstance();
        }
        return commonCtrl;
    }

    public static void setCommonCtrl (CommonCtrl c) {
        commonCtrl = c;
    }

    public static AddressCtrl address () {
        if (addressCtrl == null) {
            AddressCtrl.registerInstance();
        }
        return addressCtrl;
    }

    public static void setAddressCtrl (AddressCtrl a) {
        addressCtrl = a;
    }

    public static VideoCtrl video () {
        if (videoCtrl == null) {
            VideoCtrl.registerInstance();
        }
        return videoCtrl;
    }

    public static void setVideoCtrl (VideoCtrl v) {
        videoCtrl = v;
    }
}
