package com.prance.teacher.features.redpackage.view

import android.content.Context
import android.graphics.Color
import android.widget.TextView

/**
 *Created by rich on 2018/7/27
 */

class RedPackageView(context: Context?): TextView(context) {

    /**
     * 设置红包对应的选项
     */
    fun setChoose(choose: String){
        text = choose
        setTextColor(Color.RED)
        textSize = 40F
    }
}