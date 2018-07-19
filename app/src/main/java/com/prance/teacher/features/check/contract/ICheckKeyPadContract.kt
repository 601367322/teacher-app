package com.prance.teacher.features.check.contract

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
        fun startCheck(it: MutableList<KeyPadEntity>)
    }
    interface Presenter : IPresenter<View, Model> {
        fun getMatchedKeyPadByBaseStationId(serialNumber: String)
        fun generateGroup(mMatchKeyPadEntities: MutableList<KeyPadEntity>, mCheckKeyPadEntities: MutableList<KeyPadEntity>)
    }
    interface Model : IModel {}
}
