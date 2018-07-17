package com.prance.teacher.features.match.view

import android.app.Service
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.sunvote.service.SunVoteService
import com.prance.teacher.R
import com.prance.teacher.core.di.IServiceBinder
import com.prance.teacher.core.di.MyServiceConnection
import com.prance.teacher.features.match.contract.IMatchKeyPadContract
import com.prance.teacher.features.match.presenter.MatchKeyPadPresenter
import kotlinx.android.synthetic.main.fragment_match_keypad.*

class MatchKeyPadFragment : BaseFragment(), IMatchKeyPadContract.View, IServiceBinder {

    override var mPresenter: IMatchKeyPadContract.Presenter = MatchKeyPadPresenter()

    override fun layoutId(): Int = R.layout.fragment_match_keypad

    override var mSunVoteService: SunVoteService? = null

    override var mServiceConnection: MyServiceConnection = MyServiceConnection(this)

    private var mAdapter: MatchedKeyPadAdapter = MatchedKeyPadAdapter()

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        //绑定Service
        activity?.run {
            bindService(SunVoteService.callingIntent(this), mServiceConnection, Service.BIND_AUTO_CREATE)
        }

    }

    /**
     * 渲染答题器
     *
     * list 绑定过的答题器列表
     */
    override fun renderKeyPadItemFromDatabase(list: MutableList<KeyPadEntity>) {
        //设置显示格式
        recycler.layoutManager = GridLayoutManager(context!!, 6)
        //添加数据源
        recycler.adapter = mAdapter

        mAdapter.data = list
    }

    override fun onServiceConnected() {
        mSunVoteService?.let {
            val usbDevice = it.getUserManager().getUsbDevice()
            usbDevice?.let {
                //查找已经配对过的答题器
                mPresenter.getMatchedKeyPadByBaseStationId(it.serialNumber)
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