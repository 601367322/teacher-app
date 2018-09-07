package com.prance.teacher.features.pk.rocket

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.view.animation.DecelerateInterpolator
import com.chillingvan.canvasgl.ICanvasGL
import com.prance.lib.common.utils.GlideApp
import com.prance.teacher.R
import com.prance.teacher.features.pk.view.ICountTimeListener
import com.prance.teacher.utils.SoundUtils

/**
 * 倒计时
 *
 * @author 申兵兵
 * @date 2018-08-29
 */
class CountTime(var context: Context) {

    var alpha = 255
    var point: PointF = PointF()
    var scaleX: Float = 1F
    var scaleY: Float = 1F
    var num = 2
    var countDownTimerImg = mutableListOf<Bitmap>()
    var bitmap: Bitmap? = null
    var listeners: MutableList<ICountTimeListener> = mutableListOf()

    init {
        val width = context.resources.getDimensionPixelOffset(R.dimen.m100_0)

        for (t in mutableListOf(R.drawable.count_down_timer_1, R.drawable.count_down_timer_2, R.drawable.count_down_timer_3)) {
            countDownTimerImg.add(GlideApp.with(context)
                    .asBitmap()
                    .load(t)
                    .submit(width, width)
                    .get())
        }

        bitmap = countDownTimerImg[num]

        point.x = (context.resources.displayMetrics.widthPixels - width).toFloat() / 2F
        point.y = context.resources.getDimensionPixelOffset(R.dimen.m317_0).toFloat()

    }

    fun start() {
        createAnimate().start()

        SoundUtils.play("count_time")
    }

    private fun createAnimate(): AnimatorSet {

        val animatorA = ObjectAnimator.ofInt(255, 0)
        animatorA.addUpdateListener {
            alpha = it.animatedValue.toString().toInt()
        }

        val animatorX = ObjectAnimator.ofFloat(1F, 1.5F)
        animatorX.addUpdateListener {
            scaleX = it.animatedValue.toString().toFloat()
        }

        val animatorY = ObjectAnimator.ofFloat(1F, 1.5F)
        animatorY.addUpdateListener {
            scaleY = it.animatedValue.toString().toFloat()
        }

        val animationSet = AnimatorSet()
        animationSet.playTogether(animatorA, animatorX, animatorY)
        animationSet.interpolator = DecelerateInterpolator()
        animationSet.duration = 1000

        animationSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                if (num == 0) {
                    bitmap = null
                    for (l in listeners) {
                        l.onTimeCountEnd()
                    }
                } else {
                    num--
                    bitmap = countDownTimerImg[num]
                    createAnimate().start()
                }
            }
        })
        return animationSet
    }

    fun draw(canvas: ICanvasGL) {
        bitmap?.run {
            canvas.save()

            //渐变消失效果
            canvas.setAlpha(alpha)

            //放大效果
            val matrix = ICanvasGL.BitmapMatrix()
            matrix.scale(scaleX, scaleY)
            val scaleDiff = getScalePointDiff(this, scaleX)
            matrix.translate(point.x - scaleDiff, point.y - scaleDiff)

            canvas.drawBitmap(this, matrix)

            canvas.restore()
        }
    }

    private fun getScalePointDiff(bitmap: Bitmap, scale: Float): Float {
        return (bitmap.width * scale - bitmap.width) / 2
    }

}