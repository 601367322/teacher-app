package com.prance.lib.common.utils.weight

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View

import com.prance.lib.common.R
import kotlinx.android.synthetic.main.dialog_layout.*
import android.view.WindowManager
import android.view.Gravity

/**
 * Description : 自定义对话框
 * @author  Sen
 * @date 2018/7/14  下午2:21
 */
class AlertDialog(context: Context) : Dialog(context, R.style.DialogStyle) {

    private var mMessageStr: String? = null//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private var mConfirmStr: String? = null
    private var mCancelStr: String? = null

    private var mCancelOnClickListener: ((v: View) -> Unit?)? = null//取消按钮被点击了的监听器
    private var mConfirmOnClickListener: ((v: View) -> Unit?)? = null//确定按钮被点击了的监听器

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window = window
        val lp = window.attributes
        lp.gravity = Gravity.CENTER
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT//宽高可设置具体大小

        setContentView(R.layout.dialog_layout)


        confirm.setText(mConfirmStr!!)
        confirm.setOnClickListener {
            mConfirmOnClickListener?.invoke(it)
            dismiss()
        }
        cancel.setText(mCancelStr!!)
        cancel.setOnClickListener {
            mCancelOnClickListener?.invoke(it)
            dismiss()
        }
        message.text = mMessageStr
    }

    fun setConfirmButton(text: String, listener: ((v: View) -> Unit)?): AlertDialog {
        mConfirmStr = text
        mConfirmOnClickListener = listener
        return this
    }

    fun setCancelButton(text: String, listener: ((v: View) -> Unit)?): AlertDialog {
        mCancelStr = text
        mCancelOnClickListener = listener
        return this
    }

    fun setMessage(text: String): AlertDialog {
        mMessageStr = text
        return this
    }

}
