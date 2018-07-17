package com.prance.teacher.features.match.view

import android.view.ViewGroup
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.ui.BaseRecyclerAdapter
import com.prance.lib.teacher.base.ui.BaseRecyclerHolder

class MatchedKeyPadAdapter : BaseRecyclerAdapter<KeyPadEntity, BaseRecyclerHolder<KeyPadEntity>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerHolder<KeyPadEntity> {
        return MatchedKeyPadHolder(parent)
    }
}