package com.prance.teacher.features.match.presenter

import com.prance.teacher.features.match.contract.IMatchKeyPadContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.base.mvp.mySubscribe
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.features.main.contract.IMainContract
import com.prance.teacher.features.main.model.MainModel
import com.prance.teacher.features.match.model.MatchKeyPadModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import java.util.*

/**
 * Description :
 * @author  Sen
 * @date 2018/7/16  下午3:32
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class MatchKeyPadPresenter : BasePresenterKt<IMatchKeyPadContract.View>(), IMatchKeyPadContract.Presenter {

    override val mModel: IMatchKeyPadContract.Model = MatchKeyPadModel()

    private val mMainModel: IMainContract.Model = MainModel()

    override fun getMatchedKeyPadByBaseStationId(serialNumber: String) {
        Flowable.create<MutableList<KeyPadEntity>>({
            val list = mMainModel.getAllKeyPadByBaseStationSN(serialNumber)
            if (list?.isNotEmpty()!!) {
                it.onNext(list)
            }
        }, BackpressureStrategy.BUFFER)
                .mySubscribe {
                    mView?.renderKeyPadItemFromDatabase(it)
                }

    }
}

