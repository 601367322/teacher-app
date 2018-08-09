package com.prance.lib.teacher.base.core.di

import com.prance.lib.sunvote.service.SunVoteService

interface ISunVoteService {

    var mSunVoteService: SunVoteService.SunVoteServiceBinder?

    var mSunVoteServiceConnection: SunVoteServiceConnection

    fun onSunVoteServiceConnected() {}
}