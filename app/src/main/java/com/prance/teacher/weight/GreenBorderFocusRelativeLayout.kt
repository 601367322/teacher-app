package com.prance.teacher.weight

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.prance.lib.common.utils.weight.FocusRelativeLayout
import com.prance.teacher.R

class GreenBorderFocusRelativeLayout : FocusRelativeLayout {

    internal var i = 0


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun init() {
        super.init()

        mDrawable = resources.getDrawable(R.drawable.key_pad_focus_green_border)
    }

    override fun onDraw(canvas: Canvas) {
        var mRect = Rect()
        super.getDrawingRect(mRect)

        super.onDraw(canvas)

        if (hasFocus()) {

            //画阴影
            val drawablePadding = Rect()
            mDrawable.getPadding(drawablePadding)
            var shadowBound = Rect(
                    (mRect.left - drawablePadding.left ),
                    (mRect.top - drawablePadding.top),
                    (mRect.right + drawablePadding.right),
                    (mRect.bottom + drawablePadding.bottom))
            mDrawable.bounds = shadowBound
            mDrawable.draw(canvas)

        }
    }
}