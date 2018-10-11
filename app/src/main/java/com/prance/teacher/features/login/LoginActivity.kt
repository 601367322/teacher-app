package com.prance.teacher.features.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.prance.lib.base.extension.close
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.features.login.view.LoginFragment
import com.prance.teacher.features.login.view.UpdateFragment
import com.prance.teacher.features.main.MainActivity

class LoginActivity : BaseActivity() {


    companion object {
        fun callingIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    override fun fragment(): BaseFragment = LoginFragment()

    override fun onBackKeyEvent(): Boolean {
        val fragments = supportFragmentManager.fragments
        for (fragment in fragments) {
            if(fragment is UpdateFragment){
                return true
            }
        }
        return super.onBackKeyEvent()
    }
}