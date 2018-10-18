package com.prance.teacher.features.classes.presenter

import com.prance.teacher.features.classes.contract.IClassesContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.teacher.features.classes.model.ClassesModel
import com.prance.teacher.features.match.model.MatchKeyPadModel

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  上午10:05
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class ClassesPresenter : BasePresenterKt<IClassesContract.View>(), IClassesContract.Presenter {

    override val mModel: IClassesContract.Model = ClassesModel()

    private val keyPadModel: MatchKeyPadModel = MatchKeyPadModel()

    override fun getAllClasses(isRender: Boolean) {
        mModel.getAllClasses()
                .mySubscribe(onSubscribeError) {
                    if (isRender)
                        mView?.renderClasses(it.list)
                    else
                        mView?.refreshClasses(it.list)
                }
    }

    override fun getKeyPadCount(stationId: String): Int {
        return keyPadModel.getAllKeyPadByBaseStationSN(stationId).size
    }
}

