package com.prance.teacher.features.classes.contract

import com.prance.lib.base.mvp.*
import com.prance.teacher.features.classes.model.ClassResponseBody
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
        fun getAllClasses(userId:String)
    }
    interface Model : IModel {
        fun getAllClasses(userId:String): Flowable<ClassResponseBody>
    }
}
