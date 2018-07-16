package com.prance.teacher.features.main.presenter

import com.prance.teacher.features.main.contract.IMainContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.base.mvp.mySubscribe
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.features.main.model.MainModel
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

    override fun checkIfKeyPadAlreadyMatched(serialNumber: String, matched: () -> Unit, unMatch: () -> Unit) {
        Flowable
                .create<MutableList<KeyPadEntity>>({ subscriber ->
                    var list = mModel.getAllKeyPadByBaseStationSN(serialNumber)
                    if (list != null && list.size > 0) {
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

