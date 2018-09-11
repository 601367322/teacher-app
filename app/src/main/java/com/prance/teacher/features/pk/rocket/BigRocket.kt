package com.prance.teacher.features.pk.rocket

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.teacher.R
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class BigRocket(val context: Context,
                startPoint: PointF,
                targetPoint: PointF,
                bitmap: Bitmap) : BaseRocket(startPoint, targetPoint, bitmap) {

    var disposable: Disposable? = null

    var shakeArray: MutableList<Bitmap>? = null

    var runningArray: MutableList<Bitmap>? = null

    var position = 0

    var running = false

    init {

        Flowable
                .create<Any>({
                    val shakeRes = mutableListOf(
                            R.drawable.big_rocket_shake_00000,
                            R.drawable.big_rocket_shake_00001,
                            R.drawable.big_rocket_shake_00002,
                            R.drawable.big_rocket_shake_00003,
                            R.drawable.big_rocket_shake_00004,
                            R.drawable.big_rocket_shake_00005,
                            R.drawable.big_rocket_shake_00006,
                            R.drawable.big_rocket_shake_00007
                    )

                    val runningRes = mutableListOf(
                            R.drawable.big_rocket_running_1,
                            R.drawable.big_rocket_running_2,
                            R.drawable.big_rocket_running_3
                    )

                    val width = context.resources.getDimensionPixelOffset(R.dimen.m413_0)
                    val height = context.resources.getDimensionPixelOffset(R.dimen.m556_0)

                    shakeArray = mutableListOf()
                    for (i in shakeRes) {
                        shakeArray!!.add(GlideApp.with(context).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).load(i).submit(width, height).get())
                    }

                    runningArray = mutableListOf()
                    for (i in runningRes) {
                        runningArray!!.add(GlideApp.with(context).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).load(i).submit(width, height).get())
                    }
                    it.onNext("")
                    it.onComplete()
                }, BackpressureStrategy.BUFFER)
                .mySubscribe {
                    starShake()
                }
    }

    fun starShake() {
        disposable = Flowable.interval(142, TimeUnit.MILLISECONDS)
                .mySubscribe {
                    shakeArray?.run {

                        position++
                        if (position >= this.size) {
                            position = 0
                        }
                        super.bitmap = this[position]
                    }
                }
    }

    fun starRunning() {
        disposable?.run {
            dispose()
        }
        position = 0
        disposable = Flowable.interval(125, TimeUnit.MILLISECONDS)
                .mySubscribe {
                    runningArray?.run {

                        position++
                        if (position >= this.size) {
                            position = 0
                        }
                        super.bitmap = this[position]
                    }

                    //结束
                    if ((context as Activity).isDestroyed) {
                        disposable?.dispose()
                    }
                }
    }

    override fun onTimeCountEnd() {
        super.onTimeCountEnd()
        starRunning()
    }
}