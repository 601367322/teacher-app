package com.prance.teacher.utils

import android.content.Context

/**
 *Created by rich on 2018/7/31
 */

class DimenUtils {
    companion object {
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.getResources().getDisplayMetrics().density
            return (dpValue * scale + 0.5f).toInt()
        }
    }
}