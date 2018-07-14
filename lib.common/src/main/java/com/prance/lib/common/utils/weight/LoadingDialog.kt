package com.prance.lib.common.utils.weight

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import com.prance.lib.common.R

class LoadingDialog : Dialog {

    constructor(context: Context) : super(context, R.style.DialogStyle) {
        /**设置对话框背景透明*/
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setGravity(Gravity.CENTER)
        val attr = window.attributes
        if (attr != null) {
            attr.height = context.resources.getDimensionPixelOffset(R.dimen.m200_0)
            attr.width = context.resources.getDimensionPixelOffset(R.dimen.m200_0)
            attr.gravity = Gravity.CENTER
        }
        setContentView(R.layout.weight_loading_dialog)
        window.attributes = attr
        setCanceledOnTouchOutside(false)
    }

}