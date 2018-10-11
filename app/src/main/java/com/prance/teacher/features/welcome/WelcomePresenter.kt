package com.prance.teacher.features.welcome

import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.common.utils.http.mySubscribe

class WelcomePresenter : BasePresenterKt<IWelcomeContract.View>(), IWelcomeContract.Presenter {

    override val mModel: IWelcomeContract.Model = WelcomeModel()

    override fun checkVersion() {
        mModel.checkVersion()
                .mySubscribe(onSubscribeError) { mView?.checkVersionCallBack(it) }
    }
}