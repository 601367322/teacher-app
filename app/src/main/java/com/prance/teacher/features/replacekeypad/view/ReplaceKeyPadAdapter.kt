package com.prance.teacher.features.replacekeypad.view

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.R
import kotlinx.android.synthetic.main.item_match_key_pad.view.*

class ReplaceKeyPadAdapter(layoutResId: Int, var itemClickListener: View.OnClickListener) : BaseQuickAdapter<KeyPadEntity, BaseViewHolder>(layoutResId) {

    override fun convert(helper: BaseViewHolder?, bean: KeyPadEntity?) {
        bean?.run {
            helper?.run {
                itemView.keyNumber.text = bean.keyId.substring(4)
                itemView.keyPadBtn.setTag(R.id.tag_data, bean)
                itemView.keyImage.setImageResource(R.drawable.match_keypad_focus_icon)
                itemView.keyPadBtn.setTag(R.id.tag_data, bean)
                itemView.keyPadBtn.setOnClickListener(itemClickListener)
            }
        }
    }

}