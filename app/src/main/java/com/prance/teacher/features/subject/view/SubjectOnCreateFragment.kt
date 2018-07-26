package com.prance.teacher.features.subject.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import com.prance.lib.common.utils.AnimUtil
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.subject.SubjectActivity
import kotlinx.android.synthetic.main.fragment_subject_on_create.*

class SubjectOnCreateFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_subject_on_create

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        createAnimate().start()

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
                countDown.clearAnimation()

                if (countDown.text.toString().toInt() == 1) {
                    (activity as SubjectActivity).onSubjectStart()
                } else {
                    countDown.text = (countDown.text.toString().toInt() - 1).toString()
                    createAnimate().start()
                }
            }
        })
        return animationSet
    }
}