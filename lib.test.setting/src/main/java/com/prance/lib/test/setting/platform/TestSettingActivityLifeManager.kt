package com.prance.lib.test.setting.platform

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.blankj.utilcode.util.ProcessUtils
import com.prance.lib.common.utils.ModelUtil
import com.prance.lib.test.setting.utils.WindowUtils

class TestSettingActivityLifeManager : Application.ActivityLifecycleCallbacks {

    private var activityCount = 0

    override fun onActivityPaused(p0: Activity?) {
    }

    override fun onActivityResumed(p0: Activity?) {
    }

    override fun onActivityStarted(p0: Activity?) {
        if (ProcessUtils.isMainProcess()) {

            activityCount++

            if (ModelUtil.isTestModel)
                WindowUtils.showPopupWindow(p0!!)
        }
    }

    override fun onActivityDestroyed(p0: Activity?) {
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
    }

    override fun onActivityStopped(p0: Activity?) {
        if (ProcessUtils.isMainProcess()) {

            activityCount--

//            if (activityCount == 0)
//                if (ModelUtil.isTestModel)
//                    WindowUtils.hidePopupWindow()
        }
    }

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
    }
}