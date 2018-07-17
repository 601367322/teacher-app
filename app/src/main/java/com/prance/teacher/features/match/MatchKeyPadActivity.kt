package com.prance.teacher.features.match

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.match.view.MatchKeyPadFragment

class MatchKeyPadActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, MatchKeyPadActivity::class.java)
    }

    override fun fragment(): BaseFragment = MatchKeyPadFragment()

    override fun onBackKeyEvent(): Boolean {
        supportFragmentManager.fragments?.let {
            if (!it.isEmpty())
                return (it[0] as MatchKeyPadFragment).onBackKeyEvent()
        }
        return false
    }
}