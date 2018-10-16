package com.prance.teacher.features.classes.presenter

import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.features.classes.contract.IClassesDetailContract
import com.prance.teacher.features.classes.contract.IClassesNextStepContract
import com.prance.teacher.features.classes.model.ClassesNextStepModel
import com.prance.teacher.features.match.model.MatchKeyPadModel

class ClassesNextStepPresenter : BasePresenterKt<IClassesNextStepContract.View>(), IClassesNextStepContract.Presenter {

    override val mModel: IClassesNextStepContract.Model = ClassesNextStepModel()

    private val mMatchKeyPadModel = MatchKeyPadModel()

    override fun getKeyPadCount(stationId: String): Int {
        return mMatchKeyPadModel.getAllKeyPadByBaseStationSN(stationId).size
    }
}