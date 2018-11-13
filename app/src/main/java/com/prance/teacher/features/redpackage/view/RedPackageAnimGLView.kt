package com.prance.teacher.features.redpackage.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log

import com.chillingvan.canvasgl.ICanvasGL
import com.chillingvan.canvasgl.glview.GLContinuousView
import com.chillingvan.canvasgl.textureFilter.BasicTextureFilter
import com.prance.teacher.features.redpackage.model.RedPackageStatus
import com.prance.teacher.features.redpackage.model.RedPackageTipStatus
import com.prance.teacher.features.redpackage.view.red.RedPackage
import com.prance.teacher.features.redpackage.view.red.RedPackageManager
import com.prance.teacher.features.redpackage.view.red.RedPackageManager.Companion.DEFAULT_SCALE
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
                    //放大后，计算x，y偏移量
                    val bitmapMatrix: ICanvasGL.BitmapMatrix = ICanvasGL.BitmapMatrix()

                    val widthDiff = (item.redPackage.width * DEFAULT_SCALE - item.redPackage.width) / 2
                    val heightDiff = (item.redPackage.height * DEFAULT_SCALE - item.redPackage.height) / 2

                    itemX -= widthDiff.toInt()
                    itemY -= heightDiff.toInt()

                    titleX = (itemX + item.titleX * DEFAULT_SCALE).toInt()
                    titleY = (itemY + item.titleY * DEFAULT_SCALE).toInt()

                    item.bubble?.run {
                        bitmapMatrix.scale(DEFAULT_SCALE, DEFAULT_SCALE)
                        bitmapMatrix.translate(itemX.toFloat(), itemY.toFloat())

                        canvas.drawBitmap(item.redPackage, bitmapMatrix)

                        bitmapMatrix.reset()
                        bitmapMatrix.scale(DEFAULT_SCALE, DEFAULT_SCALE)
                        bitmapMatrix.translate(itemX.toFloat(), itemY.toFloat())

                        canvas.drawBitmap(bubble, bitmapMatrix)

                        bitmapMatrix.reset()
                        bitmapMatrix.scale(DEFAULT_SCALE, DEFAULT_SCALE)
                        bitmapMatrix.translate(titleX.toFloat(), titleY.toFloat())
                        canvas.drawBitmap(item.redPackageTitle, bitmapMatrix)
                    }
                } else {
                    item.bubble?.run {
                        canvas.drawBitmap(item.redPackage, itemX, itemY)
                        canvas.drawBitmap(item.redPackageTitle, titleX, titleY)
                        canvas.drawBitmap(bubble, itemX, itemY)
                    }
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

    fun clear(){
        mLocations.clear()
    }
}
