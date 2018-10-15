package com.prance.teacher.features.classes.presenter

import com.prance.teacher.features.classes.contract.IClassesDetailContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.KeyPadEntity
import com.prance.teacher.features.classes.model.ClassesDetailModel
import com.prance.teacher.features.match.model.MatchKeyPadModel

/**
 * Description :
 * @author  rich
 * @date 2018/8/8  上午11:02
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class ClassesDetailPresenter : BasePresenterKt<IClassesDetailContract.View>(), IClassesDetailContract.Presenter {

    override val mModel: IClassesDetailContract.Model = ClassesDetailModel()

    val mMatchKeyPadModel = MatchKeyPadModel()

    override fun getStudentsByClassesId(id: String) {
        mModel.getStudentsByClassesId(id)
                .mySubscribe(onSubscribeError) {
                    mView?.studentList(it.list)
                }
    }

    override fun getKeyPadList(s: String): MutableList<KeyPadEntity> {
        return mMatchKeyPadModel.getAllKeyPadByBaseStationSN(s)
    }
}

