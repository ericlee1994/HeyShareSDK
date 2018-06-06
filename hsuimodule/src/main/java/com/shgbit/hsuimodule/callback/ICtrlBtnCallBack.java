package com.shgbit.hsuimodule.callback;


import com.shgbit.hssdk.bean.MemberInfo;

/**
 * Created by Eric on 2018/1/29.
 */

public interface ICtrlBtnCallBack {
    void mqttMsg(String Object, String Operation, MemberInfo memberInfo);
}
