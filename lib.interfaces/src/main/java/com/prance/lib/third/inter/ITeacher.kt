package com.prance.lib.third.inter

import android.app.Application
import android.content.Context
import android.content.Intent

interface ITeacher {

    fun getMainIntent(context: Context): Intent

    fun getLifecycle(): MutableList<Application.ActivityLifecycleCallbacks>
}