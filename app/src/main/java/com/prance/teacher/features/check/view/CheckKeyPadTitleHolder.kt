package com.prance.teacher.features.check.view

import android.text.Html
import android.view.View
import com.prance.lib.common.utils.getInflate
import com.prance.lib.teacher.base.ui.BaseRecyclerHolder
import com.prance.teacher.R
import com.prance.teacher.features.check.model.CheckKeyPadGroupTitle
import kotlinx.android.synthetic.main.item_check_group_title.view.*

class CheckKeyPadTitleHolder(parent: View) : BaseRecyclerHolder<CheckKeyPadGroupTitle>(getInflate(parent, R.layout.item_check_group_title)) {

    override fun onBind(bean: CheckKeyPadGroupTitle?) {
        bean?.run {
            itemView.title.text = Html.fromHtml("""$title<font color="#3AF0EE">$number</font>个""")
        }
    }
}