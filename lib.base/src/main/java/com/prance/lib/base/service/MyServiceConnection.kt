package com.prance.lib.base.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

class MyServiceConnection<T : IBinder>(private val mServicePresenterInterface: BaseServicePresenter<T>) : ServiceConnection {

    override fun onServiceDisconnected(name: ComponentName?) {
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mServicePresenterInterface.mService = service as T
        mServicePresenterInterface.onServiceConnected()
    }
}