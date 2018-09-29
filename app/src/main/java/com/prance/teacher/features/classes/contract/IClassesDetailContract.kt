package com.prance.teacher.features.classes.contract

import com.prance.lib.base.mvp.*
import com.prance.lib.common.utils.http.ResponseBody
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.features.students.model.StudentsEntity
import io.reactivex.Flowable

/**
 * Description :
 * @author  rich
 * @date 2018/8/8  上午11:02
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IClassesDetailContract {
    interface View : IView<Presenter> {
        /**
         * 获取到了学生列表的集合
         */
        fun studentList(list: MutableList<StudentsEntity>)
    }
    interface Presenter : IPresenter<View, Model> {
        /**
         * 获取学生列表
         */
        fun getStudentsByClassesId(id: String)
    }
    interface Model : IModel {
        fun getStudentsByClassesId(id: String): Flowable<ResponseBody<StudentsEntity>>
    }
}
