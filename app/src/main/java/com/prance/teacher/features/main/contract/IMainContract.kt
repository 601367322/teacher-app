package com.prance.teacher.features.main.contract

import com.prance.lib.base.mvp.*
import com.prance.lib.database.KeyPadEntity
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/16  下午12:00
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IMainContract {
    interface View : IView<Presenter> {}
    interface Presenter : IPresenter<View, Model> {
        fun checkIfKeyPadAlreadyMatched(serialNumber: String, matched: () -> Unit, unMatch: () -> Unit)
    }

    interface Model : IModel {
        fun getAllKeyPadByBaseStationSN(serialNumber: String): MutableList<KeyPadEntity>?
    }
}
