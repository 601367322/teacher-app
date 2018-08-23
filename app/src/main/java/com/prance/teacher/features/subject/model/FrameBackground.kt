package com.prance.teacher.features.subject.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.prance.lib.common.utils.GlideApp
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class FrameBackground {

    var list: Array<Bitmap?>

    var position: Int = 0
    var bitmap: Bitmap? = null

    var disposable: Disposable? = null

    constructor(context: Context, list: MutableList<Int>) {
        this.list = arrayOfNulls(list.size)
        for ((index, i) in list.withIndex()) {
            GlideApp.with(context)
                    .asBitmap()
                    .load(i)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            LogUtils.d(index)
                            this@FrameBackground.list[index] = resource
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            super.onLoadFailed(errorDrawable)
                            LogUtils.d(index)
                        }
                    })
        }
    }

    fun start() {
        disposable = Flowable.interval(120, TimeUnit.MILLISECONDS)
                .subscribe {
                    if (position < list.size) {
                        bitmap = list[position]
                        position++
                        if (position >= list.size) {
                            position = 0
                        }
                    }
                }
    }

    fun stop() {
        disposable?.dispose()
    }
}