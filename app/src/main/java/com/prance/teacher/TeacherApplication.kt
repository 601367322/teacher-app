package com.prance.teacher

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.shuangshi.lib.base.core.di.ApplicationComponent
import com.shuangshi.lib.base.core.di.ApplicationModule
import com.shuangshi.lib.base.core.di.DaggerApplicationComponent
import com.squareup.leakcanary.LeakCanary


class TeacherApplication : Application(){

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }


    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
        this.initializeLeakDetection()
        Utils.init(this)
    }

    private fun injectMembers() = appComponent.inject(this)

    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) LeakCanary.install(this)
    }
}
