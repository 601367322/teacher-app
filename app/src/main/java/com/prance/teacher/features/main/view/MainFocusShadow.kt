package com.prance.teacher.features.main.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import com.prance.teacher.R
import android.graphics.BlurMaskFilter





class MainFocusShadow : View {

    private var mShadowWidth: Float = 0F
    private var mRadius: Float = 0F

    constructor(context: Context) : super(context) {
        mShadowWidth = resources.getDimensionPixelOffset(R.dimen.m30_0).toFloat()
        mRadius = resources.getDimensionPixelOffset(R.dimen.m26_0).toFloat()

        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.RED
        paint.maskFilter = BlurMaskFilter((measuredWidth).toFloat(), BlurMaskFilter.Blur.OUTER)

        val width = measuredWidth
        val height = measuredHeight

        canvas.drawRoundRect(
                RectF(0F - mRadius, 0F-mRadius, width.toFloat()+mRadius, height.toFloat()+mRadius),
                mRadius,
                mRadius,
                paint)

        super.onDraw(canvas)
    }
}