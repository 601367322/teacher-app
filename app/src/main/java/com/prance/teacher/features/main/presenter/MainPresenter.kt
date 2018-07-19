package com.prance.teacher.features.main.presenter

import com.prance.teacher.features.main.contract.IMainContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.base.mvp.mySubscribe
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

class MainPresenter : BasePresenterKt<IMainContract.View>(), IMainContract.Presenter {

    override val mModel: IMainContract.Model = MainModel()

    val mMatchKeyPadModel = MatchKeyPadModel()

    override fun checkIfKeyPadAlreadyMatched(serialNumber: String, matched: () -> Unit, unMatch: () -> Unit) {
        Flowable
                .create<MutableList<KeyPadEntity>>({ subscriber ->
                    var list = mMatchKeyPadModel.getAllKeyPadByBaseStationSN(serialNumber)
                    if (list?.isNotEmpty()!!) {
                        subscriber.onNext(list)
                    } else {
                        subscriber.onError(Throwable(""))
                    }
                }, BackpressureStrategy.BUFFER)
                .mySubscribe({
                    unMatch()
                }, {
                    matched()
                })
    }
}

