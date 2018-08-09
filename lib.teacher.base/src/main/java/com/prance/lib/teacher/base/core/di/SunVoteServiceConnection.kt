package com.prance.lib.teacher.base.core.di

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.prance.lib.sunvote.service.SunVoteService

class SunVoteServiceConnection(private val sunVoteServiceInterface: ISunVoteService) : ServiceConnection {

    override fun onServiceDisconnected(name: ComponentName?) {
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val myBinder = service as SunVoteService.SunVoteServiceBinder
        sunVoteServiceInterface.mSunVoteService = myBinder
        sunVoteServiceInterface.onSunVoteServiceConnected()
    }
}