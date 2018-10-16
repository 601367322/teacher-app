package com.prance.teacher.weight

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.prance.lib.common.utils.weight.FocusRelativeLayout
import com.prance.teacher.R

class GreenBorderFocusPaddingRelativeLayout : FocusRelativeLayout {

    internal var i = 0

    var paddingH = resources.getDimensionPixelOffset(R.dimen.m12_0)
    var paddingV = resources.getDimensionPixelOffset(R.dimen.m10_0)

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun init() {
        super.init()

        mDrawable = resources.getDrawable(R.drawable.key_pad_focus_green_border)
    }

    override fun onDraw(canvas: Canvas) {
        val mRect = Rect()
        super.getDrawingRect(mRect)
        super.onDraw(canvas)
        if (hasFocus()) {
            //画阴影
            val drawablePadding = Rect()
            mDrawable.getPadding(drawablePadding)
            var shadowBound = Rect(
                    (mRect.left - drawablePadding.left - paddingH),
                    (mRect.top - drawablePadding.top - paddingV),
                    (mRect.right + drawablePadding.right + paddingH),
                    (mRect.bottom + drawablePadding.bottom + paddingH))
            mDrawable.bounds = shadowBound
            mDrawable.draw(canvas)
        }
    }
}