package com.shgbit.hsuimodule.callback;


import com.shgbit.hssdk.bean.VI;

/**
 * Created by Eric on 2017/6/21.
 */

public interface IViewLayoutCallBack {
    void closePic();
    void changeId(int id);
    void changeStatus(int id);
    void backToDefaultMode(VI vi);
    void hideViewLayout(String id);
}
