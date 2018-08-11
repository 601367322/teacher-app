/*
package com.prance.teacher.features.redpackage.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.view.red.RedPackage

*/
/**
 *Created by rich on 2018/8/1
 *//*


class UpView(context: Context?, name: String, mTranslationY: Float): RelativeLayout(context) {
    var mContext: Context? = context
    var mTranslationY: Float = mTranslationY
    var tv_name: TextView
    var animatorSet: AnimatorSet? = null
    init {
        LayoutInflater.from(mContext).inflate(R.layout.upview,this)
        tv_name = findViewById(R.id.tv_name)
        tv_name.text = name
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        params.leftMargin = RedPackage.redPackageMargin
        layoutParams = params
    }

    fun startanimator(){
        if (animatorSet == null){
            val upAnimator = ObjectAnimator.ofFloat(this, "translationY", mTranslationY, mTranslationY - 100)
            val hideAnimator = ObjectAnimator.ofFloat(this, "Alpha", 1f, 0f)
            animatorSet = AnimatorSet()
            animatorSet?.play(upAnimator)?.with(hideAnimator)
            animatorSet?.duration = 1500
            animatorSet?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }
                override fun onAnimationEnd(animation: Animator?) {
                    recycle()
                }
                override fun onAnimationCancel(animation: Animator?) {
                }
                override fun onAnimationStart(animation: Animator?) {
                }
            })
        }
        animatorSet?.start()
    }

    fun recycle() {
        if (parent != null) {
            val relativeLayout = parent as ViewGroup
            relativeLayout.removeView(this)
        }
        translationY = 0f
        alpha = 1f
    }
}*/
