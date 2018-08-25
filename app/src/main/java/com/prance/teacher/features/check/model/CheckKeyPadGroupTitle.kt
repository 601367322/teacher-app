package com.prance.teacher.features.check.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

data class CheckKeyPadGroupTitle(val title: String, val number: Int):Serializable, MultiItemEntity {
    override fun getItemType(): Int {
        return 1
    }
}