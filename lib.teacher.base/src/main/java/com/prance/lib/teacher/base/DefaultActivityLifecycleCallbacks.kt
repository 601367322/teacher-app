package com.prance.lib.teacher.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.teacher.base.utils.CleanLeakUtils

class DefaultActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity?) {
//        LogUtils.i(activity?.componentName)
    }

    override fun onActivityResumed(activity: Activity?) {
//        LogUtils.i(activity?.componentName)
    }

    override fun onActivityStarted(activity: Activity?) {
//        LogUtils.i(activity?.componentName)
    }

    override fun onActivityDestroyed(activity: Activity?) {
//        LogUtils.i(activity?.componentName)
        CleanLeakUtils.fixInputMethodManagerLeak(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
//        LogUtils.i(activity?.componentName)
    }

    override fun onActivityStopped(activity: Activity?) {
//        LogUtils.i(activity?.componentName)
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
//        LogUtils.i(activity?.componentName)
    }
}