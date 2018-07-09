package com.prance.teacher.features.main

import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.*
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.prance.teacher.R
import com.prance.lib.teacher.base.core.platform.BaseFragment
import cn.sunars.sdk.SunARS
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.ImageConfig
import com.prance.lib.common.utils.ImageLoaderFactory
import com.prance.teacher.features.main.sun.*
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

class MainFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_main

    private lateinit var mUsbThread: DataReceiverThread
    private lateinit var mUsbReceiver: UsbReceiver
    private lateinit var mSunARSListener: MySunARSListener

//    private lateinit var mMainViewModel: MainViewModel

    @Inject
    lateinit var mUsbManagerImpl: UsbManagerImpl

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        DaggerMainComponent.create().inject(this)

        //注册设备连接广播
        mUsbReceiver = UsbReceiver(mUsbManagerImpl)
        registerUsbBroadcastReceiver()

        //开启Usb读取线程
        mUsbThread = DataReceiverThread(mUsbManagerImpl)
        mUsbThread.start()

        //开启基站监听
        try {
            mSunARSListener = MySunARSListener(mUsbManagerImpl)
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
        mUsbManagerImpl.checkUsbDevice(activity)
    }

    private fun registerUsbBroadcastReceiver() {
        val filter = IntentFilter()
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        filter.addAction(UsbManagerImpl.ACTION_USB_PERMISSION)
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

        ImageLoaderFactory.clearData()
    }


}