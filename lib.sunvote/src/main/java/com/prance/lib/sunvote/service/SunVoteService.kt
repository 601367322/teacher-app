package com.prance.lib.sunvote.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.os.Binder
import android.os.IBinder
import android.os.UserManager
import cn.sunars.sdk.SunARS
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.sunvote.platform.*

class SunVoteService : Service() {

    private lateinit var mUsbThread: DataReceiverThread
    private lateinit var mUsbReceiver: UsbReceiver
    private lateinit var mSunARSListener: DefaultSunARSListener

    private var mUsbManagerImpl: UsbManagerImpl = UsbManagerImpl()

    //用于和外界交互
    private val binder = SunVoteServiceBinder()

    inner class SunVoteServiceBinder : Binder() {

        fun getUserManager(): IUsbManagerInterface {
            return this@SunVoteService.getUserManager()
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        //注册设备连接广播
        mUsbReceiver = UsbReceiver(mUsbManagerImpl)
        registerUsbBroadcastReceiver()

        //开启Usb读取线程
        mUsbThread = DataReceiverThread(mUsbManagerImpl)
        mUsbThread.start()

        //开启基站监听
        try {
            mSunARSListener = DefaultSunARSListener(mUsbManagerImpl)
            SunARS.addListener(mSunARSListener)
            val r = SunARS.license(1, "SUNARS2013")
            SunARS.setLogOn(0)
            val filePath = applicationContext?.filesDir
            SunARS.setArchiveDir(filePath.toString())
        } catch (e: Throwable) {
            LogUtils.d(e.message)
            LogUtils.d("loadLibrary Error")
        }

        //链接USB
        mUsbManagerImpl.checkUsbDevice(this)
    }

    private fun registerUsbBroadcastReceiver() {
        val filter = IntentFilter()
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        filter.addAction(UsbManagerImpl.ACTION_USB_PERMISSION)
        registerReceiver(mUsbReceiver, filter)
    }

    fun getUserManager(): IUsbManagerInterface {
        return mUsbManagerImpl
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        //关闭Usb读取线程
        mUsbThread.let {
            it.interrupt()
        }

        unregisterReceiver(mUsbReceiver)

        SunARS.clearListener()
    }

    companion object {

        fun callingIntent(context: Context): Intent = Intent(context, SunVoteService::class.java)
    }
}

