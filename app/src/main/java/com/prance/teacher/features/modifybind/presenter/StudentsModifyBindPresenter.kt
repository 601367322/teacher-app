package com.prance.teacher.features.modifybind.presenter

import com.prance.lib.common.utils.http.ResultException
import com.prance.teacher.features.students.contract.IStudentsContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.features.match.model.MatchKeyPadModel
import com.prance.teacher.features.modifybind.contract.IStudentsModifyBindContract
import com.prance.teacher.features.modifybind.model.StudentsModifyBindModel
import com.prance.teacher.features.students.model.StudentsModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  下午12:56
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class StudentsModifyBindPresenter : BasePresenterKt<IStudentsModifyBindContract.View>(), IStudentsModifyBindContract.Presenter {

    override val mModel: IStudentsModifyBindContract.Model = StudentsModifyBindModel()

    private val mMatchKeyPadModel = MatchKeyPadModel()

    override fun getKeyPadCount(mUsbSerialNum: String): Int {
        return mMatchKeyPadModel.getAllKeyPadByBaseStationSN(mUsbSerialNum).size
    }

    override fun getStudentsByClassesId(id: String) {
        mModel.getStudentsByClassesId(id)
                .mySubscribe(onSubscribeError) {
                    mView?.renderStudents(it.list)
                }
    }

}

