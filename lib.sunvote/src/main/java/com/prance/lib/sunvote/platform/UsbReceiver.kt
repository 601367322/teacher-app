package com.prance.lib.sunvote.platform

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import android.hardware.usb.*
import android.os.Handler
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.common.utils.ToastUtils
import cn.sunars.sdk.SunARS
import com.prance.lib.sunvote.platform.UsbManagerImpl.Companion.ACTION_USB_PERMISSION

class UsbReceiver(private val mUsbManagerInterface: IUsbManagerInterface) : BroadcastReceiver() {

    private var lastOpenTime: Long = 0

    override fun onReceive(context: Context?, intent: Intent?) {
        Handler().post {
            intent?.let {
                val action = it.action
                if (UsbManager.ACTION_USB_DEVICE_ATTACHED == action) {
                    /* 连接 */
                    val inputManager = context?.getSystemService(Context.INPUT_SERVICE) as InputManager?

                    val deviceIds = inputManager?.inputDeviceIds// 获取所有的设备id
                    val inputDevice = inputManager?.getInputDevice(deviceIds!![deviceIds.size - 1])

                    LogUtils.i("找到设备：$inputDevice")

                    if (SunARS.checkBaseConnection() == 0) {
                        mUsbManagerInterface.checkUsbDevice(context)
                    }
                } else if (UsbManager.ACTION_USB_DEVICE_DETACHED == action) {
                    /* 移除 */
                    mUsbManagerInterface.checkUsbDevice(context)
                } else if (ACTION_USB_PERMISSION == action) {
                    //授权
                    synchronized(this) {
                        val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                        if (device.deviceName == mUsbManagerInterface.getUsbDevice()?.deviceName) {
                            if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                                // 授权成功,在这里进行打开设备操作
                                LogUtils.i("授权成功")
                                if (System.currentTimeMillis() - lastOpenTime > 1000) {
                                    lastOpenTime = System.currentTimeMillis()
                                    mUsbManagerInterface.checkUsbDevice(context)
                                }
                            } else {
                                // 授权失败
                                ToastUtils.showShort("授权失败!")
                                mUsbManagerInterface.checkUsbDevice(context)
                            }
                        }
                    }
                }
            }
        }
    }

}