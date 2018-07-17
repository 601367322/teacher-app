package com.prance.teacher.features.match.view

import android.view.ViewGroup
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.ui.BaseLoadMoreAdapter

class MatchedKeyPadAdapter : BaseLoadMoreAdapter<KeyPadEntity, MatchedKeyPadHolder>() {

    override fun onBind(viewHolder: MatchedKeyPadHolder?, position: Int, data: KeyPadEntity?) {
        viewHolder?.onBind(data)
    }

    override fun onCreate(parent: ViewGroup, viewType: Int): MatchedKeyPadHolder {
        return MatchedKeyPadHolder(parent)
    }
}