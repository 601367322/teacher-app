package com.prance.teacher.features.match.view

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.prance.lib.common.utils.weight.AlertDialog
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.R
import kotlinx.android.synthetic.main.item_match_key_pad.view.*
import org.greenrobot.eventbus.EventBus

class MatchedKeyPadAdapter : BaseQuickAdapter<KeyPadEntity, BaseViewHolder>, View.OnClickListener {


    var isDeleteState: Boolean = false

    constructor(layoutResId: Int) : super(layoutResId)

    override fun convert(helper: BaseViewHolder?, bean: KeyPadEntity?) {
        bean?.run {
            helper?.run {
                itemView.keyNumber.text = """${bean?.keyId}"""

                itemView.keyPadBtn.setTag(R.id.tag_data, bean)
                itemView.keyPadBtn.setOnClickListener(this@MatchedKeyPadAdapter)

                if (isDeleteState) {
                    itemView.keyImage.setImageResource(R.drawable.match_keypad_focus_icon_delete)
                } else {
                    itemView.keyImage.setImageResource(R.drawable.match_keypad_focus_icon)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.keyPadBtn -> {
                if (isDeleteState) {
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