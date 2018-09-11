package com.prance.teacher.features.pk.view

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chillingvan.canvasgl.ICanvasGL
import com.chillingvan.canvasgl.glview.GLContinuousView
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.teacher.R
import com.prance.teacher.features.pk.rocket.PKAnimManager
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

class PKAnimView(context: Context, attrs: AttributeSet?) : GLContinuousView(context, attrs) {


    private var mPKAnimManager: PKAnimManager? = null

    var countTimeListener: ICountTimeListener? = null

    init {
        setZOrderMediaOverlay(true)

        //初始化资源
        Flowable
                .create<PKAnimManager>({
                    it.onNext(PKAnimManager(context))
                    it.onComplete()
                }, BackpressureStrategy.BUFFER)
                .mySubscribe {
                    mPKAnimManager = it
                    //设置倒计时监听
                    countTimeListener?.let {
                        mPKAnimManager?.countTime?.listeners?.add(it)
                    }
                    //开始倒计时
                    mPKAnimManager?.countTime?.start()
                }
    }

    override fun onGLDraw(canvas: ICanvasGL) {
        mPKAnimManager?.run {

            if (pkBackground == null) {
                canvas.save()
                canvas.drawBitmap(defaultBackground, 0, 0)
                canvas.restore()
            }

            //画背景
            pkBackground?.draw(canvas)

            earth?.let {
                it.draw(canvas)
            }

            //画火箭
            rockets?.run {
                for (rocket in this) {
                    rocket.draw(canvas)
                }
            }

            //倒计时
            countTime.draw(canvas)

        }
    }

}