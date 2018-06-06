package com.shgbit.hsuimodule.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.shgbit.hsuimodule.R;


/**
 * Created by chenfangfang on 2017/9/25.
 */

public class CustomImageView extends View {
    private static final String TAG = "CustomImageView";
    private int IMAGESCALE_FILLXY = 0;
    private int IMAGESCALE_CENTER = 1;

    private int mImageScale;
    private Rect rect;
    private Rect mRectSmall;
    private int mWidth;
    private int mHeight;

    private Bitmap mBitmap;
    private Bitmap mBitmapC;
    private Bitmap mBitmap0;
    private Bitmap mBitmap1;
    private Bitmap mBitmap2;
    private Bitmap mBitmap3;
    private Bitmap mBitmap4;
    private Bitmap mBitmap5;
    private Bitmap mBitmap6;
    private Bitmap mBitmap7;
    private Bitmap mBitmap8;
    private Bitmap mBitmap9;

    private Paint mPaint;
    private String mText = "";

    private Handler stepTimeHandler;
    private Runnable mTicker;
    private boolean mTickerStopped = false;
    private long startTime = 0;

    public CustomImageView(Context context) {
        super(context);
        init();
    }
    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }
    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyle, 0);
//
//        int n = a.getIndexCount();
//
//        for (int i = 0; i < n; i++)
//        {
//            int attr = a.getIndex(i);
//
//            switch (attr)
//            {
//                case R.styleable.CustomImageView_imageScaleType:
//                    mImageScale = a.getInt(attr, 0);
//                    break;
//            }
//        }
//        a.recycle();
        init();
    }

    private void init(){
        //初始化
//        mText = "00:00:00";
        rect = new Rect();
        mRectSmall = new Rect();
        mPaint = new Paint();

        try {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_time);
            mBitmapC = BitmapFactory.decodeResource(getResources(), R.drawable.time_colon);
            mBitmap0 = BitmapFactory.decodeResource(getResources(), R.drawable.time_num_0);
            mBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.time_num_1);
            mBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.time_num_2);
            mBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.time_num_3);
            mBitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.time_num_4);
            mBitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.time_num_5);
            mBitmap6 = BitmapFactory.decodeResource(getResources(), R.drawable.time_num_6);
            mBitmap7 = BitmapFactory.decodeResource(getResources(), R.drawable.time_num_7);
            mBitmap8 = BitmapFactory.decodeResource(getResources(), R.drawable.time_num_8);
            mBitmap9 = BitmapFactory.decodeResource(getResources(), R.drawable.time_num_9);
        } catch (Throwable e){
            Log.e(TAG, "init Bitmap Throwable " + e.toString());
        }

//        startTime(10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            mWidth = specSize;
        } else
        {
            int desire = getPaddingLeft() + getPaddingRight() + mBitmap.getWidth();
            if (specMode == MeasureSpec.AT_MOST)// wrap_content
            {
                mWidth = Math.min(desire, specSize);
            }
        }

        /***
         * 设置高度
         */

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            mHeight = specSize;
        } else
        {
            int desire = getPaddingTop() + getPaddingBottom() + mBitmap.getHeight();
            if (specMode == MeasureSpec.AT_MOST)// wrap_content
            {
                mHeight = Math.min(desire, specSize);
            }
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        rect.left = getPaddingLeft();
        rect.right = mWidth - getPaddingRight();
        rect.top = getPaddingTop();
        rect.bottom = mHeight - getPaddingBottom();

        if (mImageScale == IMAGESCALE_FILLXY) {
            canvas.drawBitmap(mBitmap, null, rect, mPaint);
        } else {
            //计算居中的矩形范围
            rect.left = mWidth / 2 - mBitmap.getWidth() / 2;
            rect.right = mWidth / 2 + mBitmap.getWidth() / 2;
            rect.top = mHeight / 2 - mBitmap.getHeight() / 2;
            rect.bottom = mHeight / 2 + mBitmap.getHeight() / 2;

            canvas.drawBitmap(mBitmap, null, rect, mPaint);
        }

        char[] ch = mText.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            canvas.drawBitmap(drawBitmap(ch[i]), null, drawRect(i), mPaint);
        }
    }

    private Rect drawRect(int pos){
        int disx = (int) ((rect.right - rect.left) / 6.5);
        int disy = (rect.bottom - rect.top) / 5;
        mRectSmall.top = rect.top + disy;
        mRectSmall.bottom = rect.bottom - disy;
        if (pos == 0 | pos == 1){
            mRectSmall.left = (int) (rect.left + disx * (pos + 0.9));
            mRectSmall.right = (int) (rect.left + disx * (pos + 1.9));
        } else if (pos == 2) {
            mRectSmall.left = (int) (rect.left + disx * (pos + 0.9));
            mRectSmall.right = (int) (rect.left + disx * (pos + 1.5));
        } else if (pos == 3 | pos == 4){
            mRectSmall.left = (int) (rect.left + disx * (pos + 0.5));
            mRectSmall.right = (int) (rect.left + disx * (pos + 1.5));
//        } else if (pos == 5){
//            mRectSmall.left = (int) (rect.left + disx * (pos + 0.5));
//            mRectSmall.right = (int) (rect.left + disx * (pos + 1.1));
//        } else if (pos == 6 | pos == 7){
//            mRectSmall.left = (int) (rect.left + disx * (pos + 0.1));
//            mRectSmall.right = (int) (rect.left + disx * (pos + 1.1));
        }
        return mRectSmall;
    }

    private Bitmap drawBitmap(char c){
        if (c == '0'){
            return mBitmap0;
        } else if (c == '1'){
            return mBitmap1;
        } else if (c == '2'){
            return mBitmap2;
        } else if (c == '3'){
            return mBitmap3;
        } else if (c == '4'){
            return mBitmap4;
        } else if (c == '5'){
            return mBitmap5;
        } else if (c == '6'){
            return mBitmap6;
        } else if (c == '7'){
            return mBitmap7;
        } else if (c == '8'){
            return mBitmap8;
        } else if (c == '9'){
            return mBitmap9;
        } else if (c == ':'){
            return mBitmapC;
        }
        return mBitmap0;
    }

    //时间计数器，最多只能到99小时，如需要更大小时数需要改改方法
    private String showTimeCount(long time) {
        if (time >= 360000000) {
            return "00:00";
//            return "00:00:00";
        }
        String timeCount = "";
        long hourc = time / 3600000;
        String hour = "0" + hourc;
        hour = hour.substring(hour.length() - 2, hour.length());

        long minuec = (time - hourc * 3600000) / (60000);
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());

//        long secc = (time - hourc * 3600000 - minuec * 60000) / 1000;
//        String sec = "0" + secc;
//        sec = sec.substring(sec.length() - 2, sec.length());
//        timeCount = hour + ":" + minue + ":" + sec;
        timeCount = hour + ":" + minue;
        return timeCount;
    }

    public void startTime(int duration) {
        //开始计时
        mText = showTimeCount(duration * 1000);
        invalidate();

        stepTimeHandler = new Handler();
        startTime = System.currentTimeMillis() - duration * 1000;
        mTicker = new Runnable() {
            @Override
            public void run() {
                if (mTickerStopped){return;}
                String content = showTimeCount(System.currentTimeMillis() - startTime);
                mText = content;
                invalidate();

                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                stepTimeHandler.postAtTime(mTicker, next);
            }
        };
        //启动计时线程，定时更新
        mTicker.run();
    }

    public void stopTime(){
        if (stepTimeHandler != null) {
            stepTimeHandler.removeCallbacksAndMessages(null);
        }
        mTickerStopped = true;
    }
}
