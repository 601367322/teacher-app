package com.spark.teaching.answertool.usb.helper;

import com.spark.teaching.answertool.usb.util.DecodeUtil;
import com.spark.teaching.answertool.util.KLog;

import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * usb接收到的指令解析的任务调度
 */
public class DecodeDispatcher extends Thread {

    private boolean mIsRunning = false;
    private LinkedList<ByteBuffer> mTaskQueue = new LinkedList<>(); // 待解析任务队列

    private CommunicateHelper mCommunicateHelper;

    private Object ticket = new Object();

    public DecodeDispatcher(CommunicateHelper communicateHelper) {
        mCommunicateHelper = communicateHelper;
    }

    /**
     * 同步解析
     *
     * @param byteBuffer
     */
    public void decode(ByteBuffer byteBuffer) {
        if (!mIsRunning || null == mCommunicateHelper) {
            return;
        }

        try {
            DecodeUtil.decode(byteBuffer, mCommunicateHelper.getUsbListener());
        } catch (Throwable e) {
            KLog.throwable(e);
        }
    }

    /**
     * 把任务加到异步队列
     *
     * @param byteBuffer
     */
    public void decodeLater(ByteBuffer byteBuffer) {
        if (!mIsRunning || null == mCommunicateHelper) {
            return;
        }

        synchronized (mTaskQueue) {
            mTaskQueue.add(byteBuffer);
        }
        synchronized (ticket) {
            ticket.notify();
        }
    }

    /**
     * 启动处理线程
     */
    public void startThread() {
        if (null == mCommunicateHelper) {
            return;
        }
        if (mIsRunning) {
            return;
        }
        mIsRunning = true;
        start();
    }

    /**
     * 关闭调度器，同时把所有线程关掉
     */
    public void shutdown() {
        mCommunicateHelper = null;
        if (mIsRunning) {
            mIsRunning = false;
        }
        synchronized (mTaskQueue) {
            mTaskQueue.clear();
        }
        synchronized (ticket) {
            ticket.notify();
        }
    }

    @Override
    public void run() {
        while (mIsRunning) {
            ByteBuffer byteBuffer = poll();
            if (null == byteBuffer) {
                continue;
            }
            decode(byteBuffer);
        }
    }

    /**
     * 从队列里面取出任务
     */
    private ByteBuffer poll() {
        while (mIsRunning) {
            synchronized (mTaskQueue) {
                if (mTaskQueue.size() > 0) {
                    ByteBuffer byteBuffer = mTaskQueue.remove(0);

                    if (null != byteBuffer) {
                        return byteBuffer;
                    }
                }
            }
            synchronized (ticket) {
                try {
                    ticket.wait();
                } catch (InterruptedException ex) {
                }
            }
        }
        return null;
    }
}
