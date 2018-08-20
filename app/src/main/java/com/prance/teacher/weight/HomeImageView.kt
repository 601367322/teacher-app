package com.prance.teacher.weight

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.prance.teacher.R
import android.graphics.Bitmap
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth


class HomeImageView : ImageView {

    private var mShadowWidth: Float = 0F
    private var mStrokeWidth: Float = 0F
    private var mRadius: Float = 0F
    private var mPaint: Paint? = null

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        setWillNotDraw(false)

        mShadowWidth = resources.getDimensionPixelOffset(R.dimen.m13_0).toFloat()
        mStrokeWidth = resources.getDimensionPixelOffset(R.dimen.m3_0).toFloat()
        mRadius = resources.getDimensionPixelOffset(R.dimen.m6_0).toFloat()

//        mPaint = Paint()
//        mPaint.setColor(Color.RED);
//        mPaint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.INNER));

    }


    override fun onDraw(canvas: Canvas?) {
        val drawable = drawable

        if (null != drawable) {
            val bitmap = (drawable as BitmapDrawable).bitmap
            val dstBitmap = Bitmap.createBitmap(
                    bitmap.width, // Width
                    bitmap.height, // Height
                    Bitmap.Config.ARGB_4444 // Config
            )
            val paint = Paint()
            paint.isAntiAlias = true
            paint.color = Color.parseColor("#FFFFFF")
            paint.setShadowLayer(mShadowWidth, 0F, 0F, Color.parseColor("#1567B3"))
            val width = measuredWidth
            val height = measuredHeight
            val startLeft = mShadowWidth
            val startTop = mShadowWidth
            canvas?.drawRoundRect(RectF(startLeft, startTop, width - startLeft, height - startTop), mRadius, mRadius, paint)


//            val b = getCircleBitmap(bitmap, 14)
//            val rectSrc = Rect(0, 0, b.getWidth(), b.getHeight())
//            val rectDest = Rect(0, 0, width, height)
//            paint.reset()
//            canvas?.drawBitmap(b, rectSrc, rectDest, paint)
        }
        super.onDraw(canvas)
//        val output = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
//        canvas?.drawBitmap(output, 0F, 0F, Paint())
//        val paint = Paint()
//        paint.color = Color.parseColor("#FFFFFF")
//        paint.isAntiAlias = true
//        paint.setShadowLayer(mShadowWidth, 0F, 0F, Color.parseColor("#1567B3"))
//        paint.strokeWidth = mStrokeWidth;
//        val width = measuredWidth
//        val height = measuredHeight
//        val startLeft = mShadowWidth
//        val startTop = mShadowWidth
//        canvas?.drawRoundRect(RectF(startLeft, startTop, width - startLeft, height - startTop), mRadius, mRadius, paint)
//
//        super.onDraw(canvas)
//
//        invalidate()
    }
}