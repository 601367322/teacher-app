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
import kotlinx.android.synthetic.main.item_match_key_pad.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.Subscribe
import java.io.Serializable

class MatchKeyPadFragment : BaseFragment(), IMatchKeyPadContract.View, View.OnClickListener {

    override var mPresenter: IMatchKeyPadContract.Presenter = MatchKeyPadPresenter()

    override fun layoutId(): Int = R.layout.fragment_match_keypad

    private var mAdapter: MatchedKeyPadAdapter = MatchedKeyPadAdapter()


    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        setTip()

        //设置显示格式
        recycler.layoutManager = GridLayoutManager(context!!, 6)
        //添加数据源
        recycler.adapter = mAdapter

        complete.setOnClickListener(this)
        delete.setOnClickListener(this)
    }

    private fun setTip() {
        mRootView?.get()?.post {
            application.mBaseStation.let {
                tip.text = """请长按答题器上“⬇️”键3秒后输入${it?.stationChannel}，再按Ok键进行配对，一个答题器配对完成后才可进行下一个的配对，信息自动保存。"""
            }
        }
    }

    /**
     * 渲染答题器
     *
     * list 绑定过的答题器列表
     */
    override fun renderKeyPadItemFromDatabase(list: MutableList<KeyPadEntity>) {
        mAdapter.data = list
        mAdapter.notifyDataSetChanged()

        displayMoreBtn()
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

    private fun isExists(KeyID: String?): Boolean {
        for (keyPad in mAdapter.data) {
            if (keyPad.keyId == KeyID) {
                return true
            }
        }
        return false
    }

    private fun displayMoreBtn() {
        if (!mAdapter.data.isEmpty()) {
            complete.visibility = View.VISIBLE
            delete.visibility = View.VISIBLE
        }
        count.text = """已配对成功${mAdapter.data.size}个答题器"""
    }

    override fun onKeyEventCallBack(KeyID: String?, iMode: Int, Time: Float, sInfo: String?) {
        when (iMode) {
            SunARS.KeyResult_loginInfo, SunARS.KeyResult_match -> {
                launch(UI) {
                    if (!isExists(KeyID)) {
                        mAdapter.addData(KeyPadEntity(application.mBaseStation.sn, KeyID))
                        mAdapter.notifyDataSetChanged()

                        displayMoreBtn()
                    }
                }
            }
        }
    }

    override fun needSunVoteService(): Boolean = true

    override fun onClick(v: View?) {
        when (v) {
            complete -> {
                mPresenter.saveAllMatchedKeyPad(application.mBaseStation.sn, mAdapter.data)
            }
            delete -> {
                //删除状态
                mAdapter.isDeleteState = !mAdapter.isDeleteState

                complete.isEnabled = false
                delete.isEnabled = false

                tip.text = "请用方向键选择，并按“OK”键进行删除，按“A1”键继续配对。"
                //最后一个答题器获取焦点
                recycler.getChildAt(mAdapter.data.size - 1).keyPadBtn.requestFocus()
            }
        }
    }

    override fun needEventBus(): Boolean = true

    @Subscribe
    fun onEvent(bean: RefreshMatchedKeyPadFragment) {
        displayMoreBtn()
        //最后一个答题器获取焦点
        recycler.getChildAt(mAdapter.data.size - 1)?.keyPadBtn?.requestFocus()
    }

    class RefreshMatchedKeyPadFragment : Serializable

    override fun onSaveKeyPadSuccess() {
        activity?.finish()
    }

    fun onBackKeyEvent(): Boolean {
        if (mAdapter.isDeleteState) {
            mAdapter.isDeleteState = false
            complete.isEnabled = true
            delete.isEnabled = true
            return true
        }
        return false
    }

}