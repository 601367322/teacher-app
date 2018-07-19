package com.prance.teacher.features.classes.view

import android.text.Html
import android.view.View
import com.prance.lib.common.utils.getInflate
import com.prance.lib.teacher.base.ui.BaseRecyclerHolder
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.students.StudentsActivity
import kotlinx.android.synthetic.main.item_classes.view.*

class ClassesHolder(parent: View) : BaseRecyclerHolder<ClassesEntity>(getInflate(parent, R.layout.item_classes)), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.container -> {
                v.context.startActivity(StudentsActivity.callingIntent(v.context, v.getTag(R.id.tag_data) as ClassesEntity))
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
                itemView.bindStateIcon.setImageResource(android.R.drawable.presence_away)
                itemView.bindStateText.text = Html.fromHtml("""已绑定<font color="#27BAFF"> ${bean.binding} </font>人""")
            } else {
                itemView.bindStateIcon.setImageResource(android.R.drawable.presence_busy)
                itemView.bindStateText.text = "未绑定"
            }
            itemView.container.setTag(R.id.tag_data, bean)
            itemView.container.setOnClickListener(this@ClassesHolder)
        }

    }
}