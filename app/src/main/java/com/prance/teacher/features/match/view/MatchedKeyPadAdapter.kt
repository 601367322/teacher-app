package com.prance.teacher.features.match.view

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.R
import kotlinx.android.synthetic.main.item_match_key_pad.view.*

class MatchedKeyPadAdapter : BaseQuickAdapter<KeyPadEntity, BaseViewHolder> {

    constructor(layoutResId: Int) : super(layoutResId)

    override fun convert(helper: BaseViewHolder?, bean: KeyPadEntity?) {
        bean?.run {
            helper?.run {
                itemView.keyNumber.text = """${bean?.keyId.substring(4)}"""
                itemView.keyPadBtn.setTag(R.id.tag_data, bean)
                itemView.keyImage.setImageResource(R.drawable.match_keypad_focus_icon)
            }
        }
    }

}