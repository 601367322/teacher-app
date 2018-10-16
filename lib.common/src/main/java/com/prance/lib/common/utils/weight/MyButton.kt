package com.prance.lib.common.utils.weight

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.prance.lib.common.utils.getInflate
import com.prance.lib.common.R
import kotlinx.android.synthetic.main.weight_my_button_with_icon.view.*


open class MyButton : FocusRelativeLayout {

    private var mRadius: Float = 0F
    private var mDisableIcon: Int? = -1
    private var mIcon: Int? = -1
    protected var mText: String = ""

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {


        val a = context.obtainStyledAttributes(attrs, R.styleable.MyButton)
        try {
            val layout = a.getResourceId(R.styleable.MyButton_layoutRes, R.layout.weight_my_button_with_icon)
            addView(getInflate(this, layout))

            mIcon = a.getResourceId(R.styleable.MyButton_icon, -1)
            if (mIcon != -1) {
                icon?.setImageResource(mIcon!!)
            } else {
                icon?.visibility = View.GONE
            }
            mDisableIcon = a.getResourceId(R.styleable.MyButton_disable_icon, -1)
            mText = a.getString(R.styleable.MyButton_text)
            if (mText.isNotEmpty()) {
                text.text = mText
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            a.recycle()
        }
    }

    override fun init() {
        super.init()

        isFocusable = true
        mDrawable = resources.getDrawable(R.drawable.button_focus_bg)

        mRadius = resources.getDimensionPixelOffset(R.dimen.m8_0).toFloat()


    }

    fun setText(str: String) {
        text.text = str
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        isFocusable = enabled
        if (enabled) {
            mIcon?.run {
                icon.setImageResource(this)
            }
        } else {
            mDisableIcon?.run {
                icon.setImageResource(this)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        var mRect = Rect()
        super.getDrawingRect(mRect)

        if (isEnabled) {
            if (hasFocus()) {
                //边框的偏移量
                val padding = resources.getDimensionPixelOffset(R.dimen.m2_0)
                //画阴影
                val drawablePadding = Rect()
                mDrawable.getPadding(drawablePadding)
                var shadowBound = Rect(
                        (mRect.left - drawablePadding.left - padding),
                        (mRect.top - drawablePadding.top - padding),
                        (mRect.right + drawablePadding.right + padding),
                        (mRect.bottom + drawablePadding.bottom + padding))
                mDrawable.bounds = shadowBound
                mDrawable.draw(canvas)

                val disablePaint = Paint(Paint.ANTI_ALIAS_FLAG)
                disablePaint.color = Color.parseColor("#3A81F0")
                canvas.drawRoundRect(RectF(mRect), mRadius, mRadius, disablePaint)

                val focusPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                focusPaint.color = Color.parseColor("#33ffffff")
                canvas.drawRoundRect(RectF(mRect), mRadius, mRadius, focusPaint)

                //边框的大小
                var mBorderRect = Rect(mRect)
                mBorderRect.set(
                        -padding + mRect.left,
                        -padding + mRect.top,
                        padding + mRect.right,
                        padding + mRect.bottom)

                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                paint.strokeWidth = resources.getDimensionPixelOffset(R.dimen.m2_0).toFloat()
                paint.style = Paint.Style.STROKE
                paint.color = Color.WHITE

                //圆角边框，需要算上strokeWidth的宽度
                val roundRect = RectF(
                        mBorderRect.left + paint.strokeWidth / 2,
                        mBorderRect.top + paint.strokeWidth / 2,
                        mBorderRect.right - paint.strokeWidth / 2,
                        mBorderRect.bottom - paint.strokeWidth / 2)

                //画边框
                canvas.drawRoundRect(
                        roundRect,
                        mRadius,
                        mRadius,
                        paint)

            } else {
                //画阴影
                val drawablePadding = Rect()
                mDrawable.getPadding(drawablePadding)
                var shadowBound = Rect(
                        (mRect.left - drawablePadding.left),
                        (mRect.top - drawablePadding.top),
                        (mRect.right + drawablePadding.right),
                        (mRect.bottom + drawablePadding.bottom))
                mDrawable.bounds = shadowBound
                mDrawable.draw(canvas)

                val disablePaint = Paint(Paint.ANTI_ALIAS_FLAG)
                disablePaint.color = Color.parseColor("#3A81F0")
                canvas.drawRoundRect(RectF(mRect), mRadius, mRadius, disablePaint)
            }
        } else {
            val disablePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            disablePaint.color = Color.parseColor("#959595")
            canvas.drawRoundRect(RectF(mRect), mRadius, mRadius, disablePaint)
            return
        }
        super.onDraw(canvas)
    }


}