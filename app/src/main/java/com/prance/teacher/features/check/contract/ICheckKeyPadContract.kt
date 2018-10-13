package com.prance.teacher.features.check.contract

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.prance.lib.base.mvp.*
import com.prance.lib.database.KeyPadEntity

/**
 * Description :
 * @author  Sen
 * @date 2018/7/19  下午5:36
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface ICheckKeyPadContract {
    interface View : IView<Presenter> {
    }
    interface Presenter : IPresenter<View, Model> {
    }
    interface Model : IModel {}
}
