package com.prance.lib.teacher.base.weight

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import com.prance.lib.common.utils.getInflate
import com.prance.lib.teacher.base.R
import kotlinx.android.synthetic.main.weight_my_button.view.*
import android.widget.RelativeLayout
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.github.ksoichiro.android.observablescrollview.ScrollUtils.addOnGlobalLayoutListener
import android.view.ViewTreeObserver


class MyButton : FocusRelativeLayout {

    private var mShadowWidth: Float = 0F
    private var mRadius: Float = 0F

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        addView(getInflate(this, R.layout.weight_my_button))

        val a = context.obtainStyledAttributes(attrs, R.styleable.MyButton)
        try {
            val iconId = a.getResourceId(R.styleable.MyButton_icon, -1)
            if (iconId != -1) {
                icon.setImageResource(iconId)
            }
            val textStr = a.getString(R.styleable.MyButton_text)
            if (textStr.isNotEmpty()) {
                text.text = textStr
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            a.recycle()
        }

        isFocusable = true

        mShadowWidth = resources.getDimensionPixelOffset(R.dimen.m8_0).toFloat()
        mRadius = resources.getDimensionPixelOffset(R.dimen.m4_0).toFloat()

        val viewTreeObserver = container.viewTreeObserver
        viewTreeObserver
                .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        container.viewTreeObserver
                                .removeOnGlobalLayoutListener(this)

                        val buttonBackground = ButtonBackground(context)
                        val layoutParams = RelativeLayout.LayoutParams(container.width + mShadowWidth.toInt() * 2, container.height + mShadowWidth.toInt() * 2)
                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
                        buttonBackground.layoutParams = layoutParams
                        ((container.parent) as RelativeLayout).addView(buttonBackground, 0)
                    }
                })

    }

}