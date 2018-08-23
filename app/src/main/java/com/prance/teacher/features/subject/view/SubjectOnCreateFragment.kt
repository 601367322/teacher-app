package com.prance.teacher.features.subject.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import com.prance.lib.common.utils.AnimUtil
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.subject.SubjectActivity
import kotlinx.android.synthetic.main.fragment_subject_on_create.*
import android.media.SoundPool
import android.media.AudioAttributes
import android.media.AudioManager
import android.content.Context.AUDIO_SERVICE
import com.blankj.utilcode.util.LogUtils
import com.prance.teacher.utils.SoundUtils


class SubjectOnCreateFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_subject_on_create

    var num = 2

    var countDownTimerImg = mutableListOf(R.drawable.count_down_timer_1, R.drawable.count_down_timer_2, R.drawable.count_down_timer_3)

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        createAnimate().start()

        SoundUtils.play("count_time")
    }

    private fun createAnimate(): AnimatorSet {
        val animatorA = ObjectAnimator.ofFloat(countDown, AnimUtil.ALPHA, 1F, 0F)
        val animatorX = ObjectAnimator.ofFloat(countDown, AnimUtil.SCALEX, 1F, 1.5F)
        val animatorY = ObjectAnimator.ofFloat(countDown, AnimUtil.SCALEY, 1F, 1.5F)

        val animationSet = AnimatorSet()
        animationSet.playTogether(animatorA, animatorX, animatorY)
        animationSet.interpolator = DecelerateInterpolator()
        animationSet.duration = 1000

        animationSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                countDown?.run {
                    clearAnimation()

                    if (num == 0) {
                        (activity as SubjectActivity).onSubjectStart()
                    } else {
                        num--
                        countDown.setImageResource(countDownTimerImg[num])
                        createAnimate().start()
                    }
                }
            }
        })
        return animationSet
    }
}