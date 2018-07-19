package com.prance.teacher.features.classes.presenter

import com.prance.teacher.features.classes.contract.IClassesContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.base.mvp.mySubscribe
import com.prance.teacher.features.classes.model.ClassesModel

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  上午10:05
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class ClassesPresenter : BasePresenterKt<IClassesContract.View>(), IClassesContract.Presenter {

    override val mModel: IClassesContract.Model = ClassesModel()

    override fun getAllClasses() {
        mModel.getAllClasses()
                .mySubscribe(onSubscribeError, {
                    mView?.renderClasses(it.list)
                })
    }
}

