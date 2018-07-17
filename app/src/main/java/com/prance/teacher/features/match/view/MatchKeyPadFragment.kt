package com.prance.teacher.features.match.view

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import cn.sunars.sdk.SunARS
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.match.contract.IMatchKeyPadContract
import com.prance.teacher.features.match.presenter.MatchKeyPadPresenter
import kotlinx.android.synthetic.main.fragment_match_keypad.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class MatchKeyPadFragment : BaseFragment(), IMatchKeyPadContract.View {

    override var mPresenter: IMatchKeyPadContract.Presenter = MatchKeyPadPresenter()

    override fun layoutId(): Int = R.layout.fragment_match_keypad

    private var mAdapter: MatchedKeyPadAdapter = MatchedKeyPadAdapter()

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        setTip()

    }

    private fun setTip() {
        mRootView?.get()?.post {
            application.mBaseStation.let {
                tip.text = """${it?.stationId}\t${it?.stationChannel}\t+${it?.sn}"""
            }
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

    override fun onHDParamCallBack(iBaseID: Int, iMode: Int, sInfo: String?) {
        when (iMode) {
        //基站主信道
            SunARS.BaseStation_Channel -> {
                application.mBaseStation.stationChannel = sInfo?.toLong()
                setTip()
            }
        }
    }

    override fun onKeyEventCallBack(KeyID: String?, iMode: Int, Time: Float, sInfo: String?) {
        when (iMode) {
            SunARS.KeyResult_loginInfo, SunARS.KeyResult_match -> {
                launch(UI) {
                    mAdapter.addData(KeyPadEntity(application.mBaseStation.sn,KeyID))
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
    }

}