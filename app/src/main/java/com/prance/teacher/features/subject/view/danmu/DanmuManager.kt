package com.prance.teacher.features.subject.view.danmu

import android.content.Context
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prance.lib.common.utils.GlideApp
import com.prance.teacher.R
import com.prance.teacher.features.students.model.StudentEntity
import java.util.concurrent.CopyOnWriteArrayList

class DanmuManager(var context: Context) {


    var danmus: CopyOnWriteArrayList<Danmu> = CopyOnWriteArrayList()

    var light =
            GlideApp.with(context)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .load(R.drawable.box_light)
                    .submit()
                    .get()

    var background = GlideApp.with(context)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .load(R.drawable.danmu_background)
            .submit()
            .get()

    init {

    }

    fun add(student: StudentEntity, toX: Float, toY: Float) {
        danmus.add(Danmu(context, student.head, student.name, light, background, toX, toY))
    }

    fun destory() {
        for (dan in danmus) {
            dan.destroy()
        }
    }
}