package com.prance.teacher.features.classes.view

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.prance.teacher.R
import com.prance.teacher.features.classes.ClassesActivity
import com.prance.teacher.features.classes.model.ClassesEntity
import kotlinx.android.synthetic.main.item_classes.view.*

class ClassesAdapter : BaseQuickAdapter<ClassesEntity, BaseViewHolder>, View.OnClickListener {


    constructor(layoutResId: Int) : super(layoutResId)


    override fun convert(helper: BaseViewHolder?, bean: ClassesEntity?) {

        bean?.run {
            helper?.run {
                itemView.title.text = klass?.name
                itemView.secondTitle.text = klass?.course?.name
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
                itemView.container.setOnClickListener(this@ClassesAdapter)
            }

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.container -> {
                if (v.context is ClassesActivity) {
                    (v.context as ClassesActivity).toNext(v.getTag(R.id.tag_data) as ClassesEntity)
                }
            }
        }
    }
}