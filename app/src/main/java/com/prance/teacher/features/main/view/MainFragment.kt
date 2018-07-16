package com.prance.teacher.features.main.view

import android.app.Service
import android.content.ComponentName
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.support.v17.leanback.app.BackgroundManager
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.prance.teacher.R
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.sunvote.service.SunVoteService
import com.prance.teacher.features.exit.ExitActivity
import com.prance.teacher.features.main.contract.IMainContract
import com.prance.teacher.features.main.presenter.MainPresenter
import kotlinx.android.synthetic.main.fragment_main.*
import com.prance.lib.sunvote.service.SunVoteService.MyBinder


class MainFragment : BaseFragment(), IMainContract.View {

    override var mPresenter: IMainContract.Presenter = MainPresenter()

    override fun layoutId(): Int = R.layout.fragment_main

    var mSunVoteService: SunVoteService? = null

    private var mServiceConnection: MyServiceConnection? = null

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        activity?.run {
            mServiceConnection = MyServiceConnection()
            bindService(SunVoteService.callingIntent(this), mServiceConnection, Service.BIND_AUTO_CREATE)
        }

        BackgroundManager.getInstance(activity).color = Color.WHITE

        startLesson.setOnClickListener {
            ToastUtils.showShort("开始上课")

            mSunVoteService?.let {
                mPresenter.checkIfKeyPadAlreadyMatched(it.mUsbManagerImpl.getUsbDevice()?.serialNumber)
            }
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