package com.prance.teacher.features.match.view

import android.view.View
import com.prance.lib.common.utils.getInflate
import com.prance.lib.common.utils.weight.AlertDialog
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.ui.BaseRecyclerHolder
import com.prance.teacher.R
import kotlinx.android.synthetic.main.item_match_key_pad.view.*
import org.greenrobot.eventbus.EventBus

class MatchedKeyPadHolder(parent: View) :
        BaseRecyclerHolder<KeyPadEntity>(getInflate(parent, R.layout.item_match_key_pad)), View.OnClickListener {

    override fun onBind(bean: KeyPadEntity?) {
        itemView.keyNumber.text = """${bean?.keyId}"""

        itemView.keyPadBtn.setTag(R.id.tag_data, bean)
        itemView.keyPadBtn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.keyPadBtn -> {
                if (adapter is MatchedKeyPadAdapter) {
                    if ((adapter as MatchedKeyPadAdapter).isDeleteState) {
                        AlertDialog(v.context)
                                .setMessage("确定删除此答题器配对信息？")
                                .setCancelButton("取消", null)
                                .setConfirmButton("确定", { _ ->
                                    EventBus.getDefault().post(MatchKeyPadFragment.DeleteKeyPadEntityEvent(v.getTag(R.id.tag_data) as KeyPadEntity))
                                })
                                .show()
                    }
                }
            }
        }
    }


}