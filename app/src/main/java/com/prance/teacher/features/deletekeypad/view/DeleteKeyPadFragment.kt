package com.prance.teacher.features.deletekeypad.view

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import com.prance.lib.common.utils.Constants.KEYPAD_LIST
import com.prance.lib.common.utils.weight.AlertDialog
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.teacher.base.weight.FocusGridLayoutManager
import com.prance.teacher.R
import com.prance.teacher.features.deletekeypad.DeleteKeyPadActivity
import com.prance.teacher.features.match.view.MatchedKeyPadAdapter
import kotlinx.android.synthetic.main.fragment_delete_keypad.*
import java.io.Serializable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/19  下午12:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class DeleteKeyPadFragment : BaseFragment(), View.OnClickListener {


    override fun layoutId(): Int = R.layout.fragment_delete_keypad

    companion object {


        fun forData(list: Serializable?): DeleteKeyPadFragment {
            val fragment = DeleteKeyPadFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEYPAD_LIST, list)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mAdapter = MatchedKeyPadAdapter(R.layout.item_delete_key_pad )

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        val list = (arguments?.getSerializable(KEYPAD_LIST) as DeleteKeyPadActivity.SerializableList<KeyPadEntity>).list

        //设置显示格式
        recycler.layoutManager = FocusGridLayoutManager(context!!, 6)

        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect?.bottom = resources.getDimensionPixelOffset(R.dimen.m40_0)
            }
        })

        mAdapter.setOnItemClickListener { _, _, position ->
            val keyPad = mAdapter.getItem(position)
            keyPad?.run {
                context?.run {
                    AlertDialog(this)
                            .setMessage(
                                    Html.fromHtml("""删除编号为 <font color="#3AF0EE">${keyPad.keyId.substring(4)}</font> 的答题器吗？<br/>若答题器已绑定学生，删除后需重新为学生更换绑定的答题器"""))
                            .setCancelButton("取消", null)
                            .setConfirmButton("删除") { _ ->
                                mAdapter.data.remove(keyPad)
                                mAdapter.notifyDataSetChanged()

                                displayCountText()
                            }
                            .show()
                }
            }
        }

        mAdapter.setNewData(list)
        //添加数据源
        recycler.adapter = mAdapter

        complete.setOnClickListener(this)
        cancelBtn.setOnClickListener(this)

        //按钮页面状态初始化
        displayCountText()

    }

    private fun displayCountText() {
        count.text = Html.fromHtml("""已配对 <font color="#3AF0EE">${mAdapter.data.size}</font>""")
    }

    override fun onClick(v: View?) {
        when (v) {
            complete -> {
                val intent = Intent()
                intent.putExtra(KEYPAD_LIST, DeleteKeyPadActivity.SerializableList(mAdapter.data))
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
            cancelBtn -> {
                activity?.finish()
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

}

