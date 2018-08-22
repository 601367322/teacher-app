package com.prance.teacher.features.classes.view

import android.view.View
import com.prance.lib.common.utils.getInflate
import com.prance.lib.teacher.base.ui.BaseRecyclerHolder
import com.prance.teacher.R
import com.prance.teacher.features.classes.ClassesActivity
import com.prance.teacher.features.classes.model.ClassesEntity
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
                itemView.bindStateIcon.setImageResource(R.drawable.icon_classes_bind_left_icon)
                itemView.bindStateText.text = """已绑定 ${bean.binding} 人"""
            } else {
                itemView.bindStateIcon.setImageResource(R.drawable.icon_classes_unbind_left_icon)
                itemView.bindStateText.text = "未绑定"
            }
            itemView.container.setTag(R.id.tag_data, bean)
            itemView.container.setOnClickListener(this@ClassesHolder)
        }

    }

}