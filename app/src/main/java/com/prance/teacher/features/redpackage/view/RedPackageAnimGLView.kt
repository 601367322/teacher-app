package com.prance.teacher.features.redpackage.view

import android.content.Context
import android.util.AttributeSet

import com.chillingvan.canvasgl.ICanvasGL
import com.chillingvan.canvasgl.glview.GLContinuousView
import com.chillingvan.canvasgl.textureFilter.BasicTextureFilter
import com.prance.teacher.features.redpackage.model.RedPackageStatus
import com.prance.teacher.features.redpackage.model.RedPackageTipStatus
import com.prance.teacher.features.redpackage.view.red.RedPackage
import master.flame.danmaku.danmaku.util.SystemClock
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

class RedPackageAnimGLView : GLContinuousView {

    private val mLocations = CopyOnWriteArrayList<RedPackage>()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onGLDraw(canvas: ICanvasGL) {
        for (item in mLocations) {
            //画红包
            if (item.state != RedPackageStatus.FREE) {
                canvas.save()
                canvas.setAlpha(item.alpha)
                item.bubble?.run {
                    canvas.drawBitmap(item.redPackage, item.x, item.y)
                    canvas.drawBitmap(item.redPackageTitle, item.x + item.titleX, item.y + item.titleY)
                    canvas.drawBitmap(this, item.x, item.y)
                }
                canvas.restore()
            }
            //如果已经被抢，绘制+2分
            item.scoreTip?.let {
                if (it.state == RedPackageTipStatus.SHOW) {
                    canvas.save()
                    canvas.setAlpha(it.alpha)
                    try {
                        if (!it.bitmap.isRecycled)
                            canvas.drawBitmap(it.bitmap, it.x, it.y)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    canvas.restore()
                }
            }
        }
    }

    fun addItem(redPackage: RedPackage) {
        mLocations.add(redPackage)
    }


    private val MAX_RECORD_SIZE = 50
    private val ONE_SECOND = 1000
    private var mDrawTimes = LinkedList<Long>()

    private fun fps(): Float {
        val lastTime = SystemClock.uptimeMillis()
        mDrawTimes.addLast(lastTime)
        val first = mDrawTimes.peekFirst() ?: return 0.0f
        val dtime = (lastTime - first).toFloat()
        val frames = mDrawTimes.size
        if (frames > MAX_RECORD_SIZE) {
            mDrawTimes.removeFirst()
        }
        return if (dtime > 0) mDrawTimes.size * ONE_SECOND / dtime else 0.0f
    }
}
