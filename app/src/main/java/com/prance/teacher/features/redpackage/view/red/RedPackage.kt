package com.prance.teacher.features.redpackage.view.red

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.Utils
import com.prance.teacher.BuildConfig
import com.prance.teacher.features.redpackage.model.RedPackageStatus
import com.prance.teacher.features.redpackage.model.StudentScore
import com.prance.teacher.features.redpackage.view.red.RedPackageManager.Companion.DEFAULT_ALPHA
import com.prance.teacher.features.redpackage.view.red.RedPackageManager.Companion.DEFAULT_SCALE
import com.prance.teacher.features.redpackage.view.red.RedPackageManager.Companion.DEFAULT_WIDTH
import com.prance.teacher.features.redpackage.view.red.RedPackageManager.Companion.lines
import com.prance.teacher.features.students.model.StudentsEntity

class RedPackage {

    var screenHeight: Int = Utils.getApp().resources.displayMetrics.heightPixels
    //屏幕宽高
    private var screenWidth: Int = Utils.getApp().resources.displayMetrics.widthPixels

    //位置
    var x: Int
    var y: Int

    //创建时间
    var createTime: Long

    //透明度
    var alpha: Int

    //宽高
    var width: Int
    var height: Int

    //标题
    var title: String

    var translationAnimator: ValueAnimator? = null
    var hideAnimator: ValueAnimator? = null

    var bitmap: Bitmap

    var tipBitmap: Bitmap

    var scoreTip: ScoreTip? = null

    //被抢的状态
    var state = RedPackageStatus.CANNOTGRAB

    var context: Context

    //第几泳道
    var lineNum: Int

    var big = false

    constructor(context: Context, width: Int, height: Int, title: String, lineNum: Int, big: Boolean, bitmap: Bitmap, tipBitmap: Bitmap) {
        this.context = context
        this.x = getRedPackageStartX(lineNum).toInt()
        this.y = -height
        this.createTime = System.currentTimeMillis()
        this.alpha = DEFAULT_ALPHA
        this.width = width
        this.height = height
        this.title = title
        this.big = big
        if (big) {
            //放大红包
            val oldWidth = bitmap.width
            val matrix = Matrix()
            matrix.postScale(DEFAULT_SCALE, DEFAULT_SCALE)
            this.bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

            this.x -= ((this.bitmap.width.toFloat() - oldWidth.toFloat()) / 2F).toInt()
            this.y = -this.bitmap.height

            this.width = this.bitmap.width
            this.height = this.bitmap.height
        } else {
            this.bitmap = bitmap
        }
        this.tipBitmap = tipBitmap
        this.lineNum = lineNum
    }

    fun startFall() {
        if (translationAnimator == null) {
            translationAnimator = ObjectAnimator.ofInt(-height.toInt(), screenHeight).setDuration(RedPackageManager.translationDurationTime)
            translationAnimator!!.interpolator = LinearInterpolator()
            translationAnimator!!.addUpdateListener {
                y = it.animatedValue.toString().toInt()

                if (y >= height) {
                    //全部出来之后才可以抢
                    state = RedPackageStatus.CANGRAB
                }

                if (BuildConfig.DEBUG) {
                    if (y > 600 && hideAnimator == null) {
                        destroy(StudentScore(StudentsEntity(mutableListOf("百度", "笑话", "呵呵")[(Math.random() * 2).toInt()], "https://www.baidu.com/img/bd_logo1.png"), 2, 2))
                    }
                }
            }
            translationAnimator!!.addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    //如果该红包未被抢，则越过底部之后变为释放状态
                    if (state != RedPackageStatus.GRAB) {
                        state = RedPackageStatus.FREE
                    }
                }
            })
        }
        translationAnimator!!.start()
    }

    fun destroy(studentScore: StudentScore) {
        //设置为已被抢状态
        state = RedPackageStatus.GRAB

        //生成抢到红包提示
        scoreTip = ScoreTip(context, x, y, width, height, studentScore.student, tipBitmap, big)

        if (hideAnimator == null) {
            hideAnimator = ObjectAnimator.ofInt(alpha, 0).setDuration(RedPackageManager.alphaDurationTime)
            hideAnimator!!.interpolator = LinearInterpolator()
            hideAnimator!!.addUpdateListener {
                alpha = it.animatedValue.toString().toInt()
            }
            hideAnimator!!.addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    //取消下落动画
                    translationAnimator?.cancel()

                    //动画结束之后变为释放状态
                    state = RedPackageStatus.FREE
                }
            })
            hideAnimator!!.start()
        }
    }

    /**
     * 红包开始的X
     */
    private fun getRedPackageStartX(lineNumber: Int): Float {
        val startX = getLineWidth() * lineNumber
        return ((getLineWidth() - DEFAULT_WIDTH) / 2) + startX
    }

    /**
     * 泳道的宽度
     */
    private fun getLineWidth(): Float {
        return screenWidth.toFloat() / lines.toFloat()
    }

}