package com.prance.teacher.weight

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.*
import com.prance.teacher.R

@SuppressLint("StaticFieldLeak")
object FloatIcon {

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
            val params = WindowManager.LayoutParams()
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                Build.VERSION.SDK_INT == Build.VERSION_CODES.N -> params.type = WindowManager.LayoutParams.TYPE_TOAST
                Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1 -> params.type = WindowManager.LayoutParams.TYPE_PHONE
                else -> params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
            params.format = 1
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE // 不能抢占聚焦点
            params.flags = params.flags or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
            params.flags = params.flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS // 排版不受限制

            params.width = WindowManager.LayoutParams.WRAP_CONTENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            params.gravity = Gravity.RIGHT or Gravity.BOTTOM
//            params.x = 0
//            params.y = SizeUtils.dp2px(200f)

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
        val view = LayoutInflater.from(context).inflate(R.layout.weight_float_btn, null)
        return view
    }
}