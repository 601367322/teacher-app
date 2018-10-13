package com.prance.teacher.features.replacekeypad

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.replacekeypad.view.DeleteKeyPadFragment


class DeleteKeyPadActivity : BaseActivity() {

    companion object {

        fun callingIntent(context: Context): Intent {
            return Intent(context, DeleteKeyPadActivity::class.java)
        }
    }

    override fun fragment(): BaseFragment = DeleteKeyPadFragment()
}