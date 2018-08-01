package com.prance.teacher.features.afterclass

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.afterclass.view.AfterClassFragment
import com.prance.teacher.features.check.view.CheckKeyPadFragment
import com.prance.teacher.features.classes.view.ClassesFragment

class AfterClassActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, AfterClassActivity::class.java)
    }

    override fun fragment(): BaseFragment = AfterClassFragment()
}