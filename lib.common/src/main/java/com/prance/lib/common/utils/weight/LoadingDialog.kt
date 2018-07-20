package com.prance.lib.common.utils.weight

import android.animation.ObjectAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.RESTART
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.animation.LinearInterpolator
import com.prance.lib.common.R
import com.prance.lib.common.utils.AnimUtil
import kotlinx.android.synthetic.main.weight_loading_dialog.*

class LoadingDialog : Dialog {

    var mAnimator: ObjectAnimator? = null

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

    override fun show() {
        super.show()

        if (mAnimator == null) {
            mAnimator = ObjectAnimator.ofFloat(loadingImg, AnimUtil.ROTATION, 0F, 360F)
            mAnimator!!.duration = 1000
            mAnimator!!.interpolator = LinearInterpolator()
            mAnimator!!.repeatMode = RESTART
            mAnimator!!.repeatCount = INFINITE
        }
        mAnimator?.start()
    }

    fun show(str: String?) {
        str?.let { loadingText.text = str }

        show()
    }

    override fun dismiss() {
        super.dismiss()

        mAnimator?.cancel()
    }
}