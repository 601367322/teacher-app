package com.prance.lib.common.utils.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import com.prance.lib.common.R

class ButtonBackground : View {

    private var mShadowWidth: Float = 0F
    private var mRadius: Float = 0F

    constructor(context: Context) : super(context) {
        mShadowWidth = resources.getDimensionPixelOffset(R.dimen.m9_0).toFloat()
        mRadius = resources.getDimensionPixelOffset(R.dimen.m8_0).toFloat()

        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        val paint = Paint()
        paint.color = Color.parseColor("#FFFFFF")
        paint.isAntiAlias = true
        paint.setShadowLayer(mShadowWidth, 0F, 0F, Color.parseColor("#1567B3"))
        val width = measuredWidth
        val height = measuredHeight
        val startLeft = mShadowWidth
        val startTop = mShadowWidth
        canvas?.drawRoundRect(RectF(startLeft, startTop, width - startLeft, height - startTop), mRadius, mRadius, paint)
        super.onDraw(canvas)
    }
}