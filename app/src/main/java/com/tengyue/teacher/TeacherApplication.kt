package com.tengyue.teacher

import android.app.Application
import android.os.Bundle
import com.blankj.utilcode.util.Utils
import com.fernandocejas.sample.core.di.ApplicationComponent
import com.fernandocejas.sample.core.di.DaggerApplicationComponent
import com.tengyue.teacher.core.di.ApplicationModule

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
