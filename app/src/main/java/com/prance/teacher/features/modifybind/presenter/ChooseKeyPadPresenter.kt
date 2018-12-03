package com.prance.teacher.features.modifybind.presenter

import com.prance.teacher.features.modifybind.contract.IChooseKeyPadContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.features.match.model.MatchKeyPadModel
import com.prance.teacher.features.modifybind.model.ChooseKeyPadModel
import com.prance.teacher.features.students.model.StudentEntity
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/10/17  1:49 PM
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class ChooseKeyPadPresenter : BasePresenterKt<IChooseKeyPadContract.View>(), IChooseKeyPadContract.Presenter {

    override fun getMatchedKeyPadByBaseStationId(serialNumber: String) {
        Flowable.create<MutableList<KeyPadEntity>>({
            val list = mMatchKeyPadModel.getAllKeyPadByBaseStationSN(serialNumber)
            if (list.isNotEmpty()) {
                it.onNext(list)
                it.onComplete()
            }
        }, BackpressureStrategy.BUFFER)
                .mySubscribe {
                    mView?.renderKeyPadItemFromDatabase(it)
                }

    }


    override fun saveMatchedKeyPad(keyPadEntity: KeyPadEntity): KeyPadEntity? {
        return mMatchKeyPadModel.saveMatchedKeyPad(keyPadEntity)
    }


    override fun modifyBind(classId: String, student: StudentEntity, keyPadEntity: KeyPadEntity) {
        val oldKeyPadId = student.clickerNumber
        mModel.modifyBind(classId, student.clickerNumber!!, keyPadEntity.keyId)
                .mySubscribe(onSubscribeError) {
                    //将答题器保存数据库
                    mMatchKeyPadModel.saveMatchedKeyPad(keyPadEntity)

                    student.clickerNumber = keyPadEntity.keyId

                    try {
                        mView?.modifySuccess(student, oldKeyPadId!!)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
    }

    override val mModel: IChooseKeyPadContract.Model = ChooseKeyPadModel()

    private val mMatchKeyPadModel = MatchKeyPadModel()
}

