package com.prance.teacher.features.pk.contract

import com.prance.lib.base.mvp.*
import com.prance.teacher.features.pk.model.PKResult
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/8/29  下午5:49
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IPKResultContract {
    interface View : IView<Presenter> {
        fun renderRank(it: PKResult)
    }
    interface Presenter : IPresenter<View, Model> {
        fun getPKResult()
    }
    interface Model : IModel {
        fun getPKResult(): Flowable<PKResult>
    }
}
