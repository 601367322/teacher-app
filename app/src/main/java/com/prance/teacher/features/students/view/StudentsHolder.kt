package com.prance.teacher.features.students.view

import android.view.View
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
            if (layoutPosition % 2 == 0)
                itemView.avatar.setImageResource(R.drawable.default_circle_avatar_boy)
            else
                itemView.avatar.setImageResource(R.drawable.default_circle_avatar_girl)
        }
    }

}