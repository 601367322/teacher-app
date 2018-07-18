package com.prance.teacher.features.classes.view

import android.view.View
import com.prance.lib.common.utils.getInflate
import com.prance.lib.teacher.base.ui.BaseRecyclerHolder
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity

class ClassesHolder(parent: View):BaseRecyclerHolder<ClassesEntity>(getInflate(parent, R.layout.item_classes)){


    override fun onBind(bean: ClassesEntity?) {

    }
}