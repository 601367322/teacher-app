package com.prance.teacher.features.login

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseActivity
import com.prance.lib.teacher.base.core.platform.BaseFragment

class LoginActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    override fun fragment(): BaseFragment = LoginFragment()
}