package com.shgbit.hsuimodule.callback;


import com.shgbit.hssdk.bean.VI;

/**
 * Created by Eric on 2017/5/24.
 */

public interface IVideoViewCallBack {
    void backToDefaultMode(VI vi0, VI vi);
    void closePic();
    void switchPosition();
}
