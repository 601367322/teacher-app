package com.prance.third.impl

import android.app.Application

import com.prance.lib.test.setting.platform.TestSettingActivityLifeManager
import com.prance.lib.third.inter.ITestSetting

class TestSettingImpl : ITestSetting {

    override val testSettingActivityLifeManager: Application.ActivityLifecycleCallbacks
        get() = TestSettingActivityLifeManager()
}
