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
import com.prance.teacher.features.bind.BindKeyPadActivity
import com.prance.teacher.features.main.CheckKeyPadTipActivity
import com.prance.teacher.features.match.MatchKeyPadActivity


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
            LogUtils.d("开始上课")

            context?.let {
                startActivity(CheckKeyPadTipActivity.callingIntent(it))
            }
        }

        checkKeyPad.setOnClickListener {
            LogUtils.d("答题器检测")

            mSunVoteService?.let {
                val usbDevice = it.getUserManager().getUsbDevice()
                if (usbDevice != null) {
                    mPresenter.checkIfKeyPadAlreadyMatched(usbDevice.serialNumber, {
                        context?.let {
                            startActivity(CheckKeyPadTipActivity.callingIntent(it))
                        }
                    }, { ToastUtils.showShort("请先进行答题器配对") }
                    )
                } else {
                    ToastUtils.showShort("请先连接基站")
                }
            }
        }

        matchKeyPad.setOnClickListener {
            LogUtils.d("答题器配对")
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
            LogUtils.d("答题器绑定")
            context?.let {
                startActivity(BindKeyPadActivity.callingIntent(it))
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