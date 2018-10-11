package com.prance.teacher.features.match.presenter

import com.prance.teacher.features.match.contract.IMatchKeyPadContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.features.match.model.MatchKeyPadModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/16  下午3:32
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class MatchKeyPadPresenter : BasePresenterKt<IMatchKeyPadContract.View>(), IMatchKeyPadContract.Presenter {

    override val mModel: IMatchKeyPadContract.Model = MatchKeyPadModel()

    val mMatchKeyPadModel = MatchKeyPadModel()

    override fun getMatchedKeyPadByBaseStationId(serialNumber: String) {
        Flowable.create<MutableList<KeyPadEntity>>({
            val list = mMatchKeyPadModel.getAllKeyPadByBaseStationSN(serialNumber)
            if (list?.isNotEmpty()!!) {
                it.onNext(list)
                it.onComplete()
            }
        }, BackpressureStrategy.BUFFER)
                .mySubscribe {
                    mView?.renderKeyPadItemFromDatabase(it)
                }

    }


    override fun saveMatchedKeyPad(keyPadEntity: KeyPadEntity): KeyPadEntity? {
        return mModel.saveMatchedKeyPad(keyPadEntity)
    }

    override fun deleteKeyPad(keyPadEntity: KeyPadEntity): Boolean {
        return mModel.deleteKeyPad(keyPadEntity)
    }
}

