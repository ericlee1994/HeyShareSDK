package com.shgbit.hsuimodule.callback;


import com.shgbit.hssdk.bean.MemberInfo;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public interface IPopViewCallBack {
    void onClickPopBtn();
    void onClickMenuBtn(String type);
    void onClickPerson(MemberInfo memberInfo);
}
