package com.prance.teacher.features.pk.contract

import com.prance.lib.base.mvp.*

/**
 * Description :
 * @author  Sen
 * @date 2018/8/24  下午4:07
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IPKContract {
    interface View : IView<Presenter> {}
    interface Presenter : IPresenter<View, Model> {}
    interface Model : IModel {}
}
