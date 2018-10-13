package com.prance.teacher.features.match.view

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prance.lib.base.extension.invisible
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.weight.AlertDialog
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.spark.SparkListenerAdapter
import com.prance.lib.spark.SparkService
import com.prance.lib.spark.SparkServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.teacher.base.weight.FocusGridLayoutManager
import com.prance.teacher.R
import com.prance.teacher.features.match.contract.IMatchKeyPadContract
import com.prance.teacher.features.match.presenter.MatchKeyPadPresenter
import com.prance.teacher.features.replacekeypad.ReplaceKeyPadActivity
import com.spark.teaching.answertool.usb.model.ReportBindCard
import kotlinx.android.synthetic.main.fragment_match_keypad.*

/**
 * 配对答题器
 */
class MatchKeyPadFragment : BaseFragment(), IMatchKeyPadContract.View, View.OnClickListener {

    override var mPresenter: IMatchKeyPadContract.Presenter = MatchKeyPadPresenter()

    override fun layoutId(): Int = R.layout.fragment_match_keypad

    private var mAdapter: MatchedKeyPadAdapter = MatchedKeyPadAdapter(R.layout.item_match_key_pad)

    private val mSparkServicePresenter: SparkServicePresenter  by lazy {
        SparkServicePresenter(context!!, object : SparkListenerAdapter() {

            override fun onCardBind(reportBindCard: ReportBindCard) {


                val keyId = reportBindCard.uid.toString()
                if (!isExists(keyId)) {

                    val keyPadEntity = KeyPadEntity(SparkService.mUsbSerialNum, keyId)

                    mAdapter.addData(keyPadEntity)
                    mAdapter.notifyDataSetChanged()
                    displayMoreBtn()
                }
            }

            override fun onServiceConnected() {
                //查找已经配对过的答题器
                SparkService.mUsbSerialNum?.run {
                    mPresenter.getMatchedKeyPadByBaseStationId(this)
                }
            }

        })
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        //设置显示格式
        recycler.layoutManager = FocusGridLayoutManager(context!!, 6)

        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect?.bottom = resources.getDimensionPixelOffset(R.dimen.m40_0)
            }
        })
        //添加数据源
        recycler.adapter = mAdapter

        complete.setOnClickListener(this)
        replace.setOnClickListener(this)

        //按钮页面状态初始化
        displayMoreBtn()

        mSparkServicePresenter.bind()
    }

    /**
     * 渲染答题器
     *
     * list 绑定过的答题器列表
     */
    override fun renderKeyPadItemFromDatabase(list: MutableList<KeyPadEntity>) {

        mAdapter.setNewData(list)
        mAdapter.notifyDataSetChanged()

        displayMoreBtn()
    }

    /**
     * 检查去重配对
     */
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
            complete.visible()
            replace.visible()
        }
        count.text = Html.fromHtml("""已配对 <font color="#3AF0EE">${mAdapter.data.size}</font>""")
        displayEmptyView()
    }

    override fun onClick(v: View?) {
        when (v) {
            complete -> {
                //保存答题器
                mPresenter.saveAllKeyPad(mAdapter.data)
                activity?.finish()
            }
            replace -> {
                context?.run {
                    startActivity(ReplaceKeyPadActivity.callingIntent(this))
                }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        context?.run {
            AlertDialog(this)
                    .setMessage(
                            "直接退出将不会保存已配对的答题器信息\n确认退出吗？")
                    .setCancelButton("取消", null)
                    .setConfirmButton("退出") { _ ->
                        activity?.finish()
                    }
                    .show()
            return true
        }
        return false
    }

    override fun needEventBus(): Boolean = true

    /**
     * 空状态展示
     */
    private fun displayEmptyView() {

        if (mAdapter.data.isEmpty()) {
            emptyImage.visible()
            recycler.invisible()

            GlideApp.with(this)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .load(R.drawable.match_empty_view1)
                    .into(emptyImage)
        } else {
            emptyImage.invisible()
            recycler.visible()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mSparkServicePresenter.unBind()
    }
}