package com.prance.teacher.features.redpackage.view.red

import android.animation.*
import android.content.Context
import android.graphics.*
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.request.transition.Transition
import com.prance.lib.common.utils.GlideApp
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.model.RedPackageTipStatus
import com.prance.teacher.features.students.model.StudentsEntity
import com.prance.teacher.weight.FontCustom
import android.graphics.PaintFlagsDrawFilter
import android.text.TextUtils
import com.bumptech.glide.request.target.SimpleTarget
import jp.wasabeef.glide.transformations.CropCircleTransformation


class ScoreTip {

    var context: Context

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
    val translationDurationTime = 1000L

    //被抢的状态
    var state = RedPackageTipStatus.SHOW

    //动画集
    var animatorSet: AnimatorSet? = null

    constructor(context: Context, x: Int, y: Int, redPackageWidth: Int, redPackageHeight: Int, student: StudentsEntity, background: Bitmap, big: Boolean) {

        this.context = context

        this.title = student.name

        this.width = background.width

        //底部文字溢出长度
        val bottomPadding = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m30_0)

        //设置空的画布
        bitmap = Bitmap.createBitmap(
                background.width,
                background.height,
                Bitmap.Config.ARGB_4444)
        val canvas = Canvas(bitmap)
        //抗锯齿
        canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        //画背景
        canvas.drawBitmap(background, 0f, 0f, null)

        //描边文字
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
        val targetRect = Rect(0, 0, width, textRect.height())
        val fontMetrics = textPaint.fontMetricsInt
        val baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2
        val textStartY = bitmap.height - baseline.toFloat() + bottomPadding
        //写名字和描边
        canvas.drawText(student.name, targetRect.centerX().toFloat(), textStartY, strokeTextPaint)
        canvas.drawText(student.name, targetRect.centerX().toFloat(), textStartY, textPaint)

        //计算初始位置
        this.x = x + (redPackageWidth - this.width) / 2
        this.y = y - (bitmap.height - redPackageHeight)

        //开始下落动画
        startFall()

        //头像宽高
        var avatarWidth = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m190_0)
        if (!TextUtils.isEmpty(student.head)) {
            try {
                GlideApp.with(context)
                        .asBitmap()
                        .load(student.head)
                        .override(avatarWidth, avatarWidth)
                        .transform(CropCircleTransformation())
                        .into(object : SimpleTarget<Bitmap>() {

                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                //重新绘制一张空图
                                var bg = Bitmap.createBitmap(
                                        bitmap.width,
                                        bitmap.height,
                                        Bitmap.Config.ARGB_4444)
                                val canvas = Canvas(bg)
                                //抗锯齿
                                canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
                                //画背景
                                canvas.drawBitmap(bitmap, 0F, 0F, null)
                                // 设置X，Y
                                val matrix = Matrix()
                                val bottomPadding = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m56_0) + avatarWidth
                                matrix.postTranslate((bg.width.toFloat() - avatarWidth.toFloat()) / 2F, (bg.height - bottomPadding).toFloat())
                                //画头像
                                canvas.drawBitmap(resource, matrix, null)
                                //释放原图
                                bitmap.recycle()
                                //设置有头像的图
                                bitmap = bg
                            }
                        })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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