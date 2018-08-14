package com.prance.teacher.features.redpackage.view.red

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.*
import android.os.Looper
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.Utils
import com.prance.teacher.features.redpackage.model.RedPackageStatus
import com.prance.teacher.features.redpackage.model.StudentScore

class RedPackage {

    var screenHeight: Int = Utils.getApp().resources.displayMetrics.heightPixels

    //位置
    var x: Int
    var y: Int

    //创建时间
    var createTime: Long

    //透明度
    var alpha: Int

    //宽高
    var width: Long
    var height: Long

    //标题
    var title: String

    var fallAnimator: ValueAnimator? = null
    var hideAnimator: ValueAnimator? = null

    var bitmap: Bitmap? = null

    var scoreTip: ScoreTip? = null

    //被抢的状态
    var state = RedPackageStatus.CANNOTGRAB

    constructor(x: Int, y: Int, createTime: Long, alpha: Int, width: Long, height: Long, title: String, bitmap: Bitmap) {
        this.x = x
        this.y = y
        this.createTime = createTime
        this.alpha = alpha
        this.width = width
        this.height = height
        this.title = title
        this.bitmap = bitmap
    }

    fun startFall() {
        if (fallAnimator == null) {
            fallAnimator = ObjectAnimator.ofInt(-height.toInt(), screenHeight).setDuration(RedPackageManager.translationDurationTime)
            fallAnimator!!.interpolator = LinearInterpolator()
            fallAnimator!!.addUpdateListener {
                y = it.animatedValue.toString().toInt()

                if (y >= height) {
                    //全部出来之后才可以抢
                    state = RedPackageStatus.CANGRAB
                }
            }
            fallAnimator!!.addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    //如果该红包未被抢，则越过底部之后变为释放状态
                    if (state != RedPackageStatus.GRAB) {
                        state = RedPackageStatus.FREE

                        bitmap = null
                    }
                }
            })
        }
        fallAnimator!!.start()
    }

    fun destroy(studentScore: StudentScore) {
        //设置为已被抢状态
        state = RedPackageStatus.GRAB

        Thread({
            //生成抢到红包提示
            scoreTip = ScoreTip(x, y, width.toInt(), """${studentScore.student.name}  +${RedPackageManager.DEFAULT_SCORE}""")
        }).run()

        if (hideAnimator == null) {
            hideAnimator = ObjectAnimator.ofInt(alpha, 0).setDuration(200)
            hideAnimator!!.interpolator = LinearInterpolator()
            hideAnimator!!.addUpdateListener {
                alpha = it.animatedValue.toString().toInt()
            }
            hideAnimator!!.addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    //取消下落动画
                    fallAnimator?.cancel()

                    //动画结束之后变为释放状态
                    state = RedPackageStatus.FREE

                    bitmap = null
                }
            })
            hideAnimator!!.start()
        }
    }
}