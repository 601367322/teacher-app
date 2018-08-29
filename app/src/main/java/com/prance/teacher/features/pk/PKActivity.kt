package com.prance.teacher.features.pk

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.pk.view.PKFragment

class PKActivity : BaseActivity() {

    companion object {

        fun callingIntent(context: Context, setting: ClassesDetailFragment.Question): Intent {
            val intent = Intent(context, PKActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            intent.putExtra(PKFragment.SETTING, setting)
            return intent
        }
    }

    override fun fragment(): BaseFragment? = PKFragment.forSetting(intent.getSerializableExtra(PKFragment.SETTING) as ClassesDetailFragment.Question)
}