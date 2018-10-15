package com.prance.teacher.features.students.presenter

import com.prance.lib.common.utils.http.ResultException
import com.prance.teacher.features.students.contract.IStudentsContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.features.match.model.MatchKeyPadModel
import com.prance.teacher.features.students.model.StudentsModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  下午12:56
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class StudentsPresenter : BasePresenterKt<IStudentsContract.View>(), IStudentsContract.Presenter {

    override val mModel: IStudentsContract.Model = StudentsModel()

    private val mMatchKeyPadModel = MatchKeyPadModel()

    override fun getStudentsByClassesId(id: String) {
        mModel.getStudentsByClassesId(id)
                .mySubscribe(onSubscribeError) {
                    mView?.renderStudents(it.list)
                }
    }

    override fun startBind(classesId: String, serialNumber: String) {
        Flowable
                .create<MutableList<KeyPadEntity>>({
                    val allKeyPad = mMatchKeyPadModel.getAllKeyPadByBaseStationSN(serialNumber)
                    if (allKeyPad.isEmpty()) {
                        it.onError(ResultException(88001, "请先配对答题器，再绑定学员"))
                        it.onComplete()
                    } else {
                        it.onNext(allKeyPad)
                    }
                }, BackpressureStrategy.BUFFER)
                .flatMap {
                    val ids = mutableListOf<String>()
                    for (keyPad in it) {
                        ids.add(keyPad.keyId)
                    }
                    mModel.startBind(classesId, ids)
                }
                .mySubscribe({
                    mView?.bindFail()
                }, {
                    mView?.renderStudents(it.list)
                    mView?.checkMatch()
                })

    }

    override fun getKeyPadCount(mUsbSerialNum: String): Int {
        return mMatchKeyPadModel.getAllKeyPadByBaseStationSN(mUsbSerialNum).size
    }
}

