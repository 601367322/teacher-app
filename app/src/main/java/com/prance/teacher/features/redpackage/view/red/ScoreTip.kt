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
import com.prance.teacher.features.students.model.StudentEntity
import com.prance.teacher.weight.FontCustom
import android.graphics.PaintFlagsDrawFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import com.bumptech.glide.request.target.SimpleTarget
import jp.wasabeef.glide.transformations.CropCircleTransformation


class ScoreTip {

    var context: Context

    //向上消失距离
    var translationDistance: Int = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m40_0)

    //位置
    var x: Int = 0
    var y: Int = 0

    //透明度
    var alpha: Int = RedPackageManager.DEFAULT_ALPHA

    //宽高
    var width: Int

    //标题
    var title: String

    var bitmap: Bitmap

    //红包积分数字图
    var scoreBitmaps: MutableMap<String, Bitmap> = mutableMapOf()

    //动画时长
    val translationDurationTime = 1000L

    //被抢的状态
    var state = RedPackageTipStatus.SHOW

    //动画集
    var animatorSet: AnimatorSet? = null

    constructor(context: Context, x: Int, y: Int, redPackageWidth: Int, redPackageHeight: Int, student: StudentEntity, score: Int, tipBitmap: Bitmap, scoreBitmaps: MutableMap<String, Bitmap>) {

        this.context = context

        this.title = student.name

        this.scoreBitmaps = scoreBitmaps

        //底部文字溢出长度
        val bottomPadding = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m45_0)

        //右部文字溢出长度
        val rightPadding = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m40_0)

        //红包背景378
        bitmap = Bitmap.createBitmap(
                tipBitmap.width + rightPadding * 2,
                tipBitmap.height + bottomPadding,
                Bitmap.Config.ARGB_4444)

        this.width = bitmap.width

        val canvas = Canvas(bitmap)
        //抗锯齿
        canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        //画背景
        canvas.drawBitmap(
                tipBitmap,
                (bitmap.width - tipBitmap.width).toFloat() / 2F,
                0F,
                null)

        //画积分
        val scores = convertTextToBitmap("+$score")
        var startX = bitmap.width - Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m86_0).toFloat()
        for (score in scores) {
            //积分图
            canvas.drawBitmap(score, startX, bitmap.height - Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m98_0).toFloat() - score.height, null)
            startX += Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m35_0)
        }

        //描边文字
        val strokeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        strokeTextPaint.color = Color.parseColor("#923D00")
        strokeTextPaint.textAlign = Paint.Align.CENTER
        strokeTextPaint.strokeWidth = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m3_0).toFloat()
        strokeTextPaint.style = Paint.Style.STROKE
        strokeTextPaint.typeface = FontCustom.getFZY1JWFont(Utils.getApp())
        strokeTextPaint.textSize = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m47_0).toFloat()

        //文字画笔
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
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
        val textStartY = bitmap.height - baseline.toFloat() + bottomPadding - Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m13_0).toFloat()
        //写名字和描边
        canvas.drawText(student.name, targetRect.centerX().toFloat(), textStartY, strokeTextPaint)
        canvas.drawText(student.name, targetRect.centerX().toFloat(), textStartY, textPaint)

        //计算初始位置
        this.x = x + (redPackageWidth - this.width) / 2
        this.y = y - (bitmap.height - redPackageHeight)

        //开始下落动画
        startFall()

        //头像宽高
        var avatarWidth = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m108_0)
        if (!TextUtils.isEmpty(student.head)) {
            try {
                GlideApp.with(context)
                        .asBitmap()
                        .load(student.head)
                        .placeholder(R.drawable.red_package_score_tip_default_avatar)
                        .error(R.drawable.red_package_score_tip_default_avatar)
                        .override(avatarWidth, avatarWidth)
                        .transform(CropCircleTransformation())
                        .into(object : SimpleTarget<Bitmap>() {

                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                draw(resource)
                            }

                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                super.onLoadFailed(errorDrawable)

                                draw((errorDrawable as BitmapDrawable).bitmap)
                            }

                            fun draw(resource: Bitmap) {
                                if (!bitmap.isRecycled) {
                                    var tempBitmap = bitmap
                                    //重新绘制一张空图
                                    var bg = Bitmap.createBitmap(
                                            tempBitmap.width,
                                            tempBitmap.height,
                                            Bitmap.Config.ARGB_4444)
                                    val canvas = Canvas(bg)
                                    //抗锯齿
                                    canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
                                    //画背景
                                    canvas.drawBitmap(tempBitmap, 0F, 0F, null)
                                    // 设置X，Y
                                    val matrix = Matrix()
                                    matrix.postScale(avatarWidth / resource.width.toFloat(), avatarWidth / resource.height.toFloat())
                                    matrix.postTranslate((bg.width.toFloat() - avatarWidth.toFloat()) / 2F - Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m4_0), (bitmap.height - bottomPadding - avatarWidth - Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m15_0)).toFloat())
                                    //画头像
                                    canvas.drawBitmap(resource, matrix, null)
                                    //设置有头像的图
                                    bitmap = bg
                                    //释放原图
                                    tempBitmap.recycle()
                                }
                            }
                        })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun convertTextToBitmap(text: String): MutableList<Bitmap> {
        val list = mutableListOf<Bitmap>()
        for (i in text) {
            list.add(this.scoreBitmaps[i.toString()]!!)
        }
        return list
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