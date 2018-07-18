package com.prance.teacher.features.classes.view

import android.view.ViewGroup
import com.prance.lib.teacher.base.ui.BaseLoadMoreAdapter
import com.prance.teacher.features.classes.model.ClassesEntity

class ClassesAdapter:BaseLoadMoreAdapter<ClassesEntity,ClassesHolder>(){

    override fun onCreate(parent: ViewGroup?, viewType: Int): ClassesHolder {
        return ClassesHolder(parent!!)
    }

    override fun onBind(viewHolder: ClassesHolder?, position: Int, data: ClassesEntity?) {
        viewHolder?.onBind(data)
    }
}