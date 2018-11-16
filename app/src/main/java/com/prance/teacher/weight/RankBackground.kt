package com.prance.teacher.weight

import android.animation.*
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.AnimUtil
import com.prance.lib.common.utils.getInflate
import com.prance.teacher.R
import kotlinx.android.synthetic.main.layout_rank_background.view.*

class RankBackground : RelativeLayout {

    val animators: MutableList<Animator> = mutableListOf()

    var peopleNumber: Int = 0

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

        //更多排名边框
        val borderAnim = ObjectAnimator.ofInt(80, 1580)
        animators.add(borderAnim)
        borderAnim.addUpdateListener { i ->
            val params = lower_anim_2.layoutParams as LinearLayout.LayoutParams
            params.leftMargin = i.animatedValue.toString().toInt()
            lower_anim_2.layoutParams = params
        }

        //更多排名背景
        val lowerBGScale = AnimatorSet()
        animators.add(lowerBGScale)
        lowerBGScale.playTogether(ObjectAnimator.ofFloat(lower_anim_1, AnimUtil.SCALEY, 0F, 1F), ObjectAnimator.ofFloat(lower_anim_1, AnimUtil.SCALEX, 0F, 1F))
        lowerBGScale.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                val scoreLayouts = mutableListOf(scoreLayout6, scoreLayout7, scoreLayout8, scoreLayout9, scoreLayout10)
                var delay = 0L
                for ((index, layout) in scoreLayouts.withIndex()) {
                    if (index + 5 < peopleNumber) {
                        postDelayed({
                            val animatorSet = AnimatorSet()
                            animators.add(animatorSet)
                            animatorSet.playTogether(ObjectAnimator.ofFloat(layout, AnimUtil.ALPHA, 0f, 1f), ObjectAnimator.ofFloat(layout, AnimUtil.SCALEY, 1.5F, 1F), ObjectAnimator.ofFloat(layout, AnimUtil.SCALEX, 1.5F, 1F))
                            animatorSet.start()
                        }, delay)
                        delay += 50
                    }
                }
            }
        })

        //更多排名效果
        val lowerScale = AnimatorSet()
        animators.add(lowerScale)
        lowerScale.playTogether(ObjectAnimator.ofFloat(lower_anim_0, AnimUtil.SCALEY, 0F, 1F), ObjectAnimator.ofFloat(lower_anim_0, AnimUtil.SCALEX, 0F, 1F))
        lowerScale.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)

                borderAnim.start()

                lowerBGScale.start()
            }
        })


        //奖杯效果
        val cupsParams = cups.layoutParams as ConstraintLayout.LayoutParams
        val cupAnim = ObjectAnimator.ofInt(cupsParams.topMargin + cupsParams.height, cupsParams.topMargin)
        animators.add(cupAnim)
        cupAnim.addUpdateListener { it ->
            val params = cups.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = it.animatedValue.toString().toInt()
            cups.layoutParams = params
        }
        cupAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                cups.visible()
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                lightAnim.start()

                if (peopleNumber > 6)
                    lowerScale.start()
            }
        })
        cupAnim.duration = 1000
        cupAnim.start()

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