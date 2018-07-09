package com.prance.lib.common.utils

import android.app.ProgressDialog
import android.content.Context

import com.prance.lib.common.R

/**
 * Created by shenbingbing on 16/4/28.
 */
object ProgressDialogUtil {

    fun progress(context: Context?, res: Int): ProgressDialog? {
        return if (context != null && res != -1) {
            progress(context, context.resources.getString(res), true)
        } else {
            null
        }
    }

    fun progress(context: Context?, str: String?): ProgressDialog? {
        return if (context != null && str != null) {
            progress(context, str, true)
        } else {
            null
        }
    }

    fun progress(context: Context?, str: String?, cancleable: Boolean): ProgressDialog? {
        if (context != null && str != null) {
            val dialog = ProgressDialog(context, R.style.progress_dialog_no_dark_background)
            dialog.setMessage(str)
            dialog.setCancelable(cancleable)
            dialog.isIndeterminate = false
            return dialog
        } else {
            return null
        }
    }
}
