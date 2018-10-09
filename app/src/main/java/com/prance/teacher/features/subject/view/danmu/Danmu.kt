package com.prance.teacher.features.subject.view.danmu

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.Utils
import com.chillingvan.canvasgl.ICanvasGL
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.view.red.RedPackageManager
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class Danmu(var context: Context, var avatar: Bitmap, var name: String, var rockets: MutableList<Bitmap>, avatarBg: Bitmap) {

    var avatarBitmap: Bitmap? = null
    var rocket: Bitmap

    var x = 0

    init {
        rocket = rockets[0]

        val avatarBgWidth = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m49_0)
        val paddingLeft = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m20_0)
        val paddingRight = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m20_0)
        val height = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m60_0)
        val avatarRightMargin = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m15_0)
        val textSize = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m30_0)
        val bgRadius = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m4_0)
        val borderWidth = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m3_0)
        val avatarBorderWidth = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m6_0)
        val avatarWidth = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m36_0)
        val textMargin = Utils.getApp().resources.getDimensionPixelOffset(R.dimen.m5_0)

        val normalTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        normalTextPaint.color = Color.WHITE
        normalTextPaint.textSize = textSize.toFloat()

        val highLightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        highLightPaint.color = Color.parseColor("#FFB400")
        highLightPaint.textSize = textSize.toFloat()

        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint.color = Color.parseColor("#6E78D7")
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bgPaint.color = Color.parseColor("#374090")

        val text1 = "恭喜"
        val text2 = name.trim()
        val text3 = "答题正确"
        val text = "$text1$text2$text3"

        //计算文字宽高
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.textSize = textSize.toFloat()
        val textRect = Rect()
        textPaint.getTextBounds(text, 0, text.length, textRect)
        val textRect1 = Rect()
        textPaint.getTextBounds(text1, 0, text1.length, textRect1)
        val textRect2 = Rect()
        textPaint.getTextBounds(text2, 0, text2.length, textRect2)

        val bitmap = Bitmap.createBitmap(paddingLeft + avatarBgWidth + avatarRightMargin + textRect.width() + paddingRight, height, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(bitmap)

        //绘制边框背景
        canvas.drawRoundRect(0F, 0F, bitmap.width.toFloat(), bitmap.height.toFloat(), bgRadius.toFloat(), bgRadius.toFloat(), borderPaint)
        canvas.drawRoundRect(borderWidth.toFloat(), borderWidth.toFloat(), bitmap.width.toFloat() - borderWidth, bitmap.height.toFloat() - borderWidth, bgRadius.toFloat(), bgRadius.toFloat(), bgPaint)

        //绘制头像背景
        canvas.drawBitmap(avatarBg, paddingLeft.toFloat(), (height - avatarBgWidth) / 2f, null)
        //绘制头像
        val matrix = Matrix()
        matrix.postScale(avatarWidth / avatar.width.toFloat(), avatarWidth / avatar.height.toFloat())
        matrix.postTranslate(paddingLeft.toFloat() + avatarBorderWidth, (height - avatarBgWidth) / 2f + avatarBorderWidth)
        canvas.drawBitmap(avatar, matrix, null)

        //绘制文字
        canvas.drawText(text1, paddingLeft + avatarBgWidth + avatarRightMargin.toFloat(), textRect.height().toFloat() + (height - textRect.height().toFloat()) / 2f - borderWidth, normalTextPaint)
        canvas.drawText(text2, paddingLeft + avatarBgWidth + avatarRightMargin.toFloat() + textRect1.width() + textMargin, textRect.height().toFloat() + (height - textRect.height().toFloat()) / 2f - borderWidth, highLightPaint)
        canvas.drawText(text3, paddingLeft + avatarBgWidth + avatarRightMargin.toFloat() + textRect1.width() + textMargin + textRect2.width() + textMargin, textRect.height().toFloat() + (height - textRect.height().toFloat()) / 2f - borderWidth, normalTextPaint)

        avatarBitmap = bitmap

        starRocketAnim()

        startRunning()
    }

    var disposable: Disposable? = null
    var position = 0

    private fun starRocketAnim() {
        disposable?.run {
            dispose()
        }
        position = 0
        disposable = Flowable.interval(125, TimeUnit.MILLISECONDS)
                .mySubscribe {

                    position++
                    if (position >= rockets.size) {
                        position = 0
                    }
                    this.rocket = rockets[position]

                    //结束
                    if ((context as Activity).isDestroyed) {
                        disposable?.dispose()
                    }
                }
    }

    private fun startRunning() {
        val translationAnimator = ObjectAnimator.ofInt(ScreenUtils.getScreenWidth(), -rocket.width - avatarBitmap!!.width).setDuration(8 * 1000)
        translationAnimator!!.interpolator = LinearInterpolator()
        translationAnimator.addUpdateListener {
            x = it.animatedValue.toString().toInt()
        }
        translationAnimator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                disposable?.dispose()

                avatarBitmap?.recycle()
                avatarBitmap = null
            }
        })
        translationAnimator.start()
    }

    fun onDraw(canvas: ICanvasGL) {

        canvas.drawBitmap(rocket, x, 0)

        avatarBitmap?.let {
            canvas.drawBitmap(it, x + rocket.width, rocket.height - it.height)
        }
    }
}