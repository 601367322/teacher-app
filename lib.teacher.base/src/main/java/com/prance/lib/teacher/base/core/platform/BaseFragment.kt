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
import android.os.Bundle
import android.support.v4.app.Fragment
import cn.sunars.sdk.SunARS
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.sunvote.service.SunVoteService
import com.prance.lib.teacher.base.TeacherApplication
import com.prance.lib.teacher.base.core.di.IServiceBinder
import com.prance.lib.teacher.base.core.di.MyServiceConnection

/**
 * Base Fragment class with helper methods for handling views and back button events.
 *
 * @see Fragment
 */
abstract class BaseFragment : BaseFragment(), SunARS.SunARSListener, IServiceBinder {

    val application by lazy(mode = LazyThreadSafetyMode.NONE) {
        (context?.applicationContext as TeacherApplication)
    }

    override var mSunVoteService: SunVoteService? = null

    override var mServiceConnection: MyServiceConnection = MyServiceConnection(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //绑定Service
        activity?.run {
            startService(SunVoteService.callingIntent(this))
            bindService(SunVoteService.callingIntent(this), mServiceConnection, Service.BIND_AUTO_CREATE)
        }

        SunARS.addListener(this)
    }

    override fun onConnectEventCallBack(iBaseID: Int, iMode: Int, sInfo: String?) {
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

    override fun onKeyEventCallBack(KeyID: String?, iMode: Int, Time: Float, sInfo: String?) {
    }

    override fun onStaEventCallBack(sInfo: String?) {
    }

    override fun onLogEventCallBack(sInfo: String?) {
    }

    override fun onDataTxEventCallBack(sendData: ByteArray?, dataLen: Int) {
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
    }

    override fun onDestroy() {
        super.onDestroy()

        SunARS.removeListener(this)

        activity?.run {
            unbindService(mServiceConnection)
        }
    }
}
