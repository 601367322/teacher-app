package com.prance.teacher.features.pk.rocket

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.prance.lib.common.utils.GlideApp
import com.prance.teacher.R
import java.util.*

class PKAnimManager(var context: Context) {

    //小火箭
    var littleRocket: Bitmap = GlideApp.with(context)
            .asBitmap()
            .load(R.drawable.little_rocket)
            .submit().get()

    //大火箭
    var bigRocket: Bitmap = GlideApp.with(context)
            .asBitmap()
            .load(R.drawable.big_rocket)
            .submit().get()

    //所有火箭
    var rockets = mutableListOf<BaseRocket>()

    //倒计时
    var countTime: CountTime = CountTime(context)

    //背景
    var background: Bitmap =  GlideApp.with(context)
            .asBitmap()
            .load(R.drawable.rank_background)
            .submit().get()

    init {
        //初始化小火箭
        rockets.add(LittleRocket(PointF(80F, 700F),littleRocket.width / 2F, littleRocket))
        rockets.add(LittleRocket(PointF(260F, 500F),littleRocket.width / 2F, littleRocket))
        rockets.add(LittleRocket(PointF(420F, 650F),littleRocket.width / 2F, littleRocket))
        rockets.add(LittleRocket(PointF(1320F, 400F),littleRocket.width / 2F, littleRocket))
        rockets.add(LittleRocket(PointF(1500F, 700F),littleRocket.width / 2F, littleRocket))
        rockets.add(LittleRocket(PointF(1700F, 500F),littleRocket.width / 2F, littleRocket))
        //初始化大火箭
        rockets.add(BigRocket(PointF(1000F, 500F),littleRocket.width / 2F, bigRocket))
    }


}