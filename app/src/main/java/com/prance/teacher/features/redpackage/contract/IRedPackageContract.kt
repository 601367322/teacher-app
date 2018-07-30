package com.prance.teacher.features.redpackage.contract

import com.prance.lib.base.mvp.*
import com.prance.teacher.features.redpackage.model.RedPackageWrapper
import com.prance.teacher.features.redpackage.view.RedPackageView

/**
 * Description :
 * @author  rich
 * @date 2018/7/26  下午2:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

interface IRedPackageContract {
    interface View : IView<Presenter> {
        /**
         * 显示一个随机生成的红包
         */
        fun onShowPackage(redPackageWrapper: RedPackageWrapper)
    }
    interface Presenter : IPresenter<View, Model> {
        /**
         * 抢红包开始
         */
        fun startRedPackage()

        /**
         * 抢红包结束
         */
        fun stopRedPackage()

        /**
         * 收到答题器的抢红包指令
         */
        fun grabRedPackage(KeyID: String, sInfo: String?)
    }
    interface Model : IModel {
    }
}
