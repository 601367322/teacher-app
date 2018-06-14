package cn.sunars.sdk;

import java.util.HashMap;

import android.content.Context;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

public class UsbMangement {
	private UsbManager mUsbManager;
	private UsbDevice mUsbDevice;
	private UsbDeviceConnection mUsbConnection;
	private InputManager inputManager;
	private UsbEndpoint epOut, epIn;
	private UsbInterface mUsbInterface;

	private final int VendorID = 0x03eb;
	private final int ProductID = 0x6201;

	private final int VendorID_2 = 0x0d8c;
	private final int ProductID_2 = 0xEA10;

	private boolean device_null = true;

	public void searchUsbDevice(Context context) {
		if (mUsbManager == null) {
			mUsbManager = (UsbManager) context.getSystemService(context.USB_SERVICE);
		}
		HashMap<String, UsbDevice> map = mUsbManager.getDeviceList();
		for (UsbDevice device : map.values()) {
			if (device.getVendorId() == VendorID  && device.getProductId() == ProductID) {
				System.out.println("找到，进来了");
				//setDevice(device);
				device_null = false;
			}
		}
//		readThread = new ReadThread();
//		readThread.start();
	}
}
