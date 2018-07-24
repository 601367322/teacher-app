package com.prance.teacher.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils
import com.prance.teacher.features.main.MainActivity
import com.prance.teacher.weight.FloatButton

class FloatButtonLifecycleManager : Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
        if (AppUtils.isAppForeground()) {
            FloatButton.hidePopupWindow()
        }
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {

        if (!AppUtils.isAppForeground()) {
            if (ActivityLifeManager.getInstance().contains(MainActivity::class.java.name)) {
                FloatButton.showPopupWindow(Utils.getApp())
            }
        }
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }
}