package com.prance.lib.third.inter

import android.app.Application
import android.content.Context
import android.content.Intent

interface ITestSetting {

    val testSettingActivityLifeManager: Application.ActivityLifecycleCallbacks

    fun callingTestIntent(context: Context): Intent
}
