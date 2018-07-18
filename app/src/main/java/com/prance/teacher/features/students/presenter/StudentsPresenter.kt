package com.prance.teacher.features.students.presenter

import com.prance.teacher.features.students.contract.IStudentsContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.base.mvp.mySubscribe
import com.prance.teacher.features.students.model.StudentsModel

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  下午12:56
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class StudentsPresenter : BasePresenterKt<IStudentsContract.View>(), IStudentsContract.Presenter {

    override val mModel: IStudentsContract.Model = StudentsModel()

    override fun getStudentsByClassesId(id: String) {
        mModel.getStudentsByClassesId(id)
                .mySubscribe(onSubscribeError, {
                    mView?.renderStudents(it.list)
                })
    }
}

