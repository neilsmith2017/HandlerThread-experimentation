package com.example.sysdevns.livedatahandler;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

public class MainViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = "MainViewModel";

    private final BackgroundHandlerThread backgroundHandlerThread;

    private LiveData<Integer> counter;
    private MutableLiveData<Boolean> startButtonEnabled;
    private MutableLiveData<Boolean> resetButtonEnabled;
    private MutableLiveData<Boolean> stopButtonEnabled;
    private MutableLiveData<Boolean> startNotRestart;

    private boolean stopped = false;

    public MainViewModel() {
        Log.d(TAG, "MainViewModel: constructor");
        backgroundHandlerThread = new BackgroundHandlerThread("Background HandleThread");
    }

    MutableLiveData<Boolean> getStartButtonEnabled() {
        if (startButtonEnabled == null) {
            startButtonEnabled = new MutableLiveData<>();
            startButtonEnabled.setValue(true);
        }
        return startButtonEnabled;
    }

    MutableLiveData<Boolean> getResetButtonEnabled() {
        if (resetButtonEnabled == null) {
            resetButtonEnabled = new MutableLiveData<>();
            resetButtonEnabled.setValue(false);
        }
        return resetButtonEnabled;
    }

    MutableLiveData<Boolean> getStopButtonEnabled() {
        if (stopButtonEnabled == null) {
            stopButtonEnabled = new MutableLiveData<>();
            stopButtonEnabled.setValue(false);
        }
        return stopButtonEnabled;
    }

    MutableLiveData<Boolean> getStartNotRestart() {
        if (startNotRestart == null) {
            startNotRestart = new MutableLiveData<>();
            startNotRestart.setValue(true);
        }
        return startNotRestart;
    }

    LiveData<Integer> getCounter() {
        if (counter == null) {
            counter = backgroundHandlerThread.getCounter();
        }
        return Transformations.map(counter, input -> {
            if (input == 100) {
                onCounterFinished();
            }
            return input;
        });
    }

    private void onCounterFinished() {
        Log.d(TAG, "onCounterFinished: ");
        stopButtonEnabled.setValue(false);
        resetButtonEnabled.setValue(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
        backgroundHandlerThread.quitSafely();
    }

    void startCounter() {
        if (backgroundHandlerThread.getState() == Thread.State.NEW) {
            Log.d(TAG, "startCounter: starting thread...");
            backgroundHandlerThread.start();
        }
        if (stopped) {
            restartCounter();
        } else {
            backgroundHandlerThread.startCounter();
            startButtonEnabled.setValue(false);
            stopButtonEnabled.setValue(true);
        }
    }

    void resetCounter() {
        backgroundHandlerThread.resetCount();
        stopped = false;
        startNotRestart.setValue(true);
        startButtonEnabled.setValue(true);
        stopButtonEnabled.setValue(false);
        resetButtonEnabled.setValue(false);
    }

    void stopCounter() {
        backgroundHandlerThread.stopCounter();
        stopped = true;
        startNotRestart.setValue(false);
        startButtonEnabled.setValue(true);
        stopButtonEnabled.setValue(false);
        resetButtonEnabled.setValue(true);
    }

    private void restartCounter() {
        backgroundHandlerThread.restartCounter();
        startButtonEnabled.setValue(false);
        stopButtonEnabled.setValue(true);
        resetButtonEnabled.setValue(false);
        stopped = false;
    }
}