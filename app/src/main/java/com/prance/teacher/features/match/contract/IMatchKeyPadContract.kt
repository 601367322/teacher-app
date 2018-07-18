package com.prance.teacher.features.match.contract

import com.prance.lib.base.mvp.*
import com.prance.lib.database.KeyPadEntity

/**
 * Description :
 * @author  Sen
 * @date 2018/7/16  下午3:32
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IMatchKeyPadContract {
    interface View : IView<Presenter> {
        fun renderKeyPadItemFromDatabase(list: MutableList<KeyPadEntity>)
        fun onSaveKeyPadSuccess()
    }

    interface Presenter : IPresenter<View, Model> {
        fun getMatchedKeyPadByBaseStationId(serialNumber: String)
        fun saveAllMatchedKeyPad(serialNumber: String, data: List<KeyPadEntity>)
        fun saveMatchedKeyPad(keyPadEntity: KeyPadEntity): KeyPadEntity?
        fun deleteKeyPad(keyPadEntity: KeyPadEntity): Boolean
    }

    interface Model : IModel {
        fun saveAllMatchedKeyPad(data: List<KeyPadEntity>): Boolean
        fun deleteKeyPad(serialNumber: String): Boolean
        fun saveMatchedKeyPad(keyPadEntity: KeyPadEntity): KeyPadEntity?
        fun deleteKeyPad(keyPadEntity: KeyPadEntity): Boolean
    }
}
