package com.prance.teacher.features.login

import android.content.Context
import android.content.Intent
import com.prance.teacher.lib.base.core.platform.BaseActivity
import com.prance.teacher.core.platform.BaseFragment

class LoginActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    override fun fragment(): BaseFragment = LoginFragment()
}