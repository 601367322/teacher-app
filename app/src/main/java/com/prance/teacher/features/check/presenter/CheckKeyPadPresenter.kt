package com.prance.teacher.features.check.presenter

import cn.sunars.sdk.SunARS
import com.prance.lib.common.utils.http.ResultException
import com.prance.teacher.features.check.contract.ICheckKeyPadContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.features.check.model.CheckKeyPadGroupTitle
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

        Flowable.create<MutableList<Any>>({

            val offlineKeyPads = mutableListOf<KeyPadEntity>()
            val batteryKeyPads = mutableListOf<KeyPadEntity>()

            for (matchedKeyPad in mMatchKeyPadEntities) {
                var exists = false
                for (checkKeyPad in mCheckKeyPadEntities) {
                    if (checkKeyPad.keyId == matchedKeyPad.keyId) {
                        matchedKeyPad.status = KeyPadEntity.BATTERY
                        //低电量
                        if (checkKeyPad.battery / SunARS.MAX_BATTERY < 0.05) {
                            batteryKeyPads.add(matchedKeyPad)
                        }
                        exists = true
                    }
                }
                if (!exists) {
                    //未在线
                    matchedKeyPad.status = KeyPadEntity.OFFLINE
                    offlineKeyPads.add(matchedKeyPad)
                }
            }

            val list = mutableListOf<Any>()
            if (offlineKeyPads.isNotEmpty()) {
                list.add(CheckKeyPadGroupTitle("未在线的答题器：", offlineKeyPads.size))
                list.addAll(offlineKeyPads)
            }
            if (batteryKeyPads.isNotEmpty()) {
                list.add(CheckKeyPadGroupTitle("在线但电量不足的答题器：", batteryKeyPads.size))
                list.addAll(batteryKeyPads)
            }

            it.onNext(list)
            it.onComplete()
        }, BackpressureStrategy.BUFFER)
                .mySubscribe {
                    mView?.renderKeyPads(it)
                }

    }
}

