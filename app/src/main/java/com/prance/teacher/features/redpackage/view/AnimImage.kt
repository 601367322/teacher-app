package com.prance.teacher.features.redpackage.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.graphics.drawable.AnimationDrawable
import com.prance.teacher.R


class AnimImage:ImageView{

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val animationDrawable = getDrawable() as AnimationDrawable
        animationDrawable.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        val animationDrawable = getDrawable() as AnimationDrawable
        animationDrawable.stop()
    }
}