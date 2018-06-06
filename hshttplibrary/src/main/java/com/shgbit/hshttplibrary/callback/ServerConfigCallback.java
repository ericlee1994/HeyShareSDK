package com.shgbit.hshttplibrary.callback;


import com.shgbit.hshttplibrary.json.HotFixConfig;
import com.shgbit.hshttplibrary.json.PushConfig;
import com.shgbit.hshttplibrary.json.XiaoYuConfig;

/**
 * Created by Administrator on 2018/2/13.
 */

public interface ServerConfigCallback {
    void configXiaoyu(XiaoYuConfig config);
    void configHotfix(HotFixConfig config);
    void configPush(PushConfig config);
}
