package com.prance.teacher.utils

import android.content.Context
import android.content.Intent

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
    }
}