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

class LittleRocket(val context: Context,
                   startPoint: PointF,
                   targetPoint: PointF,
                   bitmap: Bitmap) : BaseRocket(startPoint, targetPoint, bitmap) {

    var disposable: Disposable? = null

    var runningArray: MutableList<Bitmap>? = null

    var position = 0

    init {
        Flowable
                .create<MutableList<Bitmap>>({
                    val runningRes = mutableListOf(
                            R.drawable.little_rocket_running_1,
                            R.drawable.little_rocket_running_2,
                            R.drawable.little_rocket_running_3
                    )
                    val runningArray = mutableListOf<Bitmap>()
                    for (i in runningRes) {
                        runningArray.add(GlideApp.with(context).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).load(i).submit().get())
                    }
                    it.onNext(runningArray)
                    it.onComplete()
                }, BackpressureStrategy.BUFFER)
                .mySubscribe {
                    this.runningArray = it
                }


    }

    fun starRunning() {
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