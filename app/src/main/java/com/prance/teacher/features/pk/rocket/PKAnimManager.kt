package com.prance.teacher.features.pk.rocket

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.teacher.R
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

class PKAnimManager(var context: Context) {

    //默认背景
    var defaultBackground = GlideApp.with(context).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).load(R.drawable.pk_background_0001).submit().get()

    //所有火箭
    var rockets: MutableList<BaseRocket>? = null

    fun getDimen(id: Int): Float {
        return context.resources.getDimensionPixelOffset(id).toFloat()
    }

    //倒计时
    var countTime: PKCountTime = PKCountTime(context)

    //星球
    var earth: Earth? = null

    //移动背景
    var pkBackground: PKBackground? = null

    init {

        Flowable
                .create<MutableList<BaseRocket>>({
                    earth = Earth(context)
                    countTime.listeners.add(earth!!)

                    pkBackground = PKBackground(context)
                    countTime.listeners.add(pkBackground!!)

                    //小火箭
                    var littleRocket: Bitmap = GlideApp.with(context)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .load(R.drawable.little_rocket_stop)
                            .submit().get()

                    //大火箭
                    var bigRocket: Bitmap = GlideApp.with(context)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .load(R.drawable.big_rocket_shake_00000)
                            .submit().get()

                    it.onNext(mutableListOf(
                            LittleRocket(context, PointF(getDimen(R.dimen.m224_0), getDimen(R.dimen.m862_0)), PointF(getDimen(R.dimen.m224_0), getDimen(R.dimen.m220_0)), littleRocket),
                            LittleRocket(context, PointF(getDimen(R.dimen.m386_0), getDimen(R.dimen.m823_0)), PointF(getDimen(R.dimen.m386_0), getDimen(R.dimen.m602_0)), littleRocket),
                            LittleRocket(context, PointF(getDimen(R.dimen.m542_0), getDimen(R.dimen.m807_0)), PointF(getDimen(R.dimen.m542_0), getDimen(R.dimen.m460_0)), littleRocket),
                            LittleRocket(context, PointF(getDimen(R.dimen.m1186_0), getDimen(R.dimen.m812_0)), PointF(getDimen(R.dimen.m1186_0), getDimen(R.dimen.m260_0)), littleRocket),
                            LittleRocket(context, PointF(getDimen(R.dimen.m1346_0), getDimen(R.dimen.m823_0)), PointF(getDimen(R.dimen.m1346_0), getDimen(R.dimen.m682_0)), littleRocket),
                            LittleRocket(context, PointF(getDimen(R.dimen.m1508_0), getDimen(R.dimen.m862_0)), PointF(getDimen(R.dimen.m1508_0), getDimen(R.dimen.m480_0)), littleRocket),
                            BigRocket(context, PointF(getDimen(R.dimen.m743_0), getDimen(R.dimen.m580_0)), PointF(getDimen(R.dimen.m743_0), getDimen(R.dimen.m340_0)), bigRocket)
                    ))
                    it.onComplete()
                }, BackpressureStrategy.BUFFER)
                .mySubscribe {
                    rockets = it
                    rockets?.run {
                        for (i in this) {
                            countTime.listeners.add(i)
                        }
                    }
                }

    }


    fun clear() {
        rockets?.clear()
    }
}