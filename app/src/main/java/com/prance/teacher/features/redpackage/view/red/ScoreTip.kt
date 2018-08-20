package com.prance.teacher.features.redpackage.view.red

import android.animation.*
import android.graphics.*
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.Utils
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.model.RedPackageTipStatus
import com.prance.teacher.weight.FontCustom

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

    //标题
    var title: String

    var translationAnimator: ValueAnimator? = null
    var hideAnimator: ValueAnimator? = null

    var bitmap: Bitmap? = null

    //动画时长
    val translationDurationTime = 3000L

    //被抢的状态
    var state = RedPackageTipStatus.SHOW

    constructor(x: Int, y: Int, redPackageWidth: Int, redPackageHeight: Int, title: String, background: Bitmap) {

        this.title = title

        //底部文字溢出长度
        val bottomPadding = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m30_0)

        this.width = background.width

        //红包背景378
        var bitmap = Bitmap.createBitmap(
                background.width,
                background.height,
                Bitmap.Config.ARGB_4444)

        val canvas = Canvas(bitmap)

        canvas.drawBitmap(background, 0f, 0f, null)

        val strokeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        strokeTextPaint.color = Color.parseColor("#923D00")
        strokeTextPaint.textAlign = Paint.Align.CENTER
        strokeTextPaint.strokeWidth = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m3_0).toFloat()
        strokeTextPaint.style = Paint.Style.FILL_AND_STROKE
        strokeTextPaint.typeface = FontCustom.getFZY1JWFont(Utils.getApp())
        strokeTextPaint.textSize = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m47_0).toFloat()

        //文字画笔
        val textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.color = Color.WHITE
        textPaint.typeface = FontCustom.getFZY1JWFont(Utils.getApp())
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m47_0).toFloat()

        //计算文字宽高
        val textRect = Rect()
        textPaint.getTextBounds(title, 0, title.length, textRect)

        var textHeight = textRect.height()

        var targetRect = Rect(0, 0, width, textHeight)
        //绘制名字
        val fontMetrics = textPaint.fontMetricsInt
        val baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2

        val textStartY = bitmap.height - baseline.toFloat() + bottomPadding

        canvas.drawText(title, targetRect.centerX().toFloat(), textStartY, strokeTextPaint)
        canvas.drawText(title, targetRect.centerX().toFloat(), textStartY, textPaint)

        this.bitmap = bitmap

        this.x = x + (redPackageWidth - this.width) / 2

        this.y = y - (bitmap.height - redPackageHeight)

        startFall()
    }

    var animatorSet: AnimatorSet? = null

    private fun startFall() {
        val fallAnimator = ObjectAnimator.ofInt(y, y - translationDistance)
        fallAnimator!!.addUpdateListener {
            y = it.animatedValue.toString().toInt()
        }
        val hideAnimator = ObjectAnimator.ofInt(alpha, 0)
        hideAnimator!!.addUpdateListener {
            alpha = it.animatedValue.toString().toInt()
        }

        animatorSet = AnimatorSet()
        animatorSet!!.playTogether(fallAnimator, hideAnimator)
        animatorSet!!.duration = translationDurationTime
        animatorSet!!.interpolator = LinearInterpolator()
        animatorSet!!.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                state = RedPackageTipStatus.HIDE

                bitmap?.recycle()
                bitmap = null
            }
        })
        animatorSet!!.start()
    }

    fun destroy() {
        animatorSet?.cancel()
    }


}