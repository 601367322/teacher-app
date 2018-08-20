package com.prance.teacher.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.widget.AutoSizeableTextView
import android.util.AttributeSet
import android.widget.TextView
import com.prance.teacher.R
import android.view.ViewGroup
import com.blankj.utilcode.util.SizeUtils


class FZTextView : TextView, AutoSizeableTextView {

    var strokeWidth = 0
    var strokeColor = Color.BLACK

    private var isDrawing: Boolean = false

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        this.typeface = FontCustom.getFZY1JWFont(context)

        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.FZTextView)
            try {
                strokeWidth = a.getDimensionPixelOffset(R.styleable.FZTextView_strokeWidth, strokeWidth)
                strokeColor = a.getColor(R.styleable.FZTextView_strokeColor, strokeColor)
                var typeface = a.getString(R.styleable.FZTextView_typeface)

                typeface?.let {
                    when (it) {
                        "COMICSANSMSGRAS" -> {
                            this.typeface = FontCustom.getCOMICSANSMSGRASFont(context)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                a.recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (strokeWidth > 0) {
            isDrawing = true
            val p = paint
            p.style = Paint.Style.FILL

            super.onDraw(canvas)

            val currentTextColor = currentTextColor
            p.style = Paint.Style.STROKE
            p.strokeWidth = SizeUtils.px2dp(strokeWidth.toFloat()).toFloat()
            setTextColor(strokeColor)
            super.onDraw(canvas)
            setTextColor(currentTextColor)
            isDrawing = false
        } else {
            super.onDraw(canvas)
        }
    }
}