package com.prance.teacher.features.classes.view

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.prance.lib.server.vo.teacher.ClassVo
import com.prance.teacher.R
import com.prance.teacher.features.classes.ClassesDetailActivity
import com.prance.teacher.features.classes.model.ClassesEntity
import kotlinx.android.synthetic.main.item_classes.view.*

class ClassesAdapter : BaseQuickAdapter<ClassVo, BaseViewHolder>, View.OnClickListener {


    constructor(layoutResId: Int) : super(layoutResId)


    override fun convert(helper: BaseViewHolder?, bean: ClassVo?) {

        bean?.run {
            helper?.run {
                itemView.title.text = name
                itemView.secondTitle.text = course?.name
                itemView.date.text = """${startTime}-${endTime}"""
                itemView.teacher.text = teacher?.name

//                if (binding > 0) {
//                    itemView.bindStateIcon.setImageResource(R.drawable.icon_classes_bind_left_icon)
//                    itemView.bindStateText.text = """已绑定 ${bean.binding} 人"""
//                } else {
//                    itemView.bindStateIcon.setImageResource(R.drawable.icon_classes_unbind_left_icon)
//                    itemView.bindStateText.text = "未绑定"
//                }
                itemView.container.setTag(R.id.tag_data, bean)
                itemView.container.setOnClickListener(this@ClassesAdapter)
            }

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.container -> {
                v.context.startActivity(ClassesDetailActivity.callingIntent(v.context, v.getTag(R.id.tag_data) as ClassesEntity))
            }
        }
    }
}