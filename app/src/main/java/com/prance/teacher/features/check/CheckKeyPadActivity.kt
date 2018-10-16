package com.prance.teacher.features.check

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.check.view.CheckKeyPadFragment
import com.prance.teacher.features.classes.view.ClassesFragment

/**
 * 硬件检测
 */
class CheckKeyPadActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, CheckKeyPadActivity::class.java)
    }

    override fun fragment(): BaseFragment = CheckKeyPadFragment()
}