package com.prance.teacher.features.subject.presenter

import com.prance.teacher.features.subject.contract.ISubjectContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.teacher.features.subject.model.SubjectModel

/**
 * Description :
 * @author  Sen
 * @date 2018/7/26  上午10:37
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class SubjectPresenter : BasePresenterKt<ISubjectContract.View>(), ISubjectContract.Presenter {

    override val mModel: ISubjectContract.Model = SubjectModel()
}

