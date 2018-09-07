package com.prance.teacher.features.pk.rocket

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.teacher.R
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class LittleRocket(val context: Context,
                   startPoint: PointF,
                   targetPoint: PointF,
                   bitmap: Bitmap) : BaseRocket(startPoint, targetPoint, bitmap) {

    var disposable: Disposable? = null


    var runningArray = mutableListOf<Bitmap>()

    var position = 0

    init {

        var runningRes = mutableListOf(
                R.drawable.little_rocket_running_1,
                R.drawable.little_rocket_running_2,
                R.drawable.little_rocket_running_3
        )

        for (i in runningRes) {
            runningArray.add(GlideApp.with(context).asBitmap().load(i).submit().get())
        }
    }

    fun starRunning() {
        position = 0
        disposable = Flowable.interval(125, TimeUnit.MILLISECONDS)
                .mySubscribe {
                    position++
                    if (position >= runningArray.size) {
                        position = 0
                    }
                    super.bitmap = runningArray[position]

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