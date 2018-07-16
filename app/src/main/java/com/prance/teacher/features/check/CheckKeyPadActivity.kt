package com.prance.teacher.features.check

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity

class CheckKeyPadActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, CheckKeyPadActivity::class.java)
    }

    override fun fragment(): BaseFragment = CheckKeyPadFragment()
}