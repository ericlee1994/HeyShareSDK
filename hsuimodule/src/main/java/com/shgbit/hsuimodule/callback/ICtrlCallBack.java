package com.shgbit.hsuimodule.callback;


/**
 * Created by Eric on 2017/12/15.
 */

public interface ICtrlCallBack {
    void getTracks(String[] tracks);
    void getResource(String resource);
    void exit();
    void getUploadState(boolean success);
}
