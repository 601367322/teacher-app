package com.prance.teacher.features.match.view

import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.common.utils.getInflate
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.ui.BaseRecyclerHolder
import com.prance.teacher.R
import kotlinx.android.synthetic.main.item_match_key_pad.view.*

class MatchedKeyPadHolder(itemView: View) :
        BaseRecyclerHolder<KeyPadEntity>(getInflate(itemView, R.layout.item_match_key_pad)) {

    override fun onBind(bean: KeyPadEntity?) {
        LogUtils.d(bean?.keyId)
        itemView.keyPadBtn.text = bean?.keyId
    }
}