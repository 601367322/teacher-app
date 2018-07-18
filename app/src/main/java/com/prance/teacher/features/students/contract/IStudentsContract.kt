package com.prance.teacher.features.students.contract

import com.prance.lib.base.mvp.*
import com.prance.lib.teacher.base.http.ResponseBody
import com.prance.teacher.features.students.model.StudentsEntity
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  下午12:56
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IStudentsContract {
    interface View : IView<Presenter> {
        fun renderStudents(list: MutableList<StudentsEntity>)
    }
    interface Presenter : IPresenter<View, Model> {
        fun getStudentsByClassesId(id: String)
    }
    interface Model : IModel {
        fun getStudentsByClassesId(id: String):Flowable<ResponseBody<StudentsEntity>>
    }
}
