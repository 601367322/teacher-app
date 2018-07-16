package com.prance.teacher.features.match.view

import android.app.Service
import android.os.Bundle
import android.view.View
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.sunvote.service.SunVoteService
import com.prance.teacher.R
import com.prance.teacher.core.di.IServiceBinder
import com.prance.teacher.core.di.MyServiceConnection
import com.prance.teacher.features.match.contract.IMatchKeyPadContract
import com.prance.teacher.features.match.presenter.MatchKeyPadPresenter

class MatchKeyPadFragment : BaseFragment(), IMatchKeyPadContract.View, IServiceBinder {

    override var mPresenter: IMatchKeyPadContract.Presenter = MatchKeyPadPresenter()

    override fun layoutId(): Int = R.layout.fragment_match_keypad

    override var mSunVoteService: SunVoteService? = null
    override var mServiceConnection: MyServiceConnection = MyServiceConnection(this)

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        activity?.run {
            bindService(SunVoteService.callingIntent(this), mServiceConnection, Service.BIND_AUTO_CREATE)
        }

//        mPresenter.getMatchedKeyPadByBaseStationId()
    }

    override fun onDestroy() {
        super.onDestroy()

        activity?.run {
            unbindService(mServiceConnection)
        }
    }
}