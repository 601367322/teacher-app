package com.prance.teacher.features.main

import android.content.Context
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import android.content.Intent
import android.view.View
import com.prance.teacher.features.main.view.MainFragment
import com.prance.teacher.utils.IntentUtils

class MainActivity : BaseActivity() {

    override fun fragment(): BaseFragment = MainFragment()

    companion object {

        fun callingIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onBackKeyEvent(): Boolean {
        onBackBtnClick(null)
        return true
    }

    override fun onBackBtnClick(view: View?) {
        try {
            startActivity(IntentUtils.callingTVHome())
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                startActivity(IntentUtils.callingXYHome())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}