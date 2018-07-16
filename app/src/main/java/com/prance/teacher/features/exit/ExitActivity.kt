package com.prance.teacher.features.exit

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity

class ExitActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, ExitActivity::class.java)
    }

    override fun fragment(): BaseFragment = ExitFragment()
}