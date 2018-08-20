package com.prance.teacher.weight

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ProgressBar
import com.prance.teacher.R

class SimpleProgressbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ProgressBar(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        //        super.onDraw(canvas);
        // 获取画布的宽高
        val width = measuredWidth
        val height = measuredHeight
        // 获取进度条的实际宽高
        val lineWidth = width - paddingLeft - paddingRight
        val lineHeight = height - paddingTop - paddingBottom
        // 获取当前进度
        val ratio = progress.toFloat() / max
        // 获取未完成进度大小
        val unreachedWidth = (lineWidth * (1 - ratio)).toInt()
        // 获取已完成进度大小
        val reachedWidth = lineWidth - unreachedWidth

        //渐变色
        val mShader = LinearGradient(
                0F,
                0F,
                0F,
                lineHeight.toFloat(),
                intArrayOf(
                        Color.parseColor("#FF881E"),
                        Color.parseColor("#FF3C00")),
                null,
                Shader.TileMode.REPEAT)

        paint.shader = mShader

        // 计算已完成进度条起点和终点的坐标
        var startX = paddingLeft
        val startY = getHeight()
        var stopX = startX + reachedWidth

        canvas.drawRoundRect(0F, 0F, stopX.toFloat(), startY.toFloat(), resources.getDimensionPixelOffset(R.dimen.m13_0).toFloat(), resources.getDimensionPixelOffset(R.dimen.m13_0).toFloat(), paint)
    }
}
