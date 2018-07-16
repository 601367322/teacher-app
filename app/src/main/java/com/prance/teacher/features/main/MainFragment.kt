package com.prance.teacher.features.main

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.support.v17.leanback.app.BackgroundManager
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.prance.teacher.R
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.sunvote.service.SunVoteService
import com.prance.teacher.features.exit.ExitActivity
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_main

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        activity?.run {
            startService(SunVoteService.callingIntent(this))
        }

        BackgroundManager.getInstance(activity).color = Color.WHITE

        startLesson.setOnClickListener {
            ToastUtils.showShort("开始上课")
        }

        checkKeyPad.setOnClickListener {
            ToastUtils.showShort("答题器检测")
        }

        matchKeyPad.setOnClickListener {
            ToastUtils.showShort("答题器配对")
        }

        bindKeyPad.setOnClickListener {
            ToastUtils.showShort("答题器绑定")
        }

        back.setOnClickListener {
            activity?.finish()
        }

        exit.setOnClickListener {
            context?.let {
                startActivity(ExitActivity.callingIntent(it))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        activity?.run {
            stopService(SunVoteService.callingIntent(this))
        }
    }

}