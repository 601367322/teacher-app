package com.prance.teacher.features.classes.contract

import com.prance.lib.base.mvp.*
import com.prance.lib.common.utils.http.ResponseBody
import com.prance.teacher.features.classes.model.ClassesEntity
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  上午10:05
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IClassesContract {
    interface View : IView<Presenter> {
        fun renderClasses(it: MutableList<ClassesEntity>)
    }
    interface Presenter : IPresenter<View, Model> {
        fun getAllClasses()
    }
    interface Model : IModel {
        fun getAllClasses(): Flowable<ResponseBody<ClassesEntity>>
    }
}
