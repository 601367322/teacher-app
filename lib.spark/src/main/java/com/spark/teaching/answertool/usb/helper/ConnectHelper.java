package com.spark.teaching.answertool.usb.helper;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.spark.teaching.answertool.util.KLog;

import java.util.HashMap;

/**
 * usb连接处理
 */

public class ConnectHelper {
    private static final String TAG = "ConnectHelper";

    //在访问ConnectHelper时创建单例
    private static class SingletonHolder {
        private static final ConnectHelper INSTANCE = new ConnectHelper();
    }

    //获取单例
    public static ConnectHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final int VendorID = 1155;//3725
    private static final int ProductID = 23115;//30368
    private static final int VendorID2 = 3725;//3725
    private static final int ProductID2 = 30368;//30368
    private static final String ACTION_USB_PERMISSION = "com.spark.teaching.answertool.USB_PERMISSION"; // 请求usb权限的action

    private UsbListener mUsbListener;

    private UsbReceiver mUsbReceiver;
    private UsbManager mUsbManager;
    private UsbDevice mUsbDevice; // mUsbDevice不为空不一定是已经连接成功了，有可能还没授权usb权限
    private UsbInterface mUsbInterface;
    private UsbDeviceConnection mUsbDeviceConnection;
    private UsbEndpoint mInUsbEndpoint, mOutUsbEndpoint;

    private class UsbReceiver extends BroadcastReceiver { // TODO: 2018/4/10 在清单文件注册广播接收器？
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtils.i("onReceive " + action, true);

            if (action.equals(ACTION_USB_PERMISSION)) { // 请求usb权限
                synchronized (this) {
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    boolean usbPremission = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false);
                    if (usbPremission && usbDevice != null) {
                        mUsbDevice = usbDevice;
                        afterUsbPermission();
                    } else {
                        LogUtils.i("usbPremission:" + usbPremission + "  " + usbDevice);
                        getUsbPermission(context);
                    }
                }
            } else if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) { // usb插入
                UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (usbDevice != null && usbDevice.getVendorId() == VendorID && usbDevice.getProductId() == ProductID) {
                    mUsbDevice = usbDevice;
                    if (mUsbManager.hasPermission(usbDevice)) {
                        afterUsbPermission();
                    } else {
                        LogUtils.i("attached usb no permission", false);
                        getUsbPermission(context);
                    }
                } else {
                    LogUtils.i("not wanted usb attached");
                }
            } else if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (usbDevice != null && usbDevice.getVendorId() == VendorID && usbDevice.getProductId() == ProductID) {
                    closeUsbDevice();
                    if (mUsbListener != null) {
                        mUsbListener.onDisConnected();
                    }
                } else {
                    LogUtils.i("not wanted usb detached");
                }
            }
        }
    }

    public void onServiceCreate(Context context, UsbListener usbListener) {
        mUsbListener = usbListener;

        // 1、注册usb的广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mUsbReceiver = new UsbReceiver();
        context.registerReceiver(mUsbReceiver, filter);

        // 2、枚举设备
        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        if (deviceList.isEmpty()) {
            LogUtils.i("device list empty");
            return;
        } else {
            for (UsbDevice device : deviceList.values()) {
                if (device.getVendorId() == VendorID && device.getProductId() == ProductID) {
                    mUsbDevice = device;
                    if (mUsbManager.hasPermission(device)) {
                        afterUsbPermission();
                    } else {
                        LogUtils.i("no usb permission");
                        getUsbPermission(context);
                    }
                    return;
                }
            }
            LogUtils.i("device not found");
            return;
        }
    }

    public void onServiceDestroy(Context context) {
        context.unregisterReceiver(mUsbReceiver);

        closeUsbDevice();
    }

    private void getUsbPermission(Context context) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        mUsbManager.requestPermission(mUsbDevice, pendingIntent);
    }

    private void afterUsbPermission() {
        try {
            int interfaceCount = mUsbDevice.getInterfaceCount(); // 获取设备接口，一般都是一个接口，在这个接口上有两个端点，OUT 和 IN
            LogUtils.i("interfaceCount:" + interfaceCount);
            if (interfaceCount <= 0) {
                throw new RuntimeException("interfaceCount <= 0");
            }
            mUsbInterface = mUsbDevice.getInterface(0);
            if (null == mUsbInterface) {
                throw new RuntimeException("usbInterface null");
            }

            UsbDeviceConnection usbDeviceConnection = mUsbManager.openDevice(mUsbDevice);
            if (null == usbDeviceConnection) {
                throw new RuntimeException("usbDeviceConnection null");
            }
            if (usbDeviceConnection.claimInterface(mUsbInterface, true)) {
                mUsbDeviceConnection = usbDeviceConnection;
                mInUsbEndpoint = mUsbInterface.getEndpoint(0);
                mOutUsbEndpoint = mUsbInterface.getEndpoint(1);
                afterUsbConnected(mUsbDevice.getSerialNumber());
            } else {
                usbDeviceConnection.close();
                throw new RuntimeException("claimInterface failed");
            }
        } catch (Throwable e) {
            KLog.throwable(e);
        }
    }

    private void closeUsbDevice() {
        mInUsbEndpoint = null;
        mOutUsbEndpoint = null;
        if (mUsbDeviceConnection != null) {
            mUsbDeviceConnection.releaseInterface(mUsbInterface);
            mUsbDeviceConnection.close();
            mUsbDeviceConnection = null;
        }
        mUsbInterface = null;
        mUsbDevice = null;
    }

    private void afterUsbConnected(String serialNum) {
        if (mUsbListener != null) {
            mUsbListener.onConnected(mUsbDeviceConnection, mInUsbEndpoint, mOutUsbEndpoint, serialNum);
        }
    }

}
