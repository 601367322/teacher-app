package com.prance.teacher.features.pk.rocket

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.PointF
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chillingvan.canvasgl.ICanvasGL
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.teacher.R
import com.prance.teacher.features.pk.rocket.BaseRocket.Companion.INTERNVAL_TIME_MS
import com.prance.teacher.features.pk.view.ICountTimeListener
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

class PKBackground(val context: Context) : ICountTimeListener {

    var backgroundArray = mutableListOf<Bitmap>()

    var points: MutableList<PointF> = mutableListOf()

    var disposable: Disposable? = null

    var shouldUpdatePosition = false

    init {
        val backgroundRes = mutableListOf(
                R.drawable.pk_background_0001,
                R.drawable.pk_background_0002,
                R.drawable.pk_background_0003
        )

        for ((index, i) in backgroundRes.withIndex()) {
            backgroundArray.add(GlideApp.with(context).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).load(i).submit(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight()).get())
            points.add(PointF(0F, -index.toFloat() * ScreenUtils.getScreenHeight()))
        }
    }

    fun draw(canvas: ICanvasGL) {
        canvas.save()
        for ((index, i) in backgroundArray.withIndex()) {
            val point = points[index]
            canvas.drawBitmap(i, point.x.toInt(), point.y.toInt())
        }
        if (shouldUpdatePosition)
            updatePosition(INTERNVAL_TIME_MS)
        canvas.restore()
    }

    fun startAnim() {
//        disposable = Flowable.interval(5, TimeUnit.MILLISECONDS)
//                .mySubscribe {
//                    val y = 1
//                    for ((index, p) in points.withIndex()) {
//                        p.y += y
//                        if (p.y >= ScreenUtils.getScreenHeight()) {
//                            p.y = -2 * ScreenUtils.getScreenHeight()
//                        }
//                    }
//                    if ((context as Activity).isDestroyed) {
//                        disposable?.dispose()
//                    }
//                }
    }

    override fun onTimeCountEnd() {
        shouldUpdatePosition = true
    }

    val VY_MULTIPLIER = 0.01f // px/ms
    val MIN_VY = 20

    fun updatePosition(timeMs: Int) {

        val y = MIN_VY * VY_MULTIPLIER * timeMs
        for ((index, p) in points.withIndex()) {
            p.y += y

            if (p.y >= ScreenUtils.getScreenHeight()) {
                p.y = (-2 * ScreenUtils.getScreenHeight()).toFloat()
                p.y += y
            }
        }
        EventBus.getDefault().post(this)
    }
}