package com.prance.teacher.features.main.view

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.common.utils.weight.AlertDialog
import com.prance.teacher.R
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.test.setting.features.TestSettingActivity
import com.prance.teacher.features.main.contract.IMainContract
import com.prance.teacher.features.main.presenter.MainPresenter
import kotlinx.android.synthetic.main.fragment_main.*
import com.prance.teacher.features.check.CheckKeyPadActivity
import com.prance.teacher.features.classes.ClassesActivity
import com.prance.teacher.features.classes.view.ClassesFragment
import com.prance.teacher.features.match.MatchKeyPadActivity
import com.prance.teacher.utils.IntentUtils


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
            if (application.mBaseStation.sn == null) {
                ToastUtils.showShort("请先连接基站")
            } else {
                context?.let {
                    startActivity(MatchKeyPadActivity.callingIntent(it))
                }
            }
        }

        bindKeyPad.setOnClickListener {
            context?.let {
                startActivity(ClassesActivity.callingIntent(it, ClassesFragment.ACTION_TO_BIND))
            }
        }

        exit.setOnClickListener {
            AlertDialog(context!!)
                    .setMessage("1、退出程序后不能进行课中数据传输\n2、退出程序后，请扫码登录")
                    .setCancelButton("取消", null)
                    .setConfirmButton("确定", {

                    })
                    .show()
        }
    }

    override fun needSunVoteService(): Boolean = true
}