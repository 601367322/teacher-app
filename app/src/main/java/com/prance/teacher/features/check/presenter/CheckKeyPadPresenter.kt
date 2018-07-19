package com.prance.teacher.features.check.presenter

import com.prance.lib.base.http.ResultException
import com.prance.teacher.features.check.contract.ICheckKeyPadContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.base.mvp.mySubscribe
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.features.check.model.CheckKeyPadModel
import com.prance.teacher.features.match.model.MatchKeyPadModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/19  下午5:36
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class CheckKeyPadPresenter : BasePresenterKt<ICheckKeyPadContract.View>(), ICheckKeyPadContract.Presenter {

    override val mModel: ICheckKeyPadContract.Model = CheckKeyPadModel()

    private val mMatchKeyPadModel = MatchKeyPadModel()

    override fun getMatchedKeyPadByBaseStationId(serialNumber: String) {
        Flowable.create<MutableList<KeyPadEntity>>({
            val list = mMatchKeyPadModel.getAllKeyPadByBaseStationSN(serialNumber)
            if (list != null && list.isNotEmpty()) {
                it.onNext(list)
                it.onComplete()
            } else {
                it.onError(ResultException(88002, "请先配对答题器"))
            }
        }, BackpressureStrategy.BUFFER)
                .mySubscribe(onSubscribeError, {
                    mView?.startCheck(it)
                })
    }

    override fun generateGroup(mMatchKeyPadEntities: MutableList<KeyPadEntity>, mCheckKeyPadEntities: MutableList<KeyPadEntity>) {

        val offlineKeyPads = mutableListOf<KeyPadEntity>()

        for (matchedKeyPad in mMatchKeyPadEntities) {
            var exists = false
            for (checkKeyPad in mCheckKeyPadEntities) {
                if (checkKeyPad.keyId == matchedKeyPad.keyId) exists = true
            }
            if (!exists) {
                offlineKeyPads.add(matchedKeyPad)
            }
        }
    }
}

