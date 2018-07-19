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

        fun callingIntent(context: Context, action: Int): Intent {
            val intent = Intent(context, ClassesActivity::class.java)
            intent.putExtra(ClassesFragment.ACTION, action)
            return intent
        }
    }

    override fun fragment(): BaseFragment = ClassesFragment.forAction(intent?.getIntExtra(ClassesFragment.ACTION, ClassesFragment.ACTION_TO_CLASS))
}