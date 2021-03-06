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
    interface MainView : IView<MainPresenter> {}

    interface Presenter : IPresenter<View, Model> {
        fun checkIfKeyPadAlreadyMatched(serialNumber: String, matched: () -> Unit, unMatch: () -> Unit)
    }

    interface MainPresenter : IPresenter<View, Model> {
        fun logOut()
    }

    interface Model : IModel {
        fun logOut(): Flowable<Any>

    }
}
