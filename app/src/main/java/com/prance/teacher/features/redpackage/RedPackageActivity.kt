package com.prance.teacher.features.redpackage

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.afterclass.view.AfterClassFragment
import com.prance.teacher.features.check.view.CheckKeyPadFragment
import com.prance.teacher.features.classes.view.ClassesFragment
import com.prance.teacher.features.redpackage.view.RedPackageFragment

class RedPackageActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, RedPackageActivity::class.java)
    }

    override fun fragment(): BaseFragment = RedPackageFragment()
}