package com.shgbit.hsuimodule.activity;

import com.shgbit.hsuimodule.BasePresenter;
import com.shgbit.hsuimodule.BaseView;
import com.shgbit.hsuimodule.bean.DisplayModeEnum;

public interface VideoContract {

    interface View extends BaseView<Presenter> {
        void showDisplayMode(DisplayModeEnum displayModeEnum);
        void hidePopView();
        void showBottomLayout();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);
        void connectNemo(String UserName, String Password);
        void changeDisplayMode(DisplayModeEnum displayModeEnum);
        void hidePopView();
        void clickBottomLayout();
        void getVideoInfos();
    }
}
