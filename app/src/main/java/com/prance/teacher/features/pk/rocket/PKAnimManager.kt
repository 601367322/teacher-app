package com.prance.teacher.features.pk.rocket

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prance.lib.common.utils.GlideApp
import com.prance.teacher.R

class PKAnimManager(var context: Context) {

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

    //所有火箭
    var rockets = mutableListOf(
            LittleRocket(context, PointF(getDimen(R.dimen.m224_0), getDimen(R.dimen.m862_0)), PointF(getDimen(R.dimen.m224_0), getDimen(R.dimen.m220_0)), littleRocket),
            LittleRocket(context, PointF(getDimen(R.dimen.m386_0), getDimen(R.dimen.m823_0)), PointF(getDimen(R.dimen.m386_0), getDimen(R.dimen.m602_0)), littleRocket),
            LittleRocket(context, PointF(getDimen(R.dimen.m542_0), getDimen(R.dimen.m807_0)), PointF(getDimen(R.dimen.m542_0), getDimen(R.dimen.m460_0)), littleRocket),
            LittleRocket(context, PointF(getDimen(R.dimen.m1186_0), getDimen(R.dimen.m812_0)), PointF(getDimen(R.dimen.m1186_0), getDimen(R.dimen.m260_0)), littleRocket),
            LittleRocket(context, PointF(getDimen(R.dimen.m1346_0), getDimen(R.dimen.m823_0)), PointF(getDimen(R.dimen.m1346_0), getDimen(R.dimen.m682_0)), littleRocket),
            LittleRocket(context, PointF(getDimen(R.dimen.m1508_0), getDimen(R.dimen.m862_0)), PointF(getDimen(R.dimen.m1508_0), getDimen(R.dimen.m480_0)), littleRocket),
            BigRocket(context, PointF(getDimen(R.dimen.m743_0), getDimen(R.dimen.m580_0)), PointF(getDimen(R.dimen.m743_0), getDimen(R.dimen.m340_0)), bigRocket)
    )

    fun getDimen(id: Int): Float {
        return context.resources.getDimensionPixelOffset(id).toFloat()
    }

    //倒计时
    var countTime: CountTime = CountTime(context)

    //星球
    var earth = Earth(context)

    //移动背景
    var pkBackground: PKBackground = PKBackground(context)

    init {
        countTime.listeners.add(earth)

        for (i in rockets) {
            countTime.listeners.add(i)
        }

        countTime.listeners.add(pkBackground)
    }


}