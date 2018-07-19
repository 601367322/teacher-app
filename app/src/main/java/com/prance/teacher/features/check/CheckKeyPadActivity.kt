package com.prance.teacher.features.check

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.check.view.CheckKeyPadFragment
import com.prance.teacher.features.classes.view.ClassesFragment

class CheckKeyPadActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, CheckKeyPadActivity::class.java)

        fun callingIntent(context: Context, action: Int): Intent {
            val intent = Intent(context, CheckKeyPadActivity::class.java)
            intent.putExtra(ClassesFragment.ACTION, action)
            return intent
        }
    }

    override fun fragment(): BaseFragment = CheckKeyPadFragment.forAction(intent?.getIntExtra(ClassesFragment.ACTION, CheckKeyPadFragment.ACTION_JUST_CHECK))
}