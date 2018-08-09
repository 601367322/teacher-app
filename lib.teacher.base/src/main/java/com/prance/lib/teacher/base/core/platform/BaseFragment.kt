/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.prance.lib.teacher.base.core.platform

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.support.v4.app.Fragment
import cn.sunars.sdk.SunARS
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.socket.IService
import com.prance.lib.socket.MessageListener
import com.prance.lib.socket.PushService
import com.prance.lib.socket.MyServiceConnection
import com.prance.lib.sunvote.service.SunVoteService
import com.prance.lib.teacher.base.TeacherApplication
import com.prance.lib.teacher.base.core.di.ISunVoteService
import com.prance.lib.teacher.base.core.di.SunVoteServiceConnection

/**
 * Base Fragment class with helper methods for handling views and back button events.
 *
 * @see Fragment
 */
abstract class BaseFragment : BaseFragment(), SunARS.SunARSListener, ISunVoteService {//, IService<PushService.PushServiceBinder>, MessageListener

    val application by lazy(mode = LazyThreadSafetyMode.NONE) {
        (context?.applicationContext as TeacherApplication)
    }

    override var mSunVoteService: SunVoteService.SunVoteServiceBinder? = null

//    override var mService: PushService.PushServiceBinder? = null
//
//    override var mServiceConnection = MyServiceConnection(this)

    override var mSunVoteServiceConnection: SunVoteServiceConnection = SunVoteServiceConnection(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (needSunVoteService()) {
            //绑定Service
            activity?.run {
                startService(SunVoteService.callingIntent(this))
                bindService(SunVoteService.callingIntent(this), mSunVoteServiceConnection, Service.BIND_AUTO_CREATE)
            }

            SunARS.addListener(this)
        }

//        if (needPushService()) {
//            activity?.run {
//                startService(PushService.callingIntent(this))
//                bindService(PushService.callingIntent(this), mServiceConnection, Service.BIND_AUTO_CREATE)
//            }
//        }
    }

    override fun onConnectEventCallBack(iBaseID: Int, iMode: Int, sInfo: String?) {
        if (sInfo == SunARS.BaseStation_Connected) {
            //设置基站的唯一SN编码
            mSunVoteService?.let {
                application.mBaseStation.sn = it.getUserManager().getUsbDevice()?.serialNumber
            }

        }
    }

    override fun onHDParamCallBack(iBaseID: Int, iMode: Int, sInfo: String?) {
        when (iMode) {
        //基站主信道
            SunARS.BaseStation_Channel -> {

            }
        }
    }

    override fun onHDParamBySnCallBack(KeySn: String?, iMode: Int, sInfo: String?) {
    }

    override fun onVoteEventCallBack(iBaseID: Int, iMode: Int, sInfo: String?) {
    }

    override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
    }

    override fun onStaEventCallBack(sInfo: String?) {
    }

    override fun onLogEventCallBack(sInfo: String?) {
    }

    override fun onDataTxEventCallBack(sendData: ByteArray?, dataLen: Int) {
    }

    open fun needSunVoteService(): Boolean = false

//    open fun needPushService(): Boolean = false

    override fun onDestroy() {
        super.onDestroy()

        if (needSunVoteService()) {
            SunARS.removeListener(this)

            activity?.run {
                unbindService(mSunVoteServiceConnection)
            }
        }

//        if (needPushService()) {
//            activity?.run {
//                unbindService(mServiceConnection)
//            }
//        }
    }

//    var mServiceList = mutableListOf<Class<T>>()
//
//    fun <T : Service> initServices(): MutableList<Class<T>> {
//        val list = mutableListOf<Class<T>>()
//        list.add(PushService::class.java as Class<T>)
//        return list
//    }
//
//    fun bindService() {
//
//        if (mServiceList.isEmpty()) {
//            mServiceList = initServices()
//        }
//
//        for (clazz in mServiceList) {
//            val service = clazz.newInstance()
//
//            activity?.run {
//                bindService(Intent(context, clazz), mServiceConnection, Service.BIND_AUTO_CREATE)
//            }
//        }
//
//    }
//
//    fun unBindService() {
//        for (clazz in mServiceList) {
//            activity?.run {
//                unBindService(mServiceConnection)
//            }
//
//        }
//    }
}
