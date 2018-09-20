package com.spark.teaching.answertool.usb.helper;


import com.spark.teaching.answertool.usb.model.DataPackage;
import com.spark.teaching.answertool.usb.model.ReceiveAnswerRep;
import com.spark.teaching.answertool.usb.model.ReportBindCardRep;
import com.spark.teaching.answertool.usb.util.EncodeUtil;
import com.spark.teaching.answertool.util.KLog;

import java.util.LinkedList;

/**
 * usb发送指令的任务调度
 */
public class SendDispatcher extends Thread {

    private boolean mIsRunning = false;
    private LinkedList<DataPackage> mTaskQueue = new LinkedList<>(); // 待发送任务队列

    private CommunicateHelper mCommunicateHelper;


    private Object ticket = new Object();

    public SendDispatcher(CommunicateHelper communicateHelper) {
        mCommunicateHelper = communicateHelper;
    }

    /**
     * 同步发送指令
     *
     * @param dataPackage
     */
    public void send(DataPackage dataPackage) {
        if (!mIsRunning || null == mCommunicateHelper) {
            return;
        }

        try {
            byte[] bytes = EncodeUtil.encode(dataPackage);
            if (dataPackage instanceof ReceiveAnswerRep || dataPackage instanceof ReportBindCardRep) { // 防止超时时间太长阻塞其它指令的发送
                mCommunicateHelper.write(bytes, CommunicateHelper.TIME_OUT_WRITE_RESPONSE);
            } else {
                mCommunicateHelper.write(bytes, CommunicateHelper.TIME_OUT_WRITE_SEND);
            }
        } catch (Throwable e) {
            KLog.throwable(e);
        }
    }

    /**
     * 把任务加到异步队列
     *
     * @param dataPackage
     */
    public void sendLater(DataPackage dataPackage) {
        if (!mIsRunning || null == mCommunicateHelper) {
            return;
        }

        synchronized (mTaskQueue) {
            mTaskQueue.add(dataPackage);
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
            DataPackage dataPackage = poll();
            if (null == dataPackage) {
                continue;
            }
            send(dataPackage);
        }
    }

    /**
     * 从队列里面取出任务
     */
    private DataPackage poll() {
        while (mIsRunning) {
            synchronized (mTaskQueue) {
                if (mTaskQueue.size() > 0) {
                    DataPackage dataPackage = mTaskQueue.remove(0);

                    if (null != dataPackage) {
                        return dataPackage;
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
