package com.prance.lib.teacher.base.core.platform

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.LogUtils


class HomeMenuBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val SYSTEM_DIALOG_REASON_KEY = "reason"
        const val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"
    }

    override fun onReceive(p0: Context?, intent: Intent?) {
        intent?.let {
            val action = intent.action
            if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {

                val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
                if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                    LogUtils.d("home键触发")
                }
            }
        }
    }
}