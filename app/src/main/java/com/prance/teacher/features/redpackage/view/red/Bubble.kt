package com.prance.teacher.features.redpackage.view.red

import android.graphics.Bitmap
import com.prance.lib.common.utils.http.mySubscribe
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class Bubble(val bubbles: MutableList<Bitmap>) {

    var position = 0

    var bubble = bubbles[position]

    var disposable: Disposable? = null

    fun startAnim() {
        if (bubbles.size > 1)
            disposable = Flowable.interval(110, TimeUnit.MILLISECONDS)
                    .mySubscribe {
                        bubble = bubbles[position]
                        position++
                        if (position == bubbles.size) {
                            position = 0
                        }
                    }
    }

    fun destroy() {
        disposable?.dispose()
    }

}