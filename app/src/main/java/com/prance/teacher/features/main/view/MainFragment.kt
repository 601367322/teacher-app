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
import com.prance.teacher.features.bind.BindKeyPadActivity
import com.prance.teacher.features.main.CheckKeyPadTipActivity
import com.prance.teacher.features.match.MatchKeyPadActivity


class MainFragment : BaseFragment(), IMainContract.View {

    override var mPresenter: IMainContract.Presenter = MainPresenter()

    override fun layoutId(): Int = R.layout.fragment_main

    var mSunVoteService: SunVoteService? = null

    private var mServiceConnection: MyServiceConnection? = null

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        activity?.run {
            mServiceConnection = MyServiceConnection()
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
                val usbDevice = it.mUsbManagerImpl.getUsbDevice()
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
            context?.let {
                startActivity(MatchKeyPadActivity.callingIntent(it))
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

    inner class MyServiceConnection : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val myBinder = service as MyBinder
            mSunVoteService = myBinder.service
        }
    }

}