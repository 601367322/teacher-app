/*
package com.prance.teacher.features.redpackage.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.support.constraint.solver.GoalRow
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.model.RedPackageStatus
import com.prance.teacher.utils.DimenUtils
import java.util.*

*/
/**
 *Created by rich on 2018/7/27
 *//*


class RedPackage {


    lateinit var mContext: Context
    var roadPosition: Int = 0
    var mStatus: RedPackageStatus = RedPackageStatus.CANNOTGRAB
    var screenHeight: Int = 0
    var screenWidth: Int = 0
    var fallAnimator: ObjectAnimator? = null
    var hideAnimator: ObjectAnimator? = null
    var translationY = 0f
    var alpha = 1f
    var text: String? = null

    constructor(mContext: Context) {
        this.mContext = mContext

        val resources = mContext.resources
        val dm = resources.displayMetrics
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels
    }

    companion object {
        */
/**
         * 可以被使用的选项
         *//*

        private var canUseSigns: LinkedList<String> = LinkedList()
        */
/**
         * 红包距离父view的边距
         *//*

        var redPackageMargin: Int = 0
        var redPackageWidth = 66
        var redPackageHeight = 120

        init {
            canUseSigns.addAll(arrayOf("A", "B", "C", "D"))
        }
    }


    */
/**
     * 设置红包对应的选项
     *//*

    fun setChoose(choose: String) {
        text = choose
    }

    fun getChoose(): String {
        return text.toString()
    }

    */
/**
     * 红包被抢到
     *//*

    fun grab(name: String) {
//        if (parent != null) {
//            val relativeLayout = parent as ViewGroup
//            val upView = UpView(mContext, name, translationY)
//            relativeLayout.addView(upView)
//            upView.startanimator()
//        }
        if (hideAnimator == null) {
            hideAnimator = ObjectAnimator.ofFloat(this, "Alpha", 1f, 0f)
            hideAnimator?.setDuration(1000)
            hideAnimator?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    fallAnimator?.cancel()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
        }
        hideAnimator?.start()
    }

    */
/**
     * 红包开始下落的动画
     *//*

    fun startFall() {
        if (fallAnimator == null) {
            fallAnimator = ObjectAnimator.ofFloat(this, "translationY", -DimenUtils.dip2px(mContext!!, redPackageHeight.toFloat()).toFloat(), screenHeight.toFloat())
            fallAnimator?.addUpdateListener {
                //            var value = it.getAnimatedValue() as Float
//            if (value < 0.2){
//                mStatus = RedPackageStatus.CANNOTGRAB
//                setTextColor(Color.YELLOW)
//            } else if (value > 0.2){
//                mStatus = RedPackageStatus.CANGRAB
//                setTextColor(Color.RED)
//            } else if (value > 0.8){
//                mStatus = RedPackageStatus.CANNOTGRAB
//                setTextColor(Color.YELLOW)
//            }
            }
            fallAnimator?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    if (mStatus != RedPackageStatus.GRAB)
                        canUseSigns.add(getChoose())
                    recycle()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                    canUseSigns.remove(getChoose())
                    mStatus = RedPackageStatus.CANGRAB
                }

            })
            fallAnimator?.setDuration(5000)
        }
        fallAnimator?.start()
    }

    fun recycle() {
        mStatus = RedPackageStatus.FREE
        translationY = 0f
        alpha = 1f
    }
}*/
