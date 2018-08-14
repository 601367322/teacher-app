package com.prance.teacher.features.main.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.RelativeLayout

import com.prance.lib.common.utils.weight.FocusRelativeLayout
import com.prance.teacher.R


class MainFocusRelativeLayout : FocusRelativeLayout {

    internal var i = 0

    var shadow: MainFocusShadow? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun init() {
        super.init()

        mDrawable = resources.getDrawable(R.drawable.home_focus_shadow)
    }

    override fun onDraw(canvas: Canvas) {
        var mRect = Rect()
        super.getDrawingRect(mRect)
        if (hasFocus()) {
            //边框的偏移量
            val padding = resources.getDimensionPixelOffset(R.dimen.m12_0)

            //画阴影
            val drawablePadding = Rect()
            mDrawable.getPadding(drawablePadding)
            var shadowBound = Rect(
                    (mRect.left - drawablePadding.left -padding),
                    (mRect.top - drawablePadding.top-padding),
                    (mRect.right + drawablePadding.right+padding),
                    (mRect.bottom + drawablePadding.bottom +padding))
            mDrawable.bounds = shadowBound
            mDrawable.draw(canvas)

            //边框的大小
            var mBorderRect = Rect(mRect)
            mBorderRect.set(
                    -padding + mRect.left,
                    -padding + mRect.top,
                    padding + mRect.right,
                    padding + mRect.bottom)

            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.strokeWidth = resources.getDimensionPixelOffset(R.dimen.m6_0).toFloat()
            paint.style = Paint.Style.STROKE

            //圆角边框，需要算上strokeWidth的宽度
            val roundRect = RectF(
                    mBorderRect.left + paint.strokeWidth / 2,
                    mBorderRect.top + paint.strokeWidth / 2,
                    mBorderRect.right - paint.strokeWidth / 2,
                    mBorderRect.bottom - paint.strokeWidth / 2)

            //渐变色
            val mShader = LinearGradient(
                    roundRect.left,
                    roundRect.top,
                    roundRect.right,
                    roundRect.bottom,
                    intArrayOf(
                            Color.parseColor("#FFFFFF"),
                            Color.parseColor("#919191"),
                            Color.parseColor("#C6C6C6")),
                    null,
                    Shader.TileMode.REPEAT)

            paint.shader = mShader

            //画边框
            canvas.drawRoundRect(
                    roundRect,
                    resources.getDimensionPixelOffset(R.dimen.m26_0).toFloat(),
                    resources.getDimensionPixelOffset(R.dimen.m26_0).toFloat(),
                    paint)
        }else{
            //画阴影
            val drawablePadding = Rect()
            mDrawable.getPadding(drawablePadding)
            var shadowBound = Rect(
                    (mRect.left - drawablePadding.left),
                    (mRect.top - drawablePadding.top),
                    (mRect.right + drawablePadding.right),
                    (mRect.bottom + drawablePadding.bottom ))
            mDrawable.bounds = shadowBound
            mDrawable.draw(canvas)
        }
        super.onDraw(canvas)
    }
}
