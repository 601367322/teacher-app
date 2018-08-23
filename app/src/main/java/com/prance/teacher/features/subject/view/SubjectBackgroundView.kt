package com.prance.teacher.features.subject.view

import android.content.Context
import android.util.AttributeSet

import com.chillingvan.canvasgl.ICanvasGL
import com.chillingvan.canvasgl.glview.GLContinuousView
import com.chillingvan.canvasgl.textureFilter.BasicTextureFilter
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.model.RedPackageStatus
import com.prance.teacher.features.redpackage.model.RedPackageTipStatus
import com.prance.teacher.features.redpackage.view.red.RedPackage
import com.prance.teacher.features.subject.model.FrameBackground
import master.flame.danmaku.danmaku.util.SystemClock
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

class SubjectBackgroundView : GLContinuousView {

    constructor(context: Context) : super(context){
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        init(context)
    }

    private var mBackground: FrameBackground? = null

    private fun init(context: Context) {
        setZOrderOnTop(true)
        setZOrderMediaOverlay(true)
//        mBackground = FrameBackground(context, mutableListOf(
//                R.drawable.rank_bg_00000,
//                R.drawable.rank_bg_00002,
//                R.drawable.rank_bg_00004,
//                R.drawable.rank_bg_00006,
//                R.drawable.rank_bg_00008,
//                R.drawable.rank_bg_00010,
//                R.drawable.rank_bg_00012,
//                R.drawable.rank_bg_00014,
//                R.drawable.rank_bg_00016,
//                R.drawable.rank_bg_00018,
//                R.drawable.rank_bg_00020,
//                R.drawable.rank_bg_00022,
//                R.drawable.rank_bg_00024,
//                R.drawable.rank_bg_00026,
//                R.drawable.rank_bg_00028
//        ))
        mBackground!!.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mBackground?.stop()
    }

    override fun onGLDraw(canvas: ICanvasGL) {
        canvas.save()
        mBackground?.bitmap?.run {
            canvas.drawBitmap(this, 0, 0)
        }
        canvas.restore()
    }
}
