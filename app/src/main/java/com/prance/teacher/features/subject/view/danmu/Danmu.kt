package com.prance.teacher.features.subject.view.danmu

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chillingvan.canvasgl.ICanvasGL
import com.prance.lib.common.utils.GlideApp
import com.prance.teacher.R
import jp.wasabeef.glide.transformations.CropCircleTransformation
import java.util.*

class Danmu(var context: Context, var head: String?, var name: String, var light: Bitmap, background: Bitmap, var toX: Float, var toY: Float) {

    var bitmap: Bitmap? = null
    var x = 0f
    var y = 0f

    var lightRotate = 0f

    var lightAnim: ValueAnimator? = null

    var xd = 0f
    var yd = 0f

    val avatarHeight = context.resources.getDimensionPixelOffset(R.dimen.m92_0)

    init {

        xd = (light.width.toFloat() - background.width.toFloat()) / 2f
        yd = (light.height.toFloat() - background.height.toFloat()) / 2f

        var tempBitmap = background

        //重新绘制一张空图
        var bg = Bitmap.createBitmap(
                tempBitmap.width,
                tempBitmap.height,
                Bitmap.Config.ARGB_4444)
        val canvas = Canvas(bg)
        //抗锯齿
        canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        //画背景
        canvas.drawBitmap(tempBitmap, 0f, 0f, null)

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.color = Color.WHITE
        textPaint.textSize = context.resources.getDimensionPixelOffset(R.dimen.m24_0).toFloat()
        val textRect2 = Rect()
        textPaint.getTextBounds(name, 0, name.length, textRect2)
        val xd = (background.width.toFloat() - textRect2.width().toFloat()) / 2f
        canvas.drawText(name, xd, background.height.toFloat() - context.resources.getDimensionPixelOffset(R.dimen.m44_0), textPaint)

        this.bitmap = bg

        GlideApp.with(context)
                .asBitmap()
                .load(head)
                .error(R.drawable.danmu_default_avatar)
                .override(avatarHeight, avatarHeight)
                .transform(CropCircleTransformation())
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        showDanmu(resource)
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        errorDrawable?.let { showDanmu((it as BitmapDrawable).bitmap) }
                    }

                    var inited = false

                    private fun showDanmu(resource: Bitmap) {
                        if (inited) {
                            return
                        }
                        inited = true
                        try {
                            var xd = (background.width.toFloat() - resource.width.toFloat()) / 2f
                            //绘制头像
                            var tempBitmap = bitmap

                            tempBitmap?.run {
                                //重新绘制一张空图
                                var bg = Bitmap.createBitmap(
                                        tempBitmap.width,
                                        tempBitmap.height,
                                        Bitmap.Config.ARGB_4444)
                                val canvas = Canvas(bg)
                                //抗锯齿
                                canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
                                //画背景
                                canvas.drawBitmap(tempBitmap, 0f, 0f, null)
                                //画头像
                                canvas.drawBitmap(resource, xd, context.resources.getDimensionPixelOffset(R.dimen.m24_0).toFloat(), null)
                                //设置有头像的图
                                bitmap = bg
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })

        startLightAnim()

        startTranslateAnim()
    }

    private fun startLightAnim() {
        lightAnim = ObjectAnimator.ofFloat(0f, 360f)
        lightAnim?.run {
            addUpdateListener {
                lightRotate = it.animatedValue.toString().toFloat()
            }
            duration = 500
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
            repeatMode = ValueAnimator.RESTART
            start()
        }
    }

    fun destroy() {
        lightAnim?.cancel()
    }

    private fun startTranslateAnim() {

        var border = context.resources.getDimensionPixelOffset(R.dimen.m200_0)

        x = getRandom(0, ScreenUtils.getScreenWidth() - light.width).toFloat()
        y = getRandom(border, ScreenUtils.getScreenHeight() - light.width).toFloat()

        var toXAnim = ObjectAnimator.ofFloat(x, toX - light.width.toFloat() / 2)
        toXAnim.addUpdateListener {
            x = it.animatedValue.toString().toFloat()
        }
        var toYAnim = ObjectAnimator.ofFloat(y, toY - light.height.toFloat() / 2)
        toYAnim.addUpdateListener {
            y = it.animatedValue.toString().toFloat()
        }

        var transAnimSet = AnimatorSet()
        transAnimSet.playTogether(toXAnim, toYAnim)
        transAnimSet.start()

    }

    fun getRandom(min: Int, max: Int): Int {
        val random = Random()
        val s = random.nextInt(max) % (max - min + 1) + min
        return s
    }


    fun onDraw(canvas: ICanvasGL) {
        canvas.save()

        bitmap?.run {
            if (!isRecycled) {
                val matrix = ICanvasGL.BitmapMatrix()
                matrix.rotateZ(lightRotate)
                matrix.translate(x, y)
                canvas.drawBitmap(light, matrix)

                canvas.drawBitmap(this, (x + xd).toInt(), (y + yd + context.resources.getDimensionPixelOffset(R.dimen.m30_0)).toInt())
            }
        }

        canvas.restore()
    }

}
