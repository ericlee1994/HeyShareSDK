package com.shgbit.heysharesdk;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.shgbit.hssdk.bean.MemberInfo;
import com.shgbit.hssdk.bean.VideoCell;

import java.util.ArrayList;
import java.util.List;

public class VideoView extends ViewGroup{

    private static final String TAG = "VideoView";

    private Context context;
    private int displayCount = 6;
    private ArrayList<VideoCell> mScreenList;
    private Handler handler = new Handler();
    private final int UPDATEVIEW = 0x001;
    private int memberSize = 0;

    public VideoView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private Handler UIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATEVIEW:
                    List<MemberInfo> memberInfos = (List<MemberInfo>) msg.obj;
                    for (int i = 0; i < mScreenList.size(); i++) {
                        if (i < memberInfos.size()) {
                            mScreenList.get(i).setSourceID(memberInfos.get(i).getDataSourceID());
                            mScreenList.get(i).setLocal(memberInfos.get(i).isLocal());
                        }else {
                            mScreenList.get(i).setSourceID("");
                        }
                    }
                    requestRender();
                    requestLayout();
                    break;
            }
        }
    };


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG, "onLayout mScreenList size:" + mScreenList.size());
        for (int i = 0; i < mScreenList.size(); i++) {
            if (mScreenList.get(i) == null && mScreenList.get(i) != null) {
                continue;
            }

            int vl = 0, vt = 0, vr = 0, vb = 0, x = 5;
                if (memberSize == 1) {
                    vl = 0;
                    vt = 0;
                    vr = getWidth();
                    vb = getHeight();
                    mScreenList.get(0).layout(vl, vt, vr, vb);
                    mScreenList.get(0).bringToFront();
                }else {
                    switch (i) {
                        case 0:
                            vl = x;
                            vt = x;
                            vr = getWidth() / 2;
                            vb = getHeight();
                            break;
                        case 1:
                            vl = getWidth() / 2;
                            vt = x;
                            vr = getWidth();
                            vb = getHeight();
                            break;
                        default:
                            break;
                    }
                    mScreenList.get(i).layout(vl, vt, vr, vb);
                    mScreenList.get(i).bringToFront();
                }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            View child = getChildAt(childIndex);
            child.measure(MeasureSpec.makeMeasureSpec(width,
                    MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(
                    height, MeasureSpec.UNSPECIFIED));
        }
    }

    public void init(){
        mScreenList = new ArrayList<>();
        for (int i = 0; i < displayCount; i++){
            VideoCell videoCell = new VideoCell(context);
            mScreenList.add(videoCell);
            addView(videoCell);
        }

    }

    public void setLayoutInfos(List<MemberInfo> memberInfos){

        Message msg = Message.obtain(UIHandler, UPDATEVIEW, memberInfos);
        msg.sendToTarget();

        memberSize = memberInfos.size();
    }


    public void requestLocalFrame() {
        handler.removeCallbacks(drawLocalVideoFrameRunnable);
        requestLocalVideoRender();
    }

    private void requestLocalVideoRender() {
        if (getVisibility() == VISIBLE) {
            handler.postDelayed(drawLocalVideoFrameRunnable, 1000 / 15);
        }
    }

    private Runnable drawLocalVideoFrameRunnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < mScreenList.size(); i++){
                if (mScreenList.get(i) != null && mScreenList.get(i).isLocal()){
                    mScreenList.get(i).requestRender();
                    break;
                }
            }
            requestLocalVideoRender();
        }
    };

    private void requestRender() {
        handler.removeCallbacks(drawVideoFrameRunnable);
        handler.postDelayed(drawVideoFrameRunnable, 1000 / 15);
    }

    private Runnable drawVideoFrameRunnable = new Runnable() {
        @Override
        public void run() {
            if (mScreenList != null) {
                for (int i = 0; i < mScreenList.size(); i++) {
                    if (mScreenList.get(i) == null) {
                        continue;
                    }
                    if (mScreenList.get(i)!= null && !mScreenList.get(i).isLocal()){
                        mScreenList.get(i).requestRender();
                    }
                }
            }

            requestRender();
        }
    };

    public void stopRender() {
        handler.removeCallbacks(drawVideoFrameRunnable);
    }

    public void destroy() {
        destroyDrawingCache();
//        mScreenList.clear();
        handler.removeCallbacksAndMessages(null);
    }
}
