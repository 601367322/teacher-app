package com.prance.teacher.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils

class KillerBroadcast : BroadcastReceiver() {

    companion object {

        val KILL = "com.prance.teacher.services.KillerBroadcast.kill"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.run {
            LogUtils.i(this.action)
            if (this.action == KILL) {
                AppUtils.exitApp()
            }
        }
    }
}