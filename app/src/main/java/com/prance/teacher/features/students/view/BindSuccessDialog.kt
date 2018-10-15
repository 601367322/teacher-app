package com.prance.teacher.features.students.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prance.lib.common.utils.GlideApp
import com.prance.teacher.R
import kotlinx.android.synthetic.main.weight_bind_loading_dialog.*

class BindSuccessDialog : Dialog {

    constructor(context: Context) : super(context, R.style.DialogStyle) {
        /**设置对话框背景透明*/
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setGravity(Gravity.CENTER)
        val attr = window.attributes
        if (attr != null) {
            attr.height = context.resources.getDimensionPixelOffset(R.dimen.m489_0)
            attr.width = context.resources.getDimensionPixelOffset(R.dimen.m502_0)
            attr.gravity = Gravity.CENTER
        }
        setContentView(R.layout.weight_bind_success_dialog)

        window.attributes = attr
        setCanceledOnTouchOutside(false)
    }

}