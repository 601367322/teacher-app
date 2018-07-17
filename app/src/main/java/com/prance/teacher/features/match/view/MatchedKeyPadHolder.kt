package com.prance.teacher.features.match.view

import android.view.View
import com.prance.lib.common.utils.getInflate
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.ui.BaseRecyclerHolder
import com.prance.teacher.R

class MatchedKeyPadHolder(itemView: View) :
        BaseRecyclerHolder<KeyPadEntity>(getInflate(itemView, R.layout.item_match_key_pad)) {

    override fun onBind(bean: KeyPadEntity?) {

    }
}