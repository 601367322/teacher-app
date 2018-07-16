package com.prance.teacher.core.di

import com.prance.lib.sunvote.service.SunVoteService

interface IServiceBinder {
    var mSunVoteService: SunVoteService?

    var mServiceConnection: MyServiceConnection
}