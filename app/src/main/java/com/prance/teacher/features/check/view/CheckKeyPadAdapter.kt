package com.prance.teacher.features.check.view

import android.text.Html

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.R
import com.prance.teacher.features.check.model.CheckKeyPadGroupTitle

/**
 * @author bingbing
 */
class CheckKeyPadAdapter(data: List<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    companion object {
        const val TITLE_TYPE = 1
        const val ITEM_TYPE = 2
    }

    init {
        addItemType(TITLE_TYPE, R.layout.item_check_group_title)
        addItemType(ITEM_TYPE, R.layout.item_check_key_pad)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (helper.itemViewType) {
            TITLE_TYPE -> {
                val bean = item as CheckKeyPadGroupTitle
                bean.run {
                    helper.setText(R.id.title, Html.fromHtml("""$title<font color="#3AF0EE">$number</font>个"""))
                }
            }
            ITEM_TYPE -> {
                val bean = item as KeyPadEntity
                bean.run {
                    helper.setText(R.id.keyNumber, keyId)
                    when (status) {
                        KeyPadEntity.OFFLINE -> helper.setImageResource(R.id.keyPadBackground, R.drawable.bg_keypad_background_offline)
                        else -> helper.setImageResource(R.id.keyPadBackground, R.drawable.bg_keypad_background_error)
                    }
                }
            }
        }
    }
}
