package com.spark.teaching.answertool.usb.helper;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;

import com.blankj.utilcode.util.LogUtils;
import com.spark.teaching.answertool.usb.model.DataPackage;
import com.spark.teaching.answertool.usb.util.BufferUtils;
import com.spark.teaching.answertool.util.KLog;

import java.nio.ByteBuffer;

/**
 * usb通信
 */

public class CommunicateHelper {
    private static final String TAG = "CommunicateHelper";

    //在访问CommunicateHelper时创建单例
    private static class SingletonHolder {
        private static final CommunicateHelper INSTANCE = new CommunicateHelper();
    }

    //获取单例
    public static CommunicateHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final int TIME_OUT_READ = 4000 * 1000; // 读超时时间
    public static final int TIME_OUT_WRITE_RESPONSE = 2000; // 写超时时间，用于收到接收器指令时的响应
    public static final int TIME_OUT_WRITE_SEND = 4000; // 写超时时间，用于普通发送指令给接收器

    private Object mSynchronizedReadObj = new Object();
    private Object mSynchronizedWriteObj = new Object();

    private UsbDeviceConnection mUsbDeviceConnection;
    private UsbEndpoint mInUsbEndpoint, mOutUsbEndpoint;

    private UsbListener mUsbListener;

    private boolean mIsReceiveData; // 是否需要读取数据的标识
    private SendDispatcher mSendDispatcher;
    private DecodeDispatcher mDecodeDispatcher;

    // TODO: 2018/4/10 测试下满255变1的情况
    private byte mSeq = 0; // 发送指令的序列号，每次自增1，满255变1

    public void setData(UsbDeviceConnection usbDeviceConnection, UsbEndpoint inUsbEndpoint, UsbEndpoint outUsbEndpoint, UsbListener listener) {
        mUsbDeviceConnection = usbDeviceConnection;
        mInUsbEndpoint = inUsbEndpoint;
        mOutUsbEndpoint = outUsbEndpoint;
        mUsbListener = listener;
    }

    public void stop() {
        mIsReceiveData = false;
        mUsbDeviceConnection = null;
        mInUsbEndpoint = null;
        mOutUsbEndpoint = null;

        stopSendDispatcher();
        stopDecodeDispatcher();
    }

    public void stopSendDispatcher() {
        if (mSendDispatcher != null) {
            mSendDispatcher.shutdown();
            mSendDispatcher = null;
        }
    }

    public void stopDecodeDispatcher() {
        if (mDecodeDispatcher != null) {
            mDecodeDispatcher.shutdown();
            mDecodeDispatcher = null;
        }
    }

    public void start() {
        stopSendDispatcher();
        stopDecodeDispatcher();
        mSendDispatcher = new SendDispatcher(this);
        mSendDispatcher.startThread();
        mDecodeDispatcher = new DecodeDispatcher(this);
        mDecodeDispatcher.startThread();

        if (mIsReceiveData) { // 已经start了
            return;
        }

        mIsReceiveData = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mIsReceiveData) {
                    if (null == mUsbDeviceConnection || null == mInUsbEndpoint || null == mOutUsbEndpoint || null == mDecodeDispatcher) {
                        continue;
                    }
                    try {
                        ByteBuffer data = read(64);
                        if (null == data) {
                            continue;
                        }
                        if (null == mDecodeDispatcher) {
                            continue;
                        }
                        mDecodeDispatcher.decodeLater((data));
                    } catch (Throwable e) {
                        KLog.throwable(e);
                    }
                }
            }
        }).start();
    }

    private ByteBuffer read(int size) {
        synchronized (mSynchronizedReadObj) {
            byte[] receives = new byte[size];
            int length = mUsbDeviceConnection.bulkTransfer(mInUsbEndpoint, receives, receives.length, TIME_OUT_READ);

            if (length > 0) {
//                LogUtils.i( "read length " + length);

                ByteBuffer buffer = ByteBuffer.wrap(receives, 0, length);
                return buffer;
            } else {
                return null;
            }
        }
    }

    public void write(byte[] data, int timeOut) {
//        LogUtils.i( "write " + System.currentTimeMillis());
        synchronized (mSynchronizedWriteObj) {
            ByteBuffer buffer = BufferUtils.allocateByteBuffer(data.length);
            buffer.put(data);
            int length = mUsbDeviceConnection.bulkTransfer(mOutUsbEndpoint, data, data.length, timeOut);
//            LogUtils.i( "write length " + System.currentTimeMillis() + " " + length);
        }
    }

    public void sendAsync(DataPackage dataPackage) {
        if (null == mUsbDeviceConnection || null == mInUsbEndpoint || null == mOutUsbEndpoint || null == mSendDispatcher) {
            LogUtils.i( "send before connected");
            return;
        }

        dataPackage.setSeq(++mSeq); // TODO: 2018/4/11 需要测试满255的情况
        mSendDispatcher.sendLater(dataPackage);
    }

    public UsbListener getUsbListener() {
        return mUsbListener;
    }
}
