package com.prance.teacher.features.students.contract

import com.prance.lib.base.mvp.*

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  下午12:56
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IStudentsContract {
    interface View : IView<Presenter> {}
    interface Presenter : IPresenter<View, Model> {}
    interface Model : IModel {}
}
