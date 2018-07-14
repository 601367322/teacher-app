package com.prance.third.impl

import android.app.Application
import android.content.Context
import android.content.Intent
import com.prance.lib.test.setting.features.TestSettingActivity

import com.prance.lib.test.setting.platform.TestSettingActivityLifeManager
import com.prance.lib.third.inter.ITestSetting

class TestSettingImpl : ITestSetting {

    override val testSettingActivityLifeManager: Application.ActivityLifecycleCallbacks
        get() = TestSettingActivityLifeManager()

    override fun callingTestIntent(context: Context): Intent {
        return Intent(context, TestSettingActivity::class.java)
    }
}
