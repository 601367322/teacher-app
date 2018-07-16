package com.prance.teacher.features.match.presenter

import com.prance.teacher.features.match.contract.IMatchKeyPadContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.teacher.features.match.model.MatchKeyPadModel

/**
 * Description :
 * @author  Sen
 * @date 2018/7/16  下午3:32
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class MatchKeyPadPresenter : BasePresenterKt<IMatchKeyPadContract.View>(), IMatchKeyPadContract.Presenter {

    override val mModel: IMatchKeyPadContract.Model = MatchKeyPadModel()

}

