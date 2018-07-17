package com.prance.teacher.features.match.view

import android.app.AlertDialog
import android.view.View
import com.prance.lib.common.utils.getInflate
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.ui.BaseRecyclerHolder
import com.prance.teacher.R
import kotlinx.android.synthetic.main.item_match_key_pad.view.*
import org.greenrobot.eventbus.EventBus

class MatchedKeyPadHolder(itemView: View) :
        BaseRecyclerHolder<KeyPadEntity>(getInflate(itemView, R.layout.item_match_key_pad)), View.OnClickListener {

    override fun onBind(bean: KeyPadEntity?) {
        itemView.keyPadBtn.text = """答题器编号${bean?.keyId}"""

        itemView.keyPadBtn.setTag(R.id.tag_data, bean)
        itemView.keyPadBtn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.keyPadBtn -> {
                if (adapter is MatchedKeyPadAdapter) {
                    if ((adapter as MatchedKeyPadAdapter).isDeleteState) {
                        AlertDialog.Builder(v.context)
                                .setMessage("确定删除此答题器配对信息？")
                                .setNegativeButton("取消", null)
                                .setPositiveButton("确定", { _, _ ->
                                    //删除答题器
                                    adapter.removeData(v.getTag(R.id.tag_data))
                                    adapter.notifyDataSetChanged()

                                    EventBus.getDefault().post(MatchKeyPadFragment.RefreshMatchedKeyPadFragment())
                                })
                                .show()
                    }
                }
            }
        }
    }


}