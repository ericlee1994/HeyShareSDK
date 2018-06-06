package com.shgbit.hsuimodule.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shgbit.hssdk.sdk.Common;
import com.shgbit.hsuimodule.R;
import com.shgbit.hsuimodule.bean.DisplayModeEnum;
import com.shgbit.hsuimodule.callback.IModeCallBack;


/**
 * Created by Eric on 2017/8/21.
 */

public class ModeDialog extends Dialog {
    private static final String TAG = "ModeDialog";
    private TextView mTxt_default;
    private TextView mTxt_quarter;
    private TextView mTxt_pip;
    private ImageView mImg_default;
    private ImageView mImg_four;
    private ImageView mImg_pip;
    private LinearLayout mLayout_pip;
    private IModeCallBack iModeCallBack;
    private int width;
    private int height;
    private int imgSize;
    private int margin;
    private int count;
    private boolean hasCotent;

    public ModeDialog(Context context) {
        super(context);
    }

    public ModeDialog(Context context, int themeResId, boolean hasContent) {
        super(context, themeResId);
        this.hasCotent = hasContent;
    }

    protected ModeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ModeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_dialog);

        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        win.setGravity(Gravity.CENTER);

        if (hasCotent){
            count = 3;
            width = (int) (Common.SCREENWIDTH * 0.7);
        }else {
            count = 2;
            width = (int) (Common.SCREENWIDTH * 0.48);
        }


        height = (int) (Common.SCREENHEIGHT * 0.4);

        lp.width = width;
        lp.height = height;

        win.setAttributes(lp);

        width = (int) (Common.SCREENWIDTH * 0.67);

        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView() {
        mImg_default = (ImageView) findViewById(R.id.img_default_display);
        mImg_four = (ImageView) findViewById(R.id.img_quarter_display);
        mImg_pip = (ImageView) findViewById(R.id.img_pip_display);
        mLayout_pip = (LinearLayout) findViewById(R.id.layout_pip);

        mImg_default.setOnClickListener(mClickListener);
        mImg_four.setOnClickListener(mClickListener);
        mImg_pip.setOnClickListener(mClickListener);


        if (hasCotent) {
            mImg_pip.setVisibility(View.VISIBLE);
//            mTxt_pip.setVisibility(View.VISIBLE);
            mLayout_pip.setVisibility(View.VISIBLE);
        }else {
            mImg_pip.setVisibility(View.GONE);
//            mTxt_pip.setVisibility(View.GONE);
            mLayout_pip.setVisibility(View.GONE);
        }
    }

    @Override
    public void show() {
        super.show();
        mHandler.sendEmptyMessageDelayed(0x011, 30 * 1000);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isShowing()) {
                dismiss();
            }
        }
    };

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (iModeCallBack != null) {
                int id = v.getId();
                if (id == R.id.img_default_display) {
                    dismiss();
                    iModeCallBack.getDisplayMode(DisplayModeEnum.NOT_FULL_ONEFIVE);
                }else if (id == R.id.img_quarter_display) {
                    dismiss();
                    iModeCallBack.getDisplayMode(DisplayModeEnum.NOT_FULL_QUARTER);
                }else if (id == R.id.img_pip_display) {
                    dismiss();
                    iModeCallBack.getDisplayMode(DisplayModeEnum.FULL_PIP_SIX);
                }
//                switch (v.getId()) {
//                    case R.id.img_default_display:
//                        dismiss();
//                        IModeCallBack.getDisplayMode(DISPLAY_MODE.NOT_FULL_ONEFIVE);
//                        break;
//                    case R.id.img_quarter_display:
//                        dismiss();
//                        IModeCallBack.getDisplayMode(DISPLAY_MODE.NOT_FULL_QUARTER);
//                        break;
//                    case R.id.img_pip_display:
//                        dismiss();
//                        IModeCallBack.getDisplayMode(DISPLAY_MODE.FULL_PIP_SIX);
//                        break;
//                    default:
//                        break;
//                }
            }
        }
    };

    public void setIModeCallBack(IModeCallBack iModeCallBack){
        this.iModeCallBack = iModeCallBack;
    }
}
