package com.prance.teacher.features.main.sun

import android.hardware.usb.UsbDevice
import java.util.HashMap

interface IUsbManagerInterface {

    fun closeUsb()

    fun getDeviceList(): HashMap<String, UsbDevice>?

    fun sendDataBulkTransfer(sendData: ByteArray)

    fun recvUsbData(): ByteArray?
}