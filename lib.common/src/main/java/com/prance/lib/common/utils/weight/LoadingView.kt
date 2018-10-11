package com.prance.lib.common.utils.weight

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.prance.lib.common.R
import com.prance.lib.common.utils.AnimUtil
import kotlinx.android.synthetic.main.weight_loading_dialog.*

class LoadingView : ImageView {

    var mAnimator: ObjectAnimator? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setImageResource(R.drawable.icon_loading)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (mAnimator == null) {
            mAnimator = ObjectAnimator.ofFloat(this, AnimUtil.ROTATION, 0F, 360F)
            mAnimator!!.duration = 1000
            mAnimator!!.interpolator = LinearInterpolator()
            mAnimator!!.repeatMode = ValueAnimator.RESTART
            mAnimator!!.repeatCount = ValueAnimator.INFINITE
        }
        mAnimator?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mAnimator?.cancel()
    }
}