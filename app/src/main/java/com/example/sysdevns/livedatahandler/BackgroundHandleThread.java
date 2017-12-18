package com.example.sysdevns.livedatahandler;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

public class BackgroundHandleThread extends HandlerThread {

    private static final String TAG = "BackgroundHandleThread";

    public static final int ACTIVE = 1;
    public static final int INACTIVE = 2;
    public static final int START = 3;
    public static final int TASK_DONE = 4;

    private Handler handler;
    private int counter = 0;

    public BackgroundHandleThread(String name) {
        super(name);
        Log.d(TAG, "BackgroundHandleThread: constructor");
    }

//    public Handler getHandler() {
//        return handler;
//    }

    @Override
    protected void onLooperPrepared() {
        Log.d(TAG, "onLooperPrepared: ");
//
//        handler = new Handler(getLooper()) {
//
//            @Override
//            public void handleMessage(Message msg) {
//                Log.d(TAG, "handleMessage: " + handler.getLooper().getThread().getName());
//                switch (msg.what) {
//                    case START:
//                        Log.d(TAG, "handleMessage: START");
//                        runTask();
//                        break;
//                    case ACTIVE:
//                        Log.d(TAG, "handleMessage: ACTIVE");
//                        runTask();
//                        break;
//                    case INACTIVE:
//                        Log.d(TAG, "handleMessage: INACTIVE");
//                        handler.removeMessages(TASK_DONE);
//                        break;
//                    case TASK_DONE:
//                        Log.d(TAG, "handleMessage: TASK_DONE");
//                        counter++;
//                        runTask();
//                }
//            }
//        };
//        Log.d(TAG, "handleMessage: handler is " + handler);

//        handler.sendEmptyMessage(START);
    }

//    private void runTask() {
//        Log.d(TAG, "runTask: " + counter);
//        Message message = Message.obtain(handler, TASK_DONE);
//        message.arg1 = counter;
//        handler.sendMessageDelayed(message, 3000);
//    }

    @Override
    public boolean quitSafely() {
        Log.d(TAG, "quitSafely: ");
        return super.quitSafely();
    }


}