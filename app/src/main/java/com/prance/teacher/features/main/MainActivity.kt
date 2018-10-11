package com.prance.teacher.features.main

import android.content.Context
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.Utils
import com.prance.lib.common.utils.weight.AlertDialog
import com.prance.teacher.features.classes.view.ClassesFragment
import com.prance.teacher.utils.SoundUtils
import com.prance.teacher.weight.FloatIcon
import com.prance.teacher.weight.FontCustom

class MainActivity : BaseActivity() {

    override fun fragment(): BaseFragment = ClassesFragment.forAction(ClassesFragment.ACTION_TO_CLASS)

    companion object {

        fun callingIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        //初始化音乐
        SoundUtils.load()

        //初始化字体
        FontCustom.getCOMICSANSMSGRASFont(Utils.getApp())
        FontCustom.getFZY1JWFont(Utils.getApp())
    }

    override fun onBackKeyEvent(): Boolean {
        onBackBtnClick(null)
        return true
    }

    override fun onBackBtnClick(view: View?) {
        if (supportFragmentManager.fragments.size == 1) {
            AlertDialog(this)
                    .setMessage("确认退出此账号？")
                    .setCancelButton("取消", null)
                    .setConfirmButton("退出") { _ ->
                        finish()
                    }
                    .show()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        FloatIcon.hidePopupWindow()
    }
}