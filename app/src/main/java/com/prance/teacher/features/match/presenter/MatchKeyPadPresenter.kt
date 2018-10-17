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

    override fun getMatchedKeyPadByBaseStationId(serialNumber: String) {
        Flowable.create<MutableList<KeyPadEntity>>({
            val list = mModel.getAllKeyPadByBaseStationSN(serialNumber)
            if (list?.isNotEmpty()!!) {
                it.onNext(list)
                it.onComplete()
            }
        }, BackpressureStrategy.BUFFER)
                .mySubscribe {
                    mView?.renderKeyPadItemFromDatabase(it)
                }

    }

    override fun saveAllKeyPad(serialNumber: String, data: List<KeyPadEntity>) {
        //删除所有已配对的答题器
        mModel.deleteKeyPad(serialNumber)
        if (data.isNotEmpty()) {
            //保存所有已配对的答题器
            mModel.saveAllMatchedKeyPad(data)
        }
    }
}

