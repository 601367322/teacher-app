package com.prance.teacher

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.shuangshi.lib.base.core.di.ApplicationComponent
import com.shuangshi.lib.base.core.di.ApplicationModule
import com.shuangshi.lib.base.core.di.DaggerApplicationComponent


class TeacherApplication : Application(){

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }


    override fun onCreate() {
        super.onCreate()

        Utils.init(this)
    }
}
