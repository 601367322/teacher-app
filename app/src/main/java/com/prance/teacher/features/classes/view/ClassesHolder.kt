package com.prance.teacher.features.classes.view

import android.graphics.Color
import android.text.Html
import android.view.View
import android.widget.TextView
import com.prance.lib.common.utils.getInflate
import com.prance.lib.teacher.base.ui.BaseRecyclerHolder
import com.prance.teacher.R
import com.prance.teacher.features.classes.ClassesActivity
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.students.StudentsActivity
import kotlinx.android.synthetic.main.item_classes.view.*

class ClassesHolder(parent: View) : BaseRecyclerHolder<ClassesEntity>(getInflate(parent, R.layout.item_classes)), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.container -> {
                if (v.context is ClassesActivity) {
                    (v.context as ClassesActivity).toNext(v.getTag(R.id.tag_data) as ClassesEntity)
                }
            }
        }
    }

    override fun onBind(bean: ClassesEntity?) {
        bean?.run {
            itemView.title.text = klass?.name
            itemView.secondTitle.text = klass?.addr
            itemView.date.text = """${klass?.startTime}-${klass?.endTime}"""
            itemView.teacher.text = klass?.teacher?.name

            if (binding > 0) {
                itemView.leftStateIcon.setImageResource(R.drawable.icon_classes_bind)
                itemView.containerCard.setBackgroundResource(R.drawable.classes_bind_background)
                itemView.bindStateIcon.setImageResource(R.drawable.icon_classes_bind_left_icon)
                itemView.bindStateText.text = """已绑定 ${bean.binding} 人"""

                textShadow(Color.parseColor("#12A693"),itemView.title)
                textShadow(Color.parseColor("#12A693"),itemView.secondTitle)
                textShadow(Color.parseColor("#12A693"),itemView.date)
                textShadow(Color.parseColor("#12A693"),itemView.teacher)
                textShadow(Color.parseColor("#12A693"),itemView.bindStateText)
            } else {
                itemView.leftStateIcon.setImageResource(R.drawable.icon_classes_unbind)
                itemView.containerCard.setBackgroundResource(R.drawable.classes_unbind_background)
                itemView.bindStateIcon.setImageResource(R.drawable.icon_classes_unbind_left_icon)
                itemView.bindStateText.text = "未绑定"

                textShadow(Color.parseColor("#2F66BC"),itemView.title)
                textShadow(Color.parseColor("#2F66BC"),itemView.secondTitle)
                textShadow(Color.parseColor("#2F66BC"),itemView.date)
                textShadow(Color.parseColor("#2F66BC"),itemView.teacher)
                textShadow(Color.parseColor("#2F66BC"),itemView.bindStateText)
            }
            itemView.container.setTag(R.id.tag_data, bean)
            itemView.container.setOnClickListener(this@ClassesHolder)
        }

    }

    private fun textShadow(color: Int, textView: TextView) {
        textView.setShadowLayer(0F, textView.context.resources.getDimensionPixelOffset(R.dimen.m8_0).toFloat(), textView.context.resources.getDimensionPixelOffset(R.dimen.m8_0).toFloat(), color)
    }
}