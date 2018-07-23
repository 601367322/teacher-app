package com.prance.teacher.features.match.view

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import cn.sunars.sdk.SunARS
import com.bumptech.glide.Glide
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.ImageConfig
import com.prance.lib.common.utils.ImageLoaderFactory
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.teacher.base.weight.FocusGridLayoutManager
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
        recycler.layoutManager = FocusGridLayoutManager(context!!, 6)

        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect?.bottom = resources.getDimensionPixelOffset(R.dimen.m36_0)
            }
        })
        //添加数据源
        recycler.adapter = mAdapter

        complete.setOnClickListener(this)
        delete.setOnClickListener(this)

        GlideApp.with(this)
                .asGif()
                .load(R.drawable.match_loading)
                .into(loadingImg)
    }

    private fun setTip() {
        mRootView?.get()?.post {
            application.mBaseStation.let {
                tip1.visibility = View.VISIBLE
                tip2.visibility = View.GONE
                tip_text2.text = """“${it?.stationChannel}”，再按“OK”键进行配对"""
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
        count.text = Html.fromHtml("""已配对成功<font color="#3AF0EE">${mAdapter.data.size}</font>个答题器""")
    }

    override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
        when (iMode) {
            SunARS.KeyResult_loginInfo, SunARS.KeyResult_match -> {
                launch(UI) {
                    KeyID?.let {
                        val keyId = generateKeyPadId(it)
                        if (!isExists(keyId)) {
                            //保存答题器
                            val keyPadEntity = mPresenter.saveMatchedKeyPad(KeyPadEntity(application.mBaseStation.sn, keyId))

                            keyPadEntity?.let {
                                mAdapter.addData(it)
                                mAdapter.notifyDataSetChanged()
                                recycler.post {
                                    setLastItemRequestFocus()
                                }
                                displayMoreBtn()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun needSunVoteService(): Boolean = true

    override fun onClick(v: View?) {
        when (v) {
            complete -> {
                activity?.finish()
            }
            delete -> {
                //删除状态
                mAdapter.isDeleteState = !mAdapter.isDeleteState

                tip1.visibility = View.GONE
                tip2.visibility = View.VISIBLE

                tip_text3.text = "请用方向键选择，并按“OK”键进行删除，按返回键继续配对。"
                recycler.post {
                    setLastItemRequestFocus()

                    recycler.post {
                        complete.isEnabled = false
                        delete.isEnabled = false
                    }
                }
            }
        }
    }

    private fun setLastItemRequestFocus() {
        val lastItem = recycler.layoutManager.findViewByPosition(mAdapter.data.size - 1);
        if (lastItem == null) {
            recycler.smoothScrollToPosition(mAdapter.data.size - 1)

            recycler.postDelayed({
                //最后一个答题器获取焦点
                recycler.layoutManager.findViewByPosition(mAdapter.data.size - 1).requestFocus()
            }, 250)
        } else {
            lastItem.requestFocus()
        }
    }

    override fun needEventBus(): Boolean = true

    @Subscribe
    fun onEvent(bean: DeleteKeyPadEntityEvent) {
        recycler.post {
            //删除答题器
            bean.keyPadEntity?.let {
                if (mPresenter.deleteKeyPad(it)) {
                    ToastUtils.showShort("删除成功")
                }

                var pos = mAdapter.indexOfData(it)

                //删除答题器
                mAdapter.removeData(it)
                mAdapter.notifyDataSetChanged()

                if (pos >= mAdapter.data.size) {
                    pos -= 1
                }
                if (pos >= 0) {
                    recycler.post {
                        recycler.layoutManager.findViewByPosition(pos).requestFocus()
                    }
                }
            }

            displayMoreBtn()
        }
    }

    class DeleteKeyPadEntityEvent(val keyPadEntity: KeyPadEntity?) : Serializable

    override fun onSaveKeyPadSuccess() {
        activity?.finish()
    }

    fun onBackKeyEvent(): Boolean {
        if (mAdapter.isDeleteState) {
            mAdapter.isDeleteState = false
            complete.isEnabled = true
            delete.isEnabled = true

            setTip()
            return true
        }
        return false
    }

}