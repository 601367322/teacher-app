package com.prance.lib.socket

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

class MyServiceConnection<T : IBinder>(private val mServiceInterface: IService<T>) : ServiceConnection {

    override fun onServiceDisconnected(name: ComponentName?) {
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mServiceInterface.mService = service as T
        mServiceInterface.onServiceConnected()
    }
}