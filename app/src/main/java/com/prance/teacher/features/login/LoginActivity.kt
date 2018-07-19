package com.prance.teacher.features.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.features.login.view.LoginFragment
import com.prance.teacher.features.main.MainActivity

class LoginActivity : BaseActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        //防止重复启动
        if (!this.isTaskRoot) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            //如果你就放在launcher Activity中话，这里可以直接return了
            val mainIntent = intent
            val action = mainIntent.action
            if (mainIntent.hasCategory(Intent.CATEGORY_LEANBACK_LAUNCHER) && action == Intent.ACTION_MAIN) {
                finish()
                return //finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }
        super.initView(savedInstanceState)
    }

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