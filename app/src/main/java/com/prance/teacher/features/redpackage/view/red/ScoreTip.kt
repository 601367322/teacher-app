package com.prance.teacher.features.redpackage.view.red

import android.animation.*
import android.graphics.*
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.Utils
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.model.RedPackageTipStatus

class ScoreTip {

    //向上消失距离
    var translationDistance: Int = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m40_0)

    //位置
    var x: Int
    var y: Int

    //透明度
    var alpha: Int = RedPackageManager.DEFAULT_ALPHA

    //宽高
    var width: Int
    var height: Int

    //标题
    var title: String

    var fallAnimator: ValueAnimator? = null
    var hideAnimator: ValueAnimator? = null

    var bitmap: Bitmap? = null

    //动画时长
    val translationDurationTime = 3000L

    //被抢的状态
    var state = RedPackageTipStatus.SHOW

    constructor(x: Int, y: Int, redPackageWidth: Int, title: String) {

        this.title = title

        //文字画笔
        val textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.color = Color.WHITE
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m20_0).toFloat()

        this.width = textPaint.measureText(title).toInt()
        //计算文字宽高
        val textRect = Rect()
        textPaint.getTextBounds(title, 0, title.length, textRect)

        this.height = textRect.height()

        //红包背景
        var bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        var targetRect = Rect(0, 0, width, height)
        //绘制名字
        val fontMetrics = textPaint.fontMetricsInt
        val baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2

        canvas.drawText(title, targetRect.centerX().toFloat(), baseline.toFloat(), textPaint)
        this.bitmap = bitmap

        this.x = x + (redPackageWidth - this.width) / 2

        this.y = y

        startFall()
    }

    private fun startFall() {
        val fallAnimator = ObjectAnimator.ofInt(y, y - translationDistance)
        fallAnimator!!.addUpdateListener {
            y = it.animatedValue.toString().toInt()
        }
        val hideAnimator = ObjectAnimator.ofInt(alpha, 0)
        hideAnimator!!.addUpdateListener {
            alpha = it.animatedValue.toString().toInt()
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(fallAnimator, hideAnimator)
        animatorSet.duration = translationDurationTime
        animatorSet.interpolator = LinearInterpolator()
        animatorSet.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                state = RedPackageTipStatus.HIDE

                bitmap?.recycle()
                bitmap = null
            }
        })
        animatorSet.start()
    }

    override fun toString(): String {
        return "ScoreTip(translationDistance=$translationDistance, x=$x, y=$y, alpha=$alpha, width=$width, height=$height, title='$title', fallAnimator=$fallAnimator, hideAnimator=$hideAnimator, bitmap=$bitmap, translationDurationTime=$translationDurationTime, state=$state)"
    }

}