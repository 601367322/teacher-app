package com.prance.lib.sunvote.service

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface SunVoteServiceComponent {

    fun inject(service: SunVoteService)
}
