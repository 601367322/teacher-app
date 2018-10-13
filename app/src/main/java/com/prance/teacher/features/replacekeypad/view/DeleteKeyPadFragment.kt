package com.prance.teacher.features.replacekeypad.view

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.weight.AlertDialog
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.spark.SparkListenerAdapter
import com.prance.lib.spark.SparkService
import com.prance.lib.spark.SparkServicePresenter
import com.prance.teacher.features.replacekeypad.contract.IReplaceKeyPadContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.teacher.base.weight.FocusGridLayoutManager
import com.prance.teacher.R
import com.prance.teacher.features.replacekeypad.presenter.ReplaceKeyPadPresenter
import com.spark.teaching.answertool.usb.model.ReportBindCard
import kotlinx.android.synthetic.main.alert_bind_keypad.*
import kotlinx.android.synthetic.main.fragment_replace.*
import kotlinx.android.synthetic.main.item_replace_key_pad.*

/**
 * Description :
 * @author  Sen
 * @date 2018/7/19  下午12:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class DeleteKeyPadFragment : BaseFragment(), IReplaceKeyPadContract.View, View.OnClickListener {

    override var mPresenter: IReplaceKeyPadContract.Presenter = ReplaceKeyPadPresenter()

    override fun layoutId(): Int = R.layout.fragment_replace

    private var mAdapter: DeleteKeyPadAdapter = DeleteKeyPadAdapter(R.layout.item_replace_key_pad, this)

    private val mSparkServicePresenter: SparkServicePresenter  by lazy {
        SparkServicePresenter(context!!, object : SparkListenerAdapter() {

            override fun onCardBind(reportBindCard: ReportBindCard) {

            }

            override fun onServiceConnected() {
                //查找已经配对过的答题器
                mPresenter.getMatchedKeyPadByBaseStationId(SparkService.mUsbSerialNum!!)
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
        cancelBtn.setOnClickListener(this)

        //按钮页面状态初始化
        displayCountText()

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

        displayCountText()
    }

    private fun displayCountText() {
        count.text = Html.fromHtml("""已配对 <font color="#3AF0EE">${mAdapter.data.size}</font>""")
    }

    override fun onClick(v: View?) {
        when (v) {
            complete -> {
                activity?.finish()
            }
            cancelBtn -> {
                activity?.finish()
            }
            keyPadBtn -> {
                val keyPad = v?.getTag(R.id.tag_data) as KeyPadEntity
                context?.run {
                    AlertDialog(this)
                            .setMessage(
                                    Html.fromHtml("""删除编号为 <font color="#3AF0EE">${keyPad.keyId.substring(4)}</font> 的答题器吗？<br/>若答题器已绑定学生，删除后需重新为学生更换绑定的答题器"""))
                            .setCancelButton("取消", null)
                            .setConfirmButton("删除") { _ ->
                                mAdapter.data.remove(keyPad)
                            }
                            .show()
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

    override fun onDestroy() {
        super.onDestroy()

        mSparkServicePresenter.unBind()
    }

    class BindDialog(context: Context) : Dialog(context, com.prance.lib.common.R.style.DialogStyle) {


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val window = window
            val lp = window.attributes
            lp.gravity = Gravity.CENTER
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT//宽高可设置具体大小

            setContentView(R.layout.alert_bind_keypad)

            GlideApp.with(context)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .load(R.drawable.match_empty_view)
                    .into(emptyImage)

        }


    }

}

