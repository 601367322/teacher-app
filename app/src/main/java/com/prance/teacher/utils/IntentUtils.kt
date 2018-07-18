package com.prance.teacher.utils

import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.IntentUtils

class IntentUtils {

    companion object {

        /**
         * 小鱼拨号界面
         */
        fun callingXYDial(): Intent {
            val intent = Intent()
            intent.setClassName("com.xylink.gill", "com.ainemo.becky.eapp.activity.home.DialerActivity")
            return intent
        }

        /**
         * 小鱼主界面
         */
        fun callingXYHome(): Intent {
            val mHomeIntent = IntentUtils.getLaunchAppIntent("com.xylink.launcher")
            mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            return mHomeIntent
        }

        /**
         * 小鱼主界面
         */
        fun callingTVHome(): Intent {
            val mHomeIntent = Intent(Intent.ACTION_MAIN)
            mHomeIntent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
            mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            return mHomeIntent
        }
    }
}