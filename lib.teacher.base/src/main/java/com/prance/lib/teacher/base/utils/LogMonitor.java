package com.prance.lib.teacher.base.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class LogMonitor {

    private static LogMonitor sInstance = new LogMonitor();
    private HandlerThread mLogThread = new HandlerThread("log");
    private Handler mIoHandler;
    private static final long TIME_BLOCK = 1000L;
    private final int IO_WHAT = 1;

    private LogMonitor() {
        mLogThread.start();
        mIoHandler = new Handler(mLogThread.getLooper()) {

            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                switch (msg.what) {
                    case IO_WHAT:
                        StringBuilder sb = new StringBuilder();
                        StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
                        for (StackTraceElement s : stackTrace) {
                            sb.append(s.toString() + "\n");
                        }
                        Log.e("TAG", sb.toString());
                        break;
                }
            }

        };
    }


    public static LogMonitor getInstance() {
        return sInstance;
    }

    public boolean isMonitor() {
        return mIoHandler.hasMessages(IO_WHAT);
    }

    public void startMonitor() {
        mIoHandler.sendEmptyMessageDelayed(IO_WHAT, TIME_BLOCK);
    }

    public void removeMonitor() {
        mIoHandler.removeMessages(IO_WHAT);
    }

}