package com.prance.teacher.features.redpackage.view.red

import android.animation.*
import android.content.Context
import android.graphics.*
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.prance.lib.common.utils.GlideApp
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.model.RedPackageTipStatus
import com.prance.teacher.features.students.model.StudentsEntity
import com.prance.teacher.weight.FontCustom
import android.graphics.Paint.FILTER_BITMAP_FLAG
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.PaintFlagsDrawFilter


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

    var bitmap: Bitmap

    //动画时长
    val translationDurationTime = 3000L

    //被抢的状态
    var state = RedPackageTipStatus.SHOW
    var context: Context

    constructor(context: Context, x: Int, y: Int, redPackageWidth: Int, redPackageHeight: Int, student: StudentsEntity, background: Bitmap) {

        this.context = context

        this.title = student.name

        //底部文字溢出长度
        val bottomPadding = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m30_0)

        this.width = background.width

        //红包背景378
        bitmap = Bitmap.createBitmap(
                background.width,
                background.height,
                Bitmap.Config.ARGB_4444)

        val canvas = Canvas(bitmap)

        canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
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
        textPaint.getTextBounds(student.name, 0, student.name.length, textRect)

        var textHeight = textRect.height()

        var targetRect = Rect(0, 0, width, textHeight)
        //绘制名字
        val fontMetrics = textPaint.fontMetricsInt
        val baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2

        val textStartY = bitmap.height - baseline.toFloat() + bottomPadding

        canvas.drawText(student.name, targetRect.centerX().toFloat(), textStartY, strokeTextPaint)
        canvas.drawText(student.name, targetRect.centerX().toFloat(), textStartY, textPaint)

        this.x = x + (redPackageWidth - this.width) / 2

        this.y = y - (bitmap.height - redPackageHeight)

        startFall()

        GlideApp.with(context)
                .asBitmap()
                .load(student.head)
                .circleCrop()
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        //红包背景378
                        val canvas = Canvas(this@ScoreTip.bitmap)
                        canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
                        var avatarWidth = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m190_0)
                        // 初始化Matrix对象
                        val matrix = Matrix()
                        // 根据传入的参数设置缩放比例
                        matrix.postScale(avatarWidth.toFloat() / resource.width.toFloat(), avatarWidth.toFloat() / resource.width.toFloat())
                        val bottomPadding = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m56_0) + avatarWidth

                        matrix.postTranslate((this@ScoreTip.bitmap.width.toFloat() - avatarWidth.toFloat()) / 2F, (this@ScoreTip.bitmap.height - bottomPadding).toFloat())
                        canvas.drawBitmap(resource, matrix, null)
                    }
                })
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

                bitmap.recycle()
            }
        })
        animatorSet!!.start()
    }
}