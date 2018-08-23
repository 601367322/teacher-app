package com.prance.teacher.weight

import android.animation.*
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import com.prance.lib.common.utils.AnimUtil
import com.prance.lib.common.utils.getInflate
import com.prance.teacher.R
import com.prance.teacher.R.attr.strokeColor
import com.prance.teacher.R.attr.strokeWidth
import kotlinx.android.synthetic.main.layout_rank_background.view.*

class RankBackground : RelativeLayout {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        addView(getInflate(this, R.layout.layout_rank_background))

        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.RankBackground)
            try {
                val cupsRes = a.getResourceId(R.styleable.RankBackground_cups, -1)
                if (cupsRes != -1) {
                    cups.setImageResource(cupsRes)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                a.recycle()
            }
        }

        //背景旋转光效
        val lightAnim = AnimatorSet()
        val rotationAnim = ObjectAnimator.ofFloat(light, AnimUtil.ROTATION, 0F, 360F)
        rotationAnim.duration = 8000
        rotationAnim.repeatCount = Animation.INFINITE
        rotationAnim.repeatMode = ValueAnimator.RESTART

        val alphaAnim = ObjectAnimator.ofFloat(light, AnimUtil.ALPHA, 0.9F, 0.2F)
        alphaAnim.duration = 1000
        alphaAnim.repeatCount = Animation.INFINITE
        alphaAnim.repeatMode = ValueAnimator.REVERSE

        lightAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                light.visibility = View.VISIBLE
            }
        })
        lightAnim.playTogether(rotationAnim, alphaAnim,
                ObjectAnimator.ofFloat(light, AnimUtil.SCALEY, 0F, 1F),
                ObjectAnimator.ofFloat(light, AnimUtil.SCALEX, 0F, 1F))
        lightAnim.interpolator = LinearInterpolator()

        //奖杯效果
        val cupAnim = ObjectAnimator.ofFloat(cups, AnimUtil.TRANSLATIONY, cups.y + resources.getDimensionPixelOffset(R.dimen.m200_0), cups.y)
        cupAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                cups.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                lightAnim.start()
            }
        })
        cupAnim.duration = 500
        cupAnim.start()

        //星星效果
        val startAnim = AnimatorSet()
        startAnim.playTogether(createStarAnim(star1), createStarAnim(star2), createStarAnim(star3), createStarAnim(star4), createStarAnim(star5), createStarAnim(star6))
        startAnim.start()

        //流星效果
        val liuxingWidth = resources.getDimensionPixelOffset(R.dimen.m383_0)
        val liuxingHeight = resources.getDimensionPixelOffset(R.dimen.m191_0)
        val screenWidth = resources.displayMetrics.widthPixels
        val liuxingAnimY = ObjectAnimator.ofFloat(liuxing, AnimUtil.TRANSLATIONY, -liuxingHeight.toFloat() + resources.getDimensionPixelOffset(R.dimen.m14_0), resources.getDimensionPixelOffset(R.dimen.m772_0).toFloat())
        liuxingAnimY.repeatCount = Animation.INFINITE
        liuxingAnimY.repeatMode = ValueAnimator.RESTART

        val liuxingAnimX = ObjectAnimator.ofFloat(liuxing, AnimUtil.TRANSLATIONX, -liuxingWidth.toFloat(), screenWidth.toFloat())
        liuxingAnimX.repeatCount = Animation.INFINITE
        liuxingAnimX.repeatMode = ValueAnimator.RESTART

        val liuxingAnim = AnimatorSet()
        liuxingAnim.playTogether(liuxingAnimX, liuxingAnimY)
        liuxingAnim.duration = 3000
        liuxingAnim.start()
    }

    private fun createStarAnim(view: View): ObjectAnimator {
        val starAlphaAnim = ObjectAnimator.ofFloat(view, AnimUtil.ALPHA, 1F, 0.2F)
        starAlphaAnim.duration = 1000
        starAlphaAnim.repeatCount = Animation.INFINITE
        starAlphaAnim.repeatMode = ValueAnimator.REVERSE
        return starAlphaAnim
    }
}