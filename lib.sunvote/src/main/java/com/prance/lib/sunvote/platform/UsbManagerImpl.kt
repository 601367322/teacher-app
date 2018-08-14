package com.prance.lib.sunvote.platform

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.*
import android.os.Handler
import android.os.Message
import cn.sunars.sdk.SunARS
import com.blankj.utilcode.util.LogUtils
import java.util.*


class UsbManagerImpl : IUsbManagerInterface {

    private var mUsbManager: UsbManager? = null
    private var mUsbDevice: UsbDevice? = null
    private var epOut: UsbEndpoint? = null
    private var epIn: UsbEndpoint? = null
    private var mUsbInterface: UsbInterface? = null
    private var mUsbConnection: UsbDeviceConnection? = null
    private val MSG_USB_CONNECTED = 2

    private val VendorID = 0x03eb
    private val ProductID = 0x6201

    private val VendorID_2 = 0x0d8c
    private val ProductID_2 = 0xEA10

    private val VendorID_3 = 0x2F70
    private val ProductID_3 = 0xEA10

    private val recvBuffer = ByteArray(64)

    override fun checkUsbDevice(context: Context?) {
        mUsbManager = context?.getSystemService(Context.USB_SERVICE) as UsbManager?
        val map = mUsbManager?.deviceList
        map?.let {
            var baseStation: UsbDevice? = null
            for (device in map.values) {
//                LogUtils.d("checkDevice", "找到基站: Vid:" + device.vendorId + "  Pid:" + device.productId)

                if (device.vendorId == VendorID && device.productId == ProductID
                        || device.vendorId == VendorID_2 && device.productId == ProductID_2
                        || device.vendorId == VendorID_3 && device.productId == ProductID_3) {

                    baseStation = device

                    LogUtils.d("找到基站\t\n"
                            + baseStation?.deviceId + "\n"
                            + baseStation?.deviceName + "\n"
                            + baseStation?.deviceClass + "\n"
                            + baseStation?.deviceProtocol + "\n"
                            + baseStation?.deviceSubclass + "\n"
                            + baseStation?.productId + "\n"
                            + baseStation?.productName + "\n"
                            + baseStation?.manufacturerName + "\n"
                            + baseStation?.vendorId + "\n"
                            + baseStation?.serialNumber + "\n"
                    )

                    //申请授权
                    if (!mUsbManager!!.hasPermission(device)) {
                        val intent = Intent(ACTION_USB_PERMISSION)
                        val mPermissionIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
                        mUsbManager?.requestPermission(device, mPermissionIntent)
                    } else {
                        openUsbDevice()
                    }

                    break
                }
            }

            if (baseStation == null) {
                closeUsb()
            }
            mUsbDevice = baseStation
        }
    }

    override fun getUsbDevice(): UsbDevice? {
        return mUsbDevice
    }

    /**
     * 打开连接
     *
     * @paramdevice
     */
    override fun openUsbDevice(): Boolean {
        mUsbDevice?.let {
            if (it.interfaceCount == 0) {
                return false
            }
            mUsbInterface = it.getInterface(0)
            setEndpoint(mUsbInterface)

            mUsbConnection = mUsbManager!!.openDevice(it)

            mUsbConnection?.let {
                val ret = it.claimInterface(mUsbInterface, true)
                LogUtils.d("打开设备")

                val message = Message()
                message.what = MSG_USB_CONNECTED
                mHandler.sendMessageDelayed(message, 500)

                return ret
            }
        }
        return false
    }

    /**
     * UsbInterface 进行端点设置和通讯
     *
     * @param usbInterface
     */
    private fun setEndpoint(usbInterface: UsbInterface?) {
        if (usbInterface == null)
            return
        // 设置接收数据的端点
        if (usbInterface.getEndpoint(0) != null) {
            epIn = usbInterface.getEndpoint(0)
        }
        // 当端点为2的时候
        if (usbInterface.endpointCount == 2) {
            // 设置发送数据的断点
            if (usbInterface.getEndpoint(1) != null)
                epOut = usbInterface.getEndpoint(1)
        }
    }

    private var mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_USB_CONNECTED -> {
                    SunARS.connect(5, "usb")
                }
                else -> {
                }
            }
            super.handleMessage(msg)
        }
    }

    companion object {
        const val ACTION_USB_PERMISSION = "com.hhd.USB_PERMISSION"
    }

    override fun getDeviceList(): HashMap<String, UsbDevice>? {
        return mUsbManager?.deviceList
    }

    override fun sendDataBulkTransfer(sendData: ByteArray) {
        mUsbDevice?.let {
            mUsbManager?.let {
                if (it.hasPermission(mUsbDevice)) {
                    send(sendData)
                }
            }
        }
    }

    private fun send(buffer: ByteArray): Int {
        var ref = -100
        mUsbConnection?.let {
            val length = buffer.size
            if (epOut != null) {
                ref = it.bulkTransfer(epOut, buffer, length, 1000)
                it.claimInterface(mUsbInterface, true)
            }
//            LogUtils.d("发送数据:len" + ref + ",Data: " + getDataBufString(buffer))
            return ref
        }
        return ref
    }

    private fun getDataBufString(buf: ByteArray): String {
        var tmpStr = String()
        for (i in buf.indices) {
            tmpStr += String.format("%x ", buf[i])
        }
        return tmpStr
    }

    override fun receiveUsbData(): ByteArray? {
        if (mUsbConnection == null || epIn == null) {
            // addLog(" mUsbConnection or epIn is null");
            return null
        }

        Arrays.fill(recvBuffer, 0x0.toByte())
        val ret = mUsbConnection?.bulkTransfer(epIn, recvBuffer, 64, 3000)
//        LogUtils.d("ret", "ret:$ret")

        return recvBuffer
    }


    override fun closeUsb() {
        mUsbConnection?.let {
            synchronized(mUsbConnection!!) {
                it.releaseInterface(mUsbInterface)
                it.close()
                mUsbConnection = null
                epOut = null
                epIn = null
                mUsbInterface = null
                mUsbDevice = null
            }
        }
    }
}
