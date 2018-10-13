package com.prance.teacher.features.replacekeypad.contract

import com.prance.lib.base.mvp.*
import com.prance.lib.database.KeyPadEntity
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/19  下午12:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IReplaceKeyPadContract {
    interface View : IView<Presenter> {
        fun renderKeyPadItemFromDatabase(it: MutableList<KeyPadEntity>)
    }
    interface Presenter : IPresenter<View, Model> {
        fun getMatchedKeyPadByBaseStationId(mUsbSerialNum: String)
        fun deleteKeyPad(keyPad: KeyPadEntity): Boolean
    }

    interface Model : IModel {
    }
}
