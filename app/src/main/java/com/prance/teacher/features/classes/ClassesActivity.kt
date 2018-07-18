package com.prance.teacher.features.classes

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.classes.view.ClassesFragment
import com.prance.teacher.features.match.MatchKeyPadActivity

class ClassesActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, ClassesActivity::class.java)
    }

    override fun fragment(): BaseFragment = ClassesFragment()
}