package com.prance.teacher.features.pk.presenter

import com.prance.teacher.features.pk.contract.IPKResultContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.teacher.features.pk.model.PKResultModel

/**
 * Description :
 * @author  Sen
 * @date 2018/8/29  下午5:49
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class PKResultPresenter : BasePresenterKt<IPKResultContract.View>(), IPKResultContract.Presenter {

    override val mModel: IPKResultContract.Model = PKResultModel()

    override fun getPKResult() {
        mModel
                .getPKResult()
                .mySubscribe(onSubscribeError, {
                    mView?.renderRank(it)
                })
    }
}

