package com.prance.teacher.features.check.view

import android.view.View
import com.prance.lib.common.utils.getInflate
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.ui.BaseRecyclerHolder
import com.prance.teacher.R
import com.prance.teacher.R.id.keyPadBtn
import kotlinx.android.synthetic.main.item_match_key_pad.view.*

class CheckKeyPadHolder(parent: View) : BaseRecyclerHolder<KeyPadEntity>(getInflate(parent, R.layout.item_match_key_pad)) {

    override fun onBind(bean: KeyPadEntity?) {
        bean?.let {
            itemView.keyPadBtn.text = bean.keyId
        }
    }
}