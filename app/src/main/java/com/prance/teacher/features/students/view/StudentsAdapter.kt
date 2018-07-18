package com.prance.teacher.features.students.view

import android.view.ViewGroup
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.ui.BaseLoadMoreAdapter
import com.prance.teacher.features.match.view.MatchedKeyPadHolder
import com.prance.teacher.features.students.model.StudentsEntity

class StudentsAdapter : BaseLoadMoreAdapter<StudentsEntity, StudentsHolder>() {

    var isDeleteState: Boolean = false

    override fun onBind(viewHolder: StudentsHolder?, position: Int, data: StudentsEntity?) {
        viewHolder?.onBind(data)
    }

    override fun onCreate(parent: ViewGroup, viewType: Int): StudentsHolder {
        return StudentsHolder(parent)
    }
}