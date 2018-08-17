package com.prance.teacher.features.afterclass.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.prance.lib.sunvote.service.SunARSListenerAdapter
import com.prance.lib.sunvote.service.SunVoteServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.afterclass.AfterClassActivity
import com.prance.teacher.features.afterclass.contract.IAfterClassContract
import com.prance.teacher.features.afterclass.model.FeedBack
import com.prance.teacher.features.afterclass.presenter.AfterClassPresenter
import com.prance.teacher.features.match.view.generateKeyPadId
import kotlinx.android.synthetic.main.fragment_after_class.*

/**
 * Description : 课后答题界面
 * @author  rich
 * @date 2018/7/25  下午5:15
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class AfterClassFragment : BaseFragment(), IAfterClassContract.View {

    lateinit var mTime: TextView
    var mFeedback: FeedBack? = null

    override var mPresenter: IAfterClassContract.Presenter = AfterClassPresenter()

    override fun layoutId(): Int = R.layout.fragment_after_class

    private val mSunVoteServicePresenter: SunVoteServicePresenter by lazy { SunVoteServicePresenter(context!!, object : SunARSListenerAdapter() {

        override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
            timer.post {
                mPresenter.saveChoose(generateKeyPadId(KeyID), sInfo ?: "")
            }
        }

    })}

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mTime = rootView.findViewById(R.id.timer)
        mFeedback = arguments?.getSerializable(AfterClassActivity.feedback) as FeedBack
        mPresenter.startReceive(mFeedback!!)

        mSunVoteServicePresenter.bind()
    }

    override fun onTimeChange(time: String) {
        mTime.text = time
    }

    override fun showLoading() {
        showProgress()
    }

    override fun onNetworkError(throwable: Throwable): Boolean {
        hideProgress()
        activity?.finish()
        return false
    }

    override fun confirmChooseSuccess() {
        activity?.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.stopReceive()

        mSunVoteServicePresenter.unBind()
    }
}

