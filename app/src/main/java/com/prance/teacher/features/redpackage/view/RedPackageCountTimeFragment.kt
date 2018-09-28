package com.prance.teacher.features.redpackage.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.prance.lib.common.utils.AnimUtil
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.RedPackageActivity
import com.prance.teacher.features.subject.SubjectActivity
import kotlinx.android.synthetic.main.fragment_subject_count_time.*
import com.prance.teacher.utils.SoundUtils


class RedPackageCountTimeFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_red_package_count_time

    var num = 3

    var countDownTimerImg = mutableListOf(R.drawable.count_down_timer_go, R.drawable.count_down_timer_1, R.drawable.count_down_timer_2, R.drawable.count_down_timer_3)

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        createAnimate().start()

        SoundUtils.play("four_count_time")
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
                        (activity as RedPackageActivity).onRedPackageStart()
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


    override fun onDestroy() {
        super.onDestroy()
        SoundUtils.stop("four_count_time")
    }
}