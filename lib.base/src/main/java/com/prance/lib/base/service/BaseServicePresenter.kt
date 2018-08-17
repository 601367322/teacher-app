package com.prance.lib.base.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

abstract class BaseServicePresenter<T : IBinder> {

    var context: Context

    abstract var mService: T?

    var mServiceConnection: MyServiceConnection<T> = MyServiceConnection(this)

    constructor(context: Context) {
        this.context = context
    }

    abstract var intent: Intent

    abstract fun onServiceConnected()

    open fun bind() {
        context.bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE)
    }

    open fun unBind() {
        try {
            context.unbindService(mServiceConnection)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}