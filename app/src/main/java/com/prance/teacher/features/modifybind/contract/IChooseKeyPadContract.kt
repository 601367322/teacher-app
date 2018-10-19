package com.prance.teacher.features.modifybind.contract

import com.prance.lib.base.mvp.*
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.features.students.model.StudentEntity
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/10/17  1:49 PM
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IChooseKeyPadContract {
    interface View : IView<Presenter> {

        fun renderKeyPadItemFromDatabase(list: MutableList<KeyPadEntity>)

        fun modifySuccess(student: StudentEntity)
    }

    interface Presenter : IPresenter<View, Model> {

        fun getMatchedKeyPadByBaseStationId(serialNumber: String)

        fun saveMatchedKeyPad(keyPadEntity: KeyPadEntity): KeyPadEntity?

        fun modifyBind(classId: String, student: StudentEntity, keyPadEntity: KeyPadEntity)
    }

    interface Model : IModel {
        fun modifyBind(classId: String, oldKeyPadId: String, newKeyPadId: String): Flowable<Any>
    }
}
