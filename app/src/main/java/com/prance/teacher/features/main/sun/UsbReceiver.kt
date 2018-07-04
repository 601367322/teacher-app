package com.prance.teacher.features.main.sun

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import android.hardware.usb.*
import android.os.Handler
import android.os.Message
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import cn.sunars.sdk.SunARS
import java.util.*

class UsbReceiver : BroadcastReceiver(), IUsbManagerInterface {

    private var mUsbManager: UsbManager? = null
    private var mUsbDevice: UsbDevice? = null
    private var lastOpenTime: Long = 0
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

    override fun onReceive(context: Context?, intent: Intent?) {
        mHandler.post {
            intent?.let {
                val action = it.action
                if (UsbManager.ACTION_USB_DEVICE_ATTACHED == action) {
                    /* 连接 */
                    val inputManager = context?.getSystemService(Context.INPUT_SERVICE) as InputManager?

                    val deviceIds = inputManager?.inputDeviceIds// 获取所有的设备id
                    val inputDevice = inputManager?.getInputDevice(deviceIds!![deviceIds.size - 1])

                    LogUtils.d("找到设备：$inputDevice")

                    if (SunARS.checkBaseConnection() == 0) {
                        checkUsbDevice(context)
                    }
                } else if (UsbManager.ACTION_USB_DEVICE_DETACHED == action) {
                    /* 移除 */
                    closeUsb()
                } else if (ACTION_USB_PERMISSION == action) {
                    //授权
                    synchronized(this) {
                        val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                        if (device.deviceName == mUsbDevice?.deviceName) {
                            if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                                // 授权成功,在这里进行打开设备操作
                                LogUtils.d("授权成功")
                                if (System.currentTimeMillis() - lastOpenTime > 1000) {
                                    lastOpenTime = System.currentTimeMillis()
                                    openUsbDevice()
                                }
                            } else {
                                // 授权失败
                                ToastUtils.showLong("授权失败!")
                            }
                        }
                    }
                }
            }
        }
    }

    fun checkUsbDevice(context: Context?) {
        if (mUsbManager == null) {
            mUsbManager = context?.getSystemService(Context.USB_SERVICE) as UsbManager?
        }
        val map = mUsbManager?.deviceList
        map?.let {
            for (device in map.values) {
                LogUtils.d("checkDevice", "找到基站: Vid:" + device.vendorId + "  Pid:" + device.productId)

                if (device.vendorId == VendorID && device.productId == ProductID
                        || device.vendorId == VendorID_2 && device.productId == ProductID_2
                        || device.vendorId == VendorID_3 && device.productId == ProductID_3) {

                    LogUtils.d("找到基站")

                    mUsbDevice = device

                    //申请授权
                    if (!mUsbManager!!.hasPermission(device)) {
                        val intent = Intent(ACTION_USB_PERMISSION)
                        val mPermissionIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
                        mUsbManager?.requestPermission(device, mPermissionIntent)
                    } else {
                        openUsbDevice()
                    }
                }
            }
        }
    }

    /**
     * 打开连接
     *
     * @paramdevice
     */
    private fun openUsbDevice(): Boolean {
        mUsbDevice?.let {
            if (it.interfaceCount == 0) {
                return false
            }
            mUsbInterface = it.getInterface(0)
            setEndpoint(mUsbInterface)

            mUsbConnection = mUsbManager!!.openDevice(it)
            LogUtils.d("打开连接：$mUsbConnection")

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
                    LogUtils.d("handleMessage:usb connect")
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

    override fun recvUsbData(): ByteArray? {
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