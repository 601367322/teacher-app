package com.prance.teacher.features.redpackage.view

import android.content.Context
import android.util.AttributeSet

import com.chillingvan.canvasgl.ICanvasGL
import com.chillingvan.canvasgl.glview.GLContinuousView
import com.chillingvan.canvasgl.textureFilter.BasicTextureFilter
import com.prance.teacher.features.redpackage.model.RedPackageStatus
import com.prance.teacher.features.redpackage.model.RedPackageTipStatus
import com.prance.teacher.features.redpackage.view.red.RedPackage
import com.prance.teacher.features.redpackage.view.red.RedPackageManager
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

                var itemX = item.x
                var itemY = item.y
                var titleX = item.x + item.titleX
                var titleY = item.y + item.titleY

                if (item.big) {
                    //减去1/4宽度，再除以缩放比例
                    var widthDiff = (item.redPackage.width * RedPackageManager.DEFAULT_SCALE - item.redPackage.width) / 2 / RedPackageManager.DEFAULT_SCALE
                    var heightDiff = (item.redPackage.height * RedPackageManager.DEFAULT_SCALE - item.redPackage.height) / 2 / RedPackageManager.DEFAULT_SCALE
                    var titleWidthDiff = (item.redPackageTitle.width * RedPackageManager.DEFAULT_SCALE - item.redPackageTitle.width) / 2 / RedPackageManager.DEFAULT_SCALE
                    var titleHeightDiff = (item.redPackageTitle.height * RedPackageManager.DEFAULT_SCALE - item.redPackageTitle.height) / 2 / RedPackageManager.DEFAULT_SCALE

                    canvas.scale(RedPackageManager.DEFAULT_SCALE, RedPackageManager.DEFAULT_SCALE)
                    itemX = (itemX.toFloat() / RedPackageManager.DEFAULT_SCALE).toInt() - widthDiff.toInt()
                    itemY = (itemY.toFloat() / RedPackageManager.DEFAULT_SCALE).toInt() - heightDiff.toInt()
                    titleX = (titleX.toFloat() / RedPackageManager.DEFAULT_SCALE).toInt() - titleWidthDiff.toInt()
                    titleY = (titleY.toFloat() / RedPackageManager.DEFAULT_SCALE).toInt()
                }

                item.bubble?.run {
                    canvas.drawBitmap(item.redPackage, itemX, itemY)
                    canvas.drawBitmap(item.redPackageTitle, titleX, titleY)
                    canvas.drawBitmap(this, itemX, itemY)
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
}
