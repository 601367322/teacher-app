package com.prance.third.impl

import android.content.Context
import android.content.Intent
import com.prance.lib.third.inter.ITeacher
import com.prance.teacher.features.login.LoginActivity

class TeacherImpl : ITeacher {
    override fun getMainIntent(context: Context): Intent {

        val foregroundIntent = Intent()
        foregroundIntent.action = Intent.ACTION_MAIN
        foregroundIntent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        foregroundIntent.setClass(context, LoginActivity::class.java)
        foregroundIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        return foregroundIntent
    }
}