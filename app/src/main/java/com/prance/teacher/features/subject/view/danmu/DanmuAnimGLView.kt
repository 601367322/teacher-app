package com.prance.teacher.features.subject.view.danmu

import android.content.Context
import android.util.AttributeSet
import com.chillingvan.canvasgl.ICanvasGL
import com.chillingvan.canvasgl.glview.GLContinuousView
import com.prance.lib.common.utils.http.mySubscribe
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

class DanmuAnimGLView : GLContinuousView {

    var mDanmuManager: DanmuManager? = null

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        //初始化资源
        Flowable
                .create<DanmuManager>({
                    it.onNext(DanmuManager(context))
                    it.onComplete()
                }, BackpressureStrategy.BUFFER)
                .mySubscribe {
                    mDanmuManager = it
                }
    }

    override fun onGLDraw(canvas: ICanvasGL) {
        try {
            mDanmuManager?.run {
                for (i in danmus) {
                    i.onDraw(canvas)
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mDanmuManager?.destory()
    }

}