package com.prance.teacher.features.replacekeypad.contract

import com.prance.lib.base.mvp.*
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/19  下午12:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IReplaceKeyPadContract {
    interface View : IView<Presenter> {
        fun replaceSuccess()
    }
    interface Presenter : IPresenter<View, Model> {
        fun replaceKeyPad(baseStationId: String, classesId: String, oldKeyPadId: String, newKeyPadId: String)
    }

    interface Model : IModel {
        fun replaceKeyPad(classesId: String, oldKeyPadId: String, newKeyPadId: String): Flowable<Any>
    }
}
