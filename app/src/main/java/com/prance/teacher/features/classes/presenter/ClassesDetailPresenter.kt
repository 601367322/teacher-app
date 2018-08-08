package com.prance.teacher.features.classes.presenter

import com.prance.teacher.features.classes.contract.IClassesDetailContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.base.mvp.mySubscribe
import com.prance.teacher.features.classes.contract.IClassesContract
import com.prance.teacher.features.classes.model.ClassesDetailModel
import com.prance.teacher.features.classes.model.ClassesModel

/**
 * Description :
 * @author  rich
 * @date 2018/8/8  上午11:02
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class ClassesDetailPresenter : BasePresenterKt<IClassesDetailContract.View>(), IClassesDetailContract.Presenter {
    override val mModel: IClassesDetailContract.Model = ClassesDetailModel()
    override fun getStudentsByClassesId(id: String) {
        mModel.getStudentsByClassesId(id)
                .mySubscribe(onSubscribeError, {
                    mView?.studentList(it.list)
                })
    }
}

