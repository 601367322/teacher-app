package com.prance.teacher.features.subject.view.danmu

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prance.lib.common.utils.GlideApp
import com.prance.teacher.R
import java.util.concurrent.CopyOnWriteArrayList

class DanmuManager(var context: Context) {

    var rockets: MutableList<Bitmap> = mutableListOf()

    var avatarBg = GlideApp.with(context)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .load(R.drawable.danmu_avatar_bg)
            .submit()
            .get()

    var danmus: CopyOnWriteArrayList<Danmu> = CopyOnWriteArrayList()

    init {

        val rocketsRes = mutableListOf(
                R.drawable.danmu_rocket_00000,
                R.drawable.danmu_rocket_00001,
                R.drawable.danmu_rocket_00002,
                R.drawable.danmu_rocket_00003,
                R.drawable.danmu_rocket_00004,
                R.drawable.danmu_rocket_00005,
                R.drawable.danmu_rocket_00006,
                R.drawable.danmu_rocket_00007
        )

        for (r in rocketsRes) {
            rockets.add(GlideApp.with(context)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .load(r)
                    .submit()
                    .get())
        }
    }

    fun add(avatar: Bitmap, name: String) {
        danmus.add(Danmu(context, avatar, name, rockets, avatarBg))
    }
}