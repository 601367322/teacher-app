package com.prance.teacher.features.classes.view

import android.text.Html
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.prance.lib.common.utils.dateFormat_Hour_Min
import com.prance.lib.common.utils.dateFormat_Year_Month_Day_Hour_Min
import com.prance.lib.common.utils.format
import com.prance.lib.server.vo.teacher.ClassVo
import com.prance.teacher.R
import com.prance.teacher.features.classes.ClassesDetailActivity
import com.prance.teacher.features.classes.model.ClassesEntity
import kotlinx.android.synthetic.main.item_classes.view.*

class ClassesAdapter : BaseQuickAdapter<ClassVo, BaseViewHolder> {


    constructor(layoutResId: Int) : super(layoutResId)


    override fun convert(helper: BaseViewHolder?, bean: ClassVo?) {

        bean?.run {
            helper?.run {
                itemView.title.text = name
                itemView.subTitle.text =  course?.name
                itemView.secondTitle.text = lesson?.name
                if (startTime != null && endTime != null) {
                    itemView.date.text = """${format(dateFormat_Year_Month_Day_Hour_Min, startTime!!)}-${format(dateFormat_Hour_Min, endTime!!)}"""
                }
                itemView.teacher.text = """主讲老师：${teacher?.name}"""
                itemView.assistantTeacher.text = """辅导老师：${assistant?.name}"""

                itemView.studentCount.text = Html.fromHtml("""班级人数：<font color="#37D93F">$studentCount</font>""")
                itemView.bindStudentCount.text = Html.fromHtml("""已绑定人数：<font color="#37D93F">$bindingCount</font>""")

            }

        }
    }

}