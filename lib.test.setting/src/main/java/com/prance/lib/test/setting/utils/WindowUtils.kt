package com.prance.lib.test.setting.utils

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams

import com.blankj.utilcode.util.SizeUtils
import com.prance.lib.test.setting.R
import com.prance.lib.test.setting.features.TestSettingActivity

/**
 * 弹窗辅助类
 *
 * @ClassName WindowUtils
 */
object WindowUtils {

    private val LOG_TAG = "WindowUtils"
    private var mView: View? = null
    private var mWindowManager: WindowManager? = null
    private var mContext: Context? = null
    var isShown: Boolean? = false

    /**
     * 显示弹出框
     *
     * @param context
     */
    fun showPopupWindow(context: Context) {
        if (isShown!!) {
            return
        }
        try {
            // 获取应用的Context
            mContext = context.applicationContext
            // 获取WindowManager
            mWindowManager = mContext!!
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            mView = setUpView(context)
            val params = LayoutParams()

            params.type = LayoutParams.TYPE_SYSTEM_ALERT     // 系统提示类型,重要
            params.format = 1
            params.flags = LayoutParams.FLAG_NOT_FOCUSABLE // 不能抢占聚焦点
            params.flags = params.flags or LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
            params.flags = params.flags or LayoutParams.FLAG_LAYOUT_NO_LIMITS // 排版不受限制

            params.width = LayoutParams.WRAP_CONTENT
            params.height = LayoutParams.WRAP_CONTENT
            params.gravity = Gravity.LEFT or Gravity.TOP
            params.x = 0
            params.y = SizeUtils.dp2px(200f)

            mWindowManager!!.addView(mView, params)

            isShown = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 隐藏弹出框
     */
    fun hidePopupWindow() {
        if (isShown!! && null != mView) {
            try {
                mWindowManager!!.removeViewImmediate(mView)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            isShown = false
        }
    }

    private fun setUpView(context: Context): View {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_float_test, null)
        // 点击back键可消除
        view.setOnTouchListener(object : View.OnTouchListener {
            private var lastX: Int = 0
            private var lastY: Int = 0
            private var screenWidth: Int = 0
            private var screenHeight: Int = 0
            private var left: Int = 0
            private var right: Int = 0
            private var bottom: Int = 0
            private var top: Int = 0
            private var mStartX: Float = 0.toFloat()
            private var mStartY: Float = 0.toFloat()
            private var mLastX: Float = 0.toFloat()
            private var mLastY: Float = 0.toFloat()
            private var mLastTime: Long = 0
            private var mCurrentTime: Long = 0

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val layoutParams = v
                        .layoutParams as LayoutParams
                val action = event.action
                when (action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.rawX.toInt()
                        lastY = event.rawY.toInt()
                        mStartX = event.rawX
                        mStartY = event.rawY
                        mLastTime = System.currentTimeMillis()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = event.rawX.toInt() - lastX
                        val dy = event.rawY.toInt() - lastY
                        screenWidth = mContext!!.resources.displayMetrics.widthPixels
                        screenHeight = mContext!!.resources.displayMetrics.heightPixels

                        left = layoutParams.x + dx
                        top = layoutParams.y + dy
                        right = layoutParams.x + v.measuredWidth + dx
                        bottom = layoutParams.y + v.measuredHeight + dy
                        if (left < 0) {
                            left = 0
                            right = left + v.width
                        }
                        if (right > screenWidth) {
                            right = screenWidth
                            left = right - v.width
                        }
                        if (top < 0) {
                            top = 0
                            bottom = top + v.height
                        }
                        if (bottom > screenHeight) {
                            bottom = screenHeight
                            top = bottom - v.height
                        }

                        layoutParams.x = left
                        layoutParams.y = top

                        v.layoutParams = layoutParams

                        mWindowManager!!.updateViewLayout(view, layoutParams)

                        lastX = event.rawX.toInt()
                        lastY = event.rawY.toInt()
                    }
                    MotionEvent.ACTION_UP -> {
                        if (left <= screenWidth / 2) {
                            left = 0
                        } else {
                            left = screenWidth - v.width
                        }

                        mLastX = event.rawX
                        mLastY = event.rawY
                        mCurrentTime = System.currentTimeMillis()
                        if (mCurrentTime - mLastTime < 800) {//长按不起作用
                            if (Math.abs(mStartX - mLastX) < 10.0 && Math.abs(mStartY - mLastY) < 10.0) {//判断是否属于点击
                                context.startActivity(TestSettingActivity.callingIntent(context))
                            }
                        }
                    }
                    else -> {
                    }
                }
                return true
            }
        })
        return view
    }
} 
