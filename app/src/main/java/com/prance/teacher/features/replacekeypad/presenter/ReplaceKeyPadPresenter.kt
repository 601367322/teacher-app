package com.prance.teacher.features.replacekeypad.presenter

import com.prance.lib.base.http.ResultException
import com.prance.teacher.features.replacekeypad.contract.IReplaceKeyPadContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.base.mvp.mySubscribe
import com.prance.lib.database.KeyPadEntity
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

    override fun replaceKeyPad(baseStationId: String, classesId: String, oldKeyPadId: String, newKeyPadId: String) {

        Flowable
                .create<MutableList<KeyPadEntity>>({
                    val list = mMatchKeyPadModel.getAllKeyPadByBaseStationSN(baseStationId)
                    if (list != null && list.isNotEmpty()) {
                        var existsKeyPad: KeyPadEntity? = null
                        for (keyPad in list) {
                            if (keyPad.keyId == newKeyPadId) {
                                existsKeyPad = keyPad
                            }
                        }
                        if (existsKeyPad == null) {
                            it.onError(ResultException(88002, "请先进行答题器配对"))
                        } else {
                            it.onNext(list)
                        }
                    } else {
                        it.onError(ResultException(88002, "请先进行答题器配对"))
                    }
                }, BackpressureStrategy.BUFFER)
                .flatMap({
                    mModel.replaceKeyPad(classesId, oldKeyPadId, newKeyPadId)
                })
                .mySubscribe(
                        {
                            if (it is ResultException) {
                                onSubscribeError(it)
                            } else {
                                onSubscribeError(ResultException(88003, "替换失败，请重新替换"))
                            }
                        },
                        { mView?.replaceSuccess() }
                )
    }
}

