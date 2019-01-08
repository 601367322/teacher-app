package com.prance.teacher.features.main.presenter

import com.prance.teacher.features.main.contract.IMainContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.features.main.model.MainModel
import com.prance.teacher.features.match.model.MatchKeyPadModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/16  下午12:00
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class MainActivityPresenter : BasePresenterKt<IMainContract.View>(), IMainContract.MainPresenter {

    override val mModel: IMainContract.Model = MainModel()

    override fun logOut() {
        mModel.logOut().mySubscribe { }
    }
}

