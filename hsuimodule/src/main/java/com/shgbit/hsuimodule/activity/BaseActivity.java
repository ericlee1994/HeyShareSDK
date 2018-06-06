package com.shgbit.hsuimodule.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;

import com.serenegiant.utils.HandlerThreadHandler;

/**
 * Created by Eric on 2018/3/20.
 * @author Eric
 */

public class BaseActivity extends FragmentActivity {

    private static final String TAG = "BaseActivity";

    private final Handler mUIHandler = new Handler(Looper.getMainLooper());
    private final Thread mUiThread = mUIHandler.getLooper().getThread();

    private Handler mWorkerHandler;
    private long mWorkerThreadID = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mWorkerHandler == null){
            mWorkerHandler = HandlerThreadHandler.createHandler(TAG);
            mWorkerThreadID = mWorkerHandler.getLooper().getThread().getId();
        }

    }

    @Override
    protected void onDestroy() {
        if (mWorkerHandler != null) {
            try {
                mWorkerHandler.getLooper().quit();
            } catch (final Exception e) {

            }
            mWorkerHandler = null;
        }
        super.onDestroy();
    }

    protected final synchronized void queueEvent(final Runnable task, final long delayMillis) {
        if ((task == null) || (mWorkerHandler == null)) {return;}
        try {
            mWorkerHandler.removeCallbacks(task);
            if (delayMillis > 0) {
                mWorkerHandler.postDelayed(task, delayMillis);
            } else if (mWorkerThreadID == Thread.currentThread().getId()) {
                task.run();
            } else {
                mWorkerHandler.post(task);
            }
        } catch (final Exception e) {
            // ignore
        }
    }



}
