package com.prance.lib.sunvote.platform

import android.content.Context
import android.hardware.usb.UsbDevice
import java.util.HashMap

interface IUsbManagerInterface {

    fun closeUsb()

    fun getDeviceList(): HashMap<String, UsbDevice>?

    fun sendDataBulkTransfer(sendData: ByteArray)

    fun receiveUsbData(): ByteArray?

    fun checkUsbDevice(context: Context?)

    fun getUsbDevice(): UsbDevice?
}