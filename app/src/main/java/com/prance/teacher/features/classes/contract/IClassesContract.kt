package com.prance.teacher.features.classes.contract

import com.prance.lib.base.mvp.*
import com.prance.lib.common.utils.http.ResponseBody
import com.prance.lib.server.vo.teacher.ClassVo
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  上午10:05
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IClassesContract {
    interface View : IView<Presenter> {
        fun renderClasses(it: MutableList<ClassVo>)
    }

    interface Presenter : IPresenter<View, Model> {
        fun getAllClasses()
        fun getKeyPadCount(stationId: String): Int
    }

    interface Model : IModel {
        fun getAllClasses(): Flowable<ResponseBody<ClassVo>>
    }
}
