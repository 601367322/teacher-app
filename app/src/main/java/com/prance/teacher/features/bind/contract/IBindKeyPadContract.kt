package com.prance.teacher.features.bind.contract

import com.prance.lib.base.mvp.*

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  上午9:58
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IBindKeyPadContract {
    interface View : IView<Presenter> {}
    interface Presenter : IPresenter<View, Model> {}
    interface Model : IModel {}
}
