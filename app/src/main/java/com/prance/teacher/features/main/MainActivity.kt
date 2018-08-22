package com.prance.teacher.features.main

import android.content.Context
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.Utils
import com.prance.teacher.BuildConfig
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.main.view.MainFragment
import com.prance.teacher.features.subject.SubjectActivity
import com.prance.teacher.utils.SoundUtils
import com.prance.teacher.weight.FloatButton
import com.prance.teacher.weight.FontCustom

class MainActivity : BaseActivity() {

    override fun fragment(): BaseFragment = MainFragment()

    companion object {

        fun callingIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        SoundUtils.load()

        super.initView(savedInstanceState)

        //初始化字体
        FontCustom.getCOMICSANSMSGRASFont(Utils.getApp())
        FontCustom.getFZY1JWFont(Utils.getApp())

        if (BuildConfig.DEBUG) {
//            val classes = ClassesEntity(1)
//            startActivity(ClassesDetailActivity.callingIntent(this, classes))
        }
    }

    override fun onBackKeyEvent(): Boolean {
        onBackBtnClick(null)
        return true
    }

    override fun onBackBtnClick(view: View?) {
        if (supportFragmentManager.fragments.size == 1) {
            moveTaskToBack(true)
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        FloatButton.hidePopupWindow()
    }
}