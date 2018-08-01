package com.prance.teacher.features.afterclass.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import cn.sunars.sdk.SunARS
import cn.sunars.sdk.SunARS.VoteType_Choice
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.afterclass.contract.IAfterClassContract
import com.prance.teacher.features.afterclass.presenter.AfterClassPresenter

/**
 * Description :
 * @author  rich
 * @date 2018/7/25  下午5:15
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class AfterClassFragment : BaseFragment(), IAfterClassContract.View {

    lateinit var mTime: TextView

    override var mPresenter: IAfterClassContract.Presenter = AfterClassPresenter()

    override fun layoutId(): Int = R.layout.fragment_after_class

    override fun needSunVoteService(): Boolean = true

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mPresenter.startReceive()
        mTime = rootView.findViewById(R.id.timer)
    }

    override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
        mPresenter.saveChoose(KeyID,sInfo?:"")
    }

    override fun onTimeChange(time: String) {
       mTime.text = time
    }

    override fun showLoading() {
        showProgress()
    }

    override fun confirmChooseSuccess() {
        activity?.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.stopReceive()
    }
}

