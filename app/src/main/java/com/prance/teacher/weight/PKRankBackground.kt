package com.prance.teacher.weight

import android.animation.*
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import cn.sunars.sdk.SunARS.addListener
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.AnimUtil
import com.prance.lib.common.utils.getInflate
import com.prance.teacher.R
import kotlinx.android.synthetic.main.layout_pk_rank_background.view.*

class PKRankBackground : RelativeLayout {

    val animators: MutableList<Animator> = mutableListOf()

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        addView(getInflate(this, R.layout.layout_pk_rank_background))

        //背景旋转光效
        val lightAnim = AnimatorSet()
        animators.add(lightAnim)
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
                light.visible()
            }
        })
        lightAnim.playTogether(rotationAnim, alphaAnim,
                ObjectAnimator.ofFloat(light, AnimUtil.SCALEY, 0F, 1F),
                ObjectAnimator.ofFloat(light, AnimUtil.SCALEX, 0F, 1F))
        lightAnim.interpolator = LinearInterpolator()

        //奖杯效果
        val cupAnimSet = AnimatorSet()
        val cupAnim = ObjectAnimator.ofFloat(cups, AnimUtil.TRANSLATIONY, cups.y + resources.getDimensionPixelOffset(R.dimen.m200_0), cups.y)
        val cupAnim1 = ObjectAnimator.ofFloat(my_cup, AnimUtil.TRANSLATIONY, my_cup.y + resources.getDimensionPixelOffset(R.dimen.m200_0), my_cup.y)
        animators.add(cupAnimSet)
        cupAnimSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                cups.visible()
                my_cup.visible()
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                lightAnim.start()
            }
        })
        cupAnimSet.duration = 500
        cupAnimSet.playTogether(cupAnim, cupAnim1)
        cupAnimSet.start()


        //星星效果
        val startAnim = AnimatorSet()
        animators.add(startAnim)
        startAnim.playTogether(createStarAnim(star1), createStarAnim(star2), createStarAnim(star3), createStarAnim(star4), createStarAnim(star5), createStarAnim(star6))
        startAnim.start()

        //流星效果
        val meteorWidth = resources.getDimensionPixelOffset(R.dimen.m383_0)
        val meteorHeight = resources.getDimensionPixelOffset(R.dimen.m191_0)
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels

        animators.add(createMeteorAnim(
                meteor1,
                -meteorHeight.toFloat() + resources.getDimensionPixelOffset(R.dimen.m14_0),
                resources.getDimensionPixelOffset(R.dimen.m772_0).toFloat(),
                -meteorWidth.toFloat(),
                screenWidth.toFloat(),
                3000
        ))
        animators.add(createMeteorAnim(
                meteor2,
                -meteorHeight.toFloat(),
                resources.getDimensionPixelOffset(R.dimen.m138_0).toFloat(),
                resources.getDimensionPixelOffset(R.dimen.m1511_0) - meteorWidth.toFloat(),
                screenWidth.toFloat(),
                5000
        ))
        animators.add(createMeteorAnim(
                meteor3,
                resources.getDimensionPixelOffset(R.dimen.m673_0).toFloat() - meteorHeight.toFloat(),
                screenHeight.toFloat(),
                -meteorWidth.toFloat(),
                resources.getDimensionPixelOffset(R.dimen.m990_0).toFloat(),
                2000
        ))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        for (anim in animators) {
            try {
                anim.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createMeteorAnim(view: View, startY: Float, endY: Float, startX: Float, endX: Float, duration: Long): AnimatorSet {
        val meteorAnimY = ObjectAnimator.ofFloat(view, AnimUtil.TRANSLATIONY, startY, endY)
        meteorAnimY.repeatCount = Animation.INFINITE
        meteorAnimY.repeatMode = ValueAnimator.RESTART

        val meteorAnimX = ObjectAnimator.ofFloat(view, AnimUtil.TRANSLATIONX, startX, endX)
        meteorAnimX.repeatCount = Animation.INFINITE
        meteorAnimX.repeatMode = ValueAnimator.RESTART

        val meteorAnim = AnimatorSet()
        meteorAnim.playTogether(meteorAnimX, meteorAnimY)
        meteorAnim.duration = duration
        meteorAnim.start()

        return meteorAnim
    }

    private fun createStarAnim(view: View): ObjectAnimator {
        val starAlphaAnim = ObjectAnimator.ofFloat(view, AnimUtil.ALPHA, 1F, 0.2F)
        starAlphaAnim.duration = 1000
        starAlphaAnim.repeatCount = Animation.INFINITE
        starAlphaAnim.repeatMode = ValueAnimator.REVERSE
        return starAlphaAnim
    }
}