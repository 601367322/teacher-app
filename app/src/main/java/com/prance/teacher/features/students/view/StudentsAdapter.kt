package com.prance.teacher.features.students.view

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.prance.lib.base.extension.invisible
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.GlideApp
import com.prance.teacher.R
import com.prance.teacher.features.students.model.StudentsEntity
import kotlinx.android.synthetic.main.item_students.view.*

class StudentsAdapter : BaseQuickAdapter<StudentsEntity, BaseViewHolder> {
    constructor(layoutResId: Int) : super(layoutResId)

    override fun convert(helper: BaseViewHolder?, bean: StudentsEntity?) {
        bean?.run {
            helper?.run {
                itemView.keyPadId.invisible()
                itemView.name.text = name
                clickers?.run {
                    if (isNotEmpty()) {
                        itemView.keyPadId.visible()
                        itemView.keyPadId.text = clickers!![0].number?.substring(4)
                    }
                }
                GlideApp.with(itemView)
                        .load(head)
                        .placeholder(R.drawable.default_avatar_boy)
                        .into(itemView.avatar)
            }
        }
    }
}