package com.prance.lib.test.setting.features

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseActivity
import com.prance.lib.base.platform.BaseFragment

class TestSettingActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context): Intent {
            val intent = Intent(context, TestSettingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return intent
        }
    }

    override fun fragment(): BaseFragment = TestSettingFragment()
}