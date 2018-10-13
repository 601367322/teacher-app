package com.prance.teacher.features.replacekeypad.presenter

import com.prance.lib.common.utils.http.ResultException
import com.prance.teacher.features.replacekeypad.contract.IReplaceKeyPadContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.features.match.contract.IMatchKeyPadContract
import com.prance.teacher.features.match.model.MatchKeyPadModel
import com.prance.teacher.features.replacekeypad.model.ReplaceKeyPadModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/19  下午12:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class ReplaceKeyPadPresenter : BasePresenterKt<IReplaceKeyPadContract.View>(), IReplaceKeyPadContract.Presenter {

    override val mModel: IReplaceKeyPadContract.Model = ReplaceKeyPadModel()

    private val mMatchKeyPadModel = MatchKeyPadModel()


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


//    override fun saveMatchedKeyPad(keyPadEntity: KeyPadEntity): KeyPadEntity? {
//        return mModel.saveMatchedKeyPad(keyPadEntity)
//    }
//
    override fun deleteKeyPad(keyPadEntity: KeyPadEntity): Boolean {
        return mMatchKeyPadModel.deleteKeyPad(keyPadEntity)
    }
}

