package com.prance.teacher.features.pk.rocket

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import com.blankj.utilcode.util.ScreenUtils
import com.chillingvan.canvasgl.ICanvasGL
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.teacher.R
import com.prance.teacher.features.pk.view.ICountTimeListener
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

class Earth(val context: Context) :ICountTimeListener{

    var bitmap: Bitmap = GlideApp.with(context)
            .asBitmap()
            .load(R.drawable.rank_earth_background)
            .submit().get()

    var screenHeight = ScreenUtils.getScreenHeight()

    var point = Point(0, screenHeight - bitmap.height)

    fun draw(canvas: ICanvasGL) {
        canvas.save()
        canvas.drawBitmap(bitmap, 0, point.y)
        canvas.restore()

    }

    override fun onTimeCountEnd() {
        startAnim()
    }

    fun startAnim() {
        val anim = ObjectAnimator.ofInt(bitmap.height, 0)
        anim.addUpdateListener {
            point.y = screenHeight - it.animatedValue.toString().toInt()
        }
        anim.start()
    }
}