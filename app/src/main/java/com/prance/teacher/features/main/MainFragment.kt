package com.prance.teacher.features.main

import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.*
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.prance.teacher.R
import com.prance.teacher.core.platform.BaseFragment
import com.prance.teacher.features.main.sun.DataReceiverThread
import com.prance.teacher.features.main.sun.MySunARSListener
import com.prance.teacher.features.main.sun.UsbReceiver
import cn.sunars.sdk.SunARS

class MainFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_main

    private var mUsbThread: DataReceiverThread? = null
    private var mUsbReceiver: UsbReceiver? = null
    private var mSunARSListener: MySunARSListener? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //注册设备连接广播
        mUsbReceiver = UsbReceiver()
        registerUsbBroadcastReceiver()

        //开启Usb读取线程
        mUsbThread = DataReceiverThread(mUsbReceiver)
        mUsbThread?.start()

        //开启基站监听
        try {
            mSunARSListener = MySunARSListener(mUsbReceiver!!)
            SunARS.setListener(mSunARSListener)
            val r = SunARS.license(1, "SUNARS2013")
            SunARS.setLogOn(0)
            val filePath = activity?.applicationContext?.filesDir
            SunARS.setArchiveDir(filePath.toString())
            LogUtils.d("license:$r")
        } catch (e: Throwable) {
            LogUtils.d(e.message)
            LogUtils.d("loadLibrary Error")
        }

        //链接USB
        mUsbReceiver?.checkUsbDevice(activity)
    }

    private fun registerUsbBroadcastReceiver() {
        val filter = IntentFilter()
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        filter.addAction(UsbReceiver.ACTION_USB_PERMISSION)
        activity?.run { registerReceiver(mUsbReceiver, filter) }
    }

    override fun onDestroy() {
        super.onDestroy()

        //关闭Usb读取线程
        mUsbThread?.let {
            it.interrupt()
        }

        activity?.run {
            unregisterReceiver(mUsbReceiver)
            stopService(Intent(this, SunARS::class.java))
        }

        SunARS.clearListener()
    }


}