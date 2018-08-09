package com.prance.lib.socket

import android.os.IBinder

interface IService<T : IBinder> {

    var mService: T?

    var mServiceConnection: MyServiceConnection<T>

    fun onServiceConnected() {}
}