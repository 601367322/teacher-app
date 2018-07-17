package com.prance.teacher.features.main

import android.content.Context
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import android.content.Intent
import com.prance.teacher.features.main.view.MainFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainActivity : BaseActivity() {

    override fun fragment(): BaseFragment = MainFragment()

    companion object {

        fun callingIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onBackKeyEvent(): Boolean {
        supportFragmentManager.fragments?.let {
            if(!it.isEmpty()){
                (it[0] as MainFragment).exit.performClick()
            }
        }
        return true
    }
}