package com.prance.teacher.features.main

import com.prance.lib.teacher.base.core.di.ApplicationModule
import com.prance.lib.base.viewmodel.ViewModelModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [MainModule::class, ApplicationModule::class, ViewModelModule::class])
interface MainComponent {

    fun inject(fragment: MainFragment)
}
