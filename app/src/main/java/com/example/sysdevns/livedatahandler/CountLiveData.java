package com.example.sysdevns.livedatahandler;

import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.util.Log;

public class CountLiveData extends MutableLiveData<Integer> {

    private static final String TAG = "CountLiveData";

    private Handler handler;

    public CountLiveData() {
        Log.d(TAG, "CountLiveData: constructor");
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive: ");
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive: ");
    }
}
