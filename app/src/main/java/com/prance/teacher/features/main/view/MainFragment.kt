package com.prance.teacher.features.main.view

import android.app.Service
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.constraint.ConstraintLayout
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.prance.teacher.R
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.sunvote.service.SunVoteService
import com.prance.teacher.features.exit.ExitActivity
import com.prance.teacher.features.main.contract.IMainContract
import com.prance.teacher.features.main.presenter.MainPresenter
import kotlinx.android.synthetic.main.fragment_main.*
import com.prance.lib.sunvote.service.SunVoteService.MyBinder
import com.prance.teacher.core.di.IServiceBinder
import com.prance.teacher.core.di.MyServiceConnection
import com.prance.teacher.features.bind.BindKeyPadActivity
import com.prance.teacher.features.main.CheckKeyPadTipActivity
import com.prance.teacher.features.match.MatchKeyPadActivity


class MainFragment : BaseFragment(), IMainContract.View ,IServiceBinder{

    override var mPresenter: IMainContract.Presenter = MainPresenter()

    override fun layoutId(): Int = R.layout.fragment_main

    override var mSunVoteService: SunVoteService? = null

    override var mServiceConnection: MyServiceConnection = MyServiceConnection(this)

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        activity?.run {
            startService(SunVoteService.callingIntent(this))
            bindService(SunVoteService.callingIntent(this), mServiceConnection, Service.BIND_AUTO_CREATE)
        }

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
            context?.let {
                startActivity(ExitActivity.callingIntent(it))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        activity?.run {
            unbindService(mServiceConnection)
        }
    }

}