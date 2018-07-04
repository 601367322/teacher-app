package com.prance.teacher.features.main;

import android.content.Context;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import java.util.HashMap;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UsbModule {

    private static final int VendorID = 0x03eb;
    private static final int ProductID = 0x6201;

    private static final int VendorID_2 = 0x0d8c;
    private static final int ProductID_2 = 0xEA10;

    private static final int VendorID_3 = 0x2F70;
    private static final int ProductID_3 = 0xEA10;

    private Context context;

    public UsbModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context providesContext() {
        return context;
    }

    @Singleton
    @Provides
    UsbManager provideUsbManager(Context context) {
        return (UsbManager) context.getSystemService(Context.USB_SERVICE);
    }

    @Singleton
    @Provides
    UsbDevice provideUsbDevice(UsbManager mUsbManager) {
        HashMap<String, UsbDevice> map = mUsbManager.getDeviceList();
        UsbDevice mUsbDevice = null;
        for (UsbDevice device : map.values()) {
            if (device.getVendorId() == VendorID && device.getProductId() == ProductID
                    || device.getVendorId() == VendorID_2 && device.getProductId() == ProductID_2
                    || device.getVendorId() == VendorID_3 && device.getProductId() == ProductID_3) {
                mUsbDevice = device;
            }
        }
        return mUsbDevice;
    }

    @Singleton
    @Provides
    UsbDeviceConnection provideUsbDeviceConnection(UsbManager mUsbManager, UsbDevice mUsbDevice) {
        return mUsbManager.openDevice(mUsbDevice);
    }


}
