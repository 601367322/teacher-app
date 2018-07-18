package com.prance.teacher.features.bind

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.bind.view.BindKeyPadFragment

class BindKeyPadActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, BindKeyPadActivity::class.java)
    }

    override fun fragment(): BaseFragment = BindKeyPadFragment()
}