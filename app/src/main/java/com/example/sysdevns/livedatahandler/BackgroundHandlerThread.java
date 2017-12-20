package com.example.sysdevns.livedatahandler;

import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

public class BackgroundHandlerThread extends HandlerThread {

    private static final String TAG = "BackgroundHandlerThread";

    private static final int START = 1;
    private static final int FINISHED = 3;

    private MutableLiveData<Integer> counter;

    private Handler backgroundHandler;
    private Handler foregroundHandler;
    private HandlerCallback callback;

    private boolean okToContinue = true;

    BackgroundHandlerThread(String name) {
        super(name);
        Log.d(TAG, "BackgroundHandlerThread: constructor");
        counter = new MutableLiveData<>();
        resetCount();
        callback = new HandlerCallback();
        foregroundHandler = new Handler(Looper.getMainLooper(), callback);
    }

    @Override
    protected void onLooperPrepared() {
        Log.d(TAG, "onLooperPrepared: ");
        backgroundHandler = new Handler(getLooper(), callback);
    }

    @Override
    public boolean quitSafely() {
        Log.d(TAG, "quitSafely: ");
        return super.quitSafely();
    }

    MutableLiveData<Integer> getCounter() {
        return counter;
    }

    void resetCount() {
        counter.setValue(0);
        okToContinue = true;
    }

    void startCounter() {
        counter.setValue(0);
        startCounterInBackground();
    }

    private void startCounterInBackground() {
        if (backgroundHandler != null) {
            Log.d(TAG, "startCounter: starting because looper ready in the background thread");
            backgroundHandler.obtainMessage(START).sendToTarget();
        } else {
            Log.d(TAG, "startCounter: iterating because looper not ready in the background thread");
            foregroundHandler.postDelayed(this::startCounterInBackground, 50);
        }
    }

    void stopCounter() {
        okToContinue = false;
    }

    void restartCounter() {
        okToContinue = true;
        startCounterInBackground();
    }


    class HandlerCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case START:
                    Log.d(TAG, "handleMessage: " + message.what);
                    runLoop();
                    break;

                case FINISHED:
                    Log.d(TAG, "handleMessage: " + message.what);
                    break;
            }

            return true;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void runLoop() {
        Log.d(TAG, "handleMessage: running loop");
        for (int i = counter.getValue(); i <= MainActivity.MAX; i++) {
            if (!okToContinue) break;
            SystemClock.sleep(100);
            Log.d(TAG, "runLoop: sleep done: i is " + i + "  counter is " + counter.getValue());
            counter.postValue(i);
        }
        foregroundHandler.obtainMessage(FINISHED).sendToTarget();
    }
}