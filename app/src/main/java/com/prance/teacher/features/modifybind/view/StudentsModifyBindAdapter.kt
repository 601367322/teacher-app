package com.prance.teacher.features.modifybind.view

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.prance.lib.base.extension.invisible
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.GlideApp
import com.prance.teacher.R
import com.prance.teacher.features.match.view.getLastStr
import com.prance.teacher.features.students.model.StudentEntity
import kotlinx.android.synthetic.main.item_students.view.*

class StudentsModifyBindAdapter : BaseQuickAdapter<StudentEntity, BaseViewHolder> {
    constructor(layoutResId: Int) : super(layoutResId)

    override fun convert(helper: BaseViewHolder?, bean: StudentEntity?) {
        bean?.run {
            helper?.run {
                itemView.keyPadId.invisible()
                itemView.name.text = name
                clickerNumber?.run {
                    itemView.keyPadId.visible()
                    itemView.keyPadId.text = getLastStr(this)
                }
                GlideApp.with(itemView)
                        .load(head)
                        .placeholder(R.drawable.default_avatar_boy)
                        .into(itemView.avatar)
            }
        }
    }
}