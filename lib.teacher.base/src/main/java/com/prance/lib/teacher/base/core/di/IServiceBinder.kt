package com.prance.lib.teacher.base.core.di

import com.prance.lib.sunvote.service.SunVoteService

interface IServiceBinder {

    var mSunVoteService: SunVoteService?

    var mServiceConnection: MyServiceConnection

    fun onServiceConnected() {}
}