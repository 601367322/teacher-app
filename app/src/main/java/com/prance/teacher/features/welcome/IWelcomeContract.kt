package com.prance.teacher.features.welcome

import com.prance.lib.base.mvp.IModel
import com.prance.lib.base.mvp.IPresenter
import com.prance.lib.base.mvp.IView
import com.prance.teacher.features.login.model.VersionEntity
import com.prance.teacher.features.subject.model.KeyPadResult
import io.reactivex.Flowable

interface IWelcomeContract {

    interface View : IView<Presenter> {
        fun checkVersionCallBack(it: VersionEntity)
    }

    interface Presenter : IPresenter<View, Model> {
        fun checkVersion()
    }

    interface Model : IModel {
        fun checkVersion(): Flowable<VersionEntity>
    }
}