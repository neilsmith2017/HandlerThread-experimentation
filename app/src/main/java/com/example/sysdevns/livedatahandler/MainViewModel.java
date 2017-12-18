package com.example.sysdevns.livedatahandler;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.sysdevns.livedatahandler.BackgroundHandleThread.TASK_DONE;

public class MainViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = "MainViewModel";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler;

    private CountLiveData counter;

    public MainViewModel() {
        Log.d(TAG, "MainViewModel: constructor");

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                counter.setValue(msg.arg1);
                if (!executorService.isShutdown())
                    executorService.submit(new ViewModelRunnable(msg.arg1, handler));
            }
        };
        counter = new CountLiveData();
        counter.setValue(0);
        executorService.submit(new ViewModelRunnable(counter.getValue(), handler));
    }

    public LiveData<Integer> getCounter() {
        Log.d(TAG, "getCounter: ");
        return counter;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
        executorService.shutdown();
    }

    static class ViewModelRunnable implements Runnable {

        private Handler handler;
        private int currentValue;

        public ViewModelRunnable(int value, Handler handler) {
            currentValue = value;
            this.handler = handler;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = Message.obtain(handler, TASK_DONE);
            message.arg1 = currentValue + 1;
            handler.sendMessage(message);
        }
    }
}
