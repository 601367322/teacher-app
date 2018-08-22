package com.prance.teacher.features.students.view

import android.view.View
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.getInflate
import com.prance.lib.teacher.base.ui.BaseRecyclerHolder
import com.prance.teacher.R
import com.prance.teacher.features.students.model.StudentsEntity
import kotlinx.android.synthetic.main.item_students.view.*

class StudentsHolder(itemView: View) :
        BaseRecyclerHolder<StudentsEntity>(getInflate(itemView, R.layout.item_students)) {

    override fun onBind(bean: StudentsEntity?) {

        itemView.keyPadId.visibility = View.GONE

        bean?.let {
            itemView.name.text = it.name
            it.clickers?.run {
                if (isNotEmpty()) {
                    itemView.keyPadId.visibility = View.VISIBLE
                    itemView.keyPadId.text = it.clickers!![0].number
                }
            }
            GlideApp.with(itemView)
                    .load(it.head)
                    .placeholder(R.drawable.default_avatar_boy)
                    .into(itemView.avatar)

        }
    }

}