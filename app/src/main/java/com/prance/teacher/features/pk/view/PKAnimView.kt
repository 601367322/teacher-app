package com.prance.teacher.features.pk.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chillingvan.canvasgl.ICanvasGL
import com.chillingvan.canvasgl.glcanvas.GLPaint
import com.chillingvan.canvasgl.glview.GLContinuousView
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.teacher.R
import com.prance.teacher.features.pk.rocket.BaseRocket
import com.prance.teacher.features.pk.rocket.CollisionListener
import com.prance.teacher.features.pk.rocket.CountTime
import com.prance.teacher.features.pk.rocket.PKAnimManager
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import java.nio.Buffer
import java.util.ArrayList

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
                    mPKAnimManager?.countTime?.listener = countTimeListener
                    //开始倒计时
                    mPKAnimManager?.countTime?.start()
                }
    }

    override fun onGLDraw(canvas: ICanvasGL) {
        mPKAnimManager?.run {

            //画背景
            background.let {
                canvas.save()
                canvas.drawBitmap(background, 0, 0)
                canvas.restore()
            }

            //画火箭
            for (rocket in rockets) {
                rocket.draw(canvas)
            }

            //倒计时
            countTime.draw(canvas)
        }
    }

}