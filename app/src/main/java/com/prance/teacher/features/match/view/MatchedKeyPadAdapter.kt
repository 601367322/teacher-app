package com.prance.teacher.features.match.view

import android.view.ViewGroup
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.ui.BaseLoadMoreAdapter

class MatchedKeyPadAdapter : BaseLoadMoreAdapter<KeyPadEntity, MatchedKeyPadHolder>() {

    override fun onBind(viewHolder: MatchedKeyPadHolder?, position: Int, data: KeyPadEntity?) {
        LogUtils.d("onBind(viewHolder: MatchedKeyPadHolder?, position: I")
        viewHolder?.onBind(data)
    }

    override fun onCreate(parent: ViewGroup, viewType: Int): MatchedKeyPadHolder {
        LogUtils.d("onCreate(parent: ViewGroup, viewType: Int)")
        return MatchedKeyPadHolder(parent)
    }
}