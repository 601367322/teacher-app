package com.prance.teacher.features.main.view

import android.app.AlertDialog
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.View
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.prance.teacher.R
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.features.main.contract.IMainContract
import com.prance.teacher.features.main.presenter.MainPresenter
import kotlinx.android.synthetic.main.fragment_main.*
import com.prance.teacher.features.check.CheckKeyPadActivity
import com.prance.teacher.features.classes.ClassesActivity
import com.prance.teacher.features.classes.view.ClassesFragment
import com.prance.teacher.features.match.MatchKeyPadActivity
import com.prance.teacher.features.students.view.StudentsFragment


class MainFragment : BaseFragment(), IMainContract.View {

    override var mPresenter: IMainContract.Presenter = MainPresenter()

    override fun layoutId(): Int = R.layout.fragment_main

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        /**
         * 整体居中
         */
        val marginStart = (ScreenUtils.getScreenWidth() - (resources.getDimensionPixelOffset(R.dimen.m520_0) +
                resources.getDimensionPixelOffset(R.dimen.m50_0) +
                resources.getDimensionPixelOffset(R.dimen.m960_0))) / 2
        (startLesson.layoutParams as ConstraintLayout.LayoutParams).marginStart = marginStart


        startLesson.setOnClickListener {
            context?.let {
                startActivity(CheckKeyPadActivity.callingIntent(it, ClassesFragment.ACTION_TO_CLASS))
            }
        }

        check.setOnClickListener {
            if (application.mBaseStation.sn == null) {
                ToastUtils.showShort("请先连接基站")
            } else {
                mPresenter.checkIfKeyPadAlreadyMatched(application.mBaseStation.sn, {
                    context?.let {
                        startActivity(CheckKeyPadActivity.callingIntent(it))
                    }
                }, { ToastUtils.showShort("请先进行答题器配对") }
                )
            }
        }

        matchKeyPad.setOnClickListener {
            mSunVoteService?.let {
                val usbDevice = it.getUserManager().getUsbDevice()
                if (usbDevice != null) {
                    context?.let {
                        startActivity(MatchKeyPadActivity.callingIntent(it))
                    }
                } else {
                    ToastUtils.showShort("请先连接基站")
                }
            }
        }

        bindKeyPad.setOnClickListener {
            context?.let {
                startActivity(ClassesActivity.callingIntent(it, ClassesFragment.ACTION_TO_BIND))
            }
        }

        exit.setOnClickListener {
            AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage("1、退出程序后不能进行课中数据传输\n2、退出程序后，请扫码登录")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", { _, _ ->
                        AppUtils.exitApp()
                    })
                    .show()
        }
    }

    override fun needSunVoteService(): Boolean = true
}