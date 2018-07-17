package com.prance.lib.teacher.base.core.di

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.prance.lib.sunvote.service.SunVoteService

class MyServiceConnection(private val serviceBinder: IServiceBinder) : ServiceConnection {

    override fun onServiceDisconnected(name: ComponentName?) {
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val myBinder = service as SunVoteService.MyBinder
        serviceBinder.mSunVoteService = myBinder.service
        serviceBinder.onServiceConnected()
    }
}