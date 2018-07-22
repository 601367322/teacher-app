package com.prance.lib.common.utils.weight

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.prance.lib.common.utils.getInflate
import android.widget.RelativeLayout
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.prance.lib.common.R
import kotlinx.android.synthetic.main.weight_my_button_with_icon.view.*


class MyButton : FocusRelativeLayout {

    private var mShadowWidth: Float = 0F
    private var mRadius: Float = 0F
    private var mDisableIcon: Int? = -1
    private var mIcon: Int? = -1

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
}