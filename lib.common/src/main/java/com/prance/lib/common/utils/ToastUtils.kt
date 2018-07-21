package com.prance.lib.common.utils

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.blankj.utilcode.util.Utils
import com.prance.lib.common.R
import kotlinx.android.synthetic.main.toast_layout.view.*

class ToastUtils {

    companion object {

        private var mToast: Toast? = null

        fun showShort(format: String?) {
            cancel()
            val view = getView(R.layout.toast_layout)
            view!!.toast_str.text = format

            var toast = Toast.makeText(Utils.getApp(), format, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.view = view
            mToast = toast
            toast.show()
        }

        private fun cancel() {
            mToast?.let {
                it.cancel()
            }
        }

        private fun getView(@LayoutRes layoutId: Int): View? {
            val inflate = Utils.getApp().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            return inflate?.inflate(layoutId, null)
        }
    }
}