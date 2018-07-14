package com.prance.teacher.features.login

import android.content.Context
import android.content.Intent
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.features.login.view.LoginFragment
import com.prance.teacher.features.main.MainActivity

class LoginActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    override fun fragment(): BaseFragment = LoginFragment()

    override fun onOkKeyEvent(): Boolean {
        startActivity(MainActivity.callingIntent(this))
        finish()
        return true
    }

    override fun onBackKeyEvent(): Boolean {
        return true
    }
}