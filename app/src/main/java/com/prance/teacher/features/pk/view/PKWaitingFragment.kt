package com.prance.teacher.features.pk.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.database.MessageEntity
import com.prance.lib.socket.MessageListener
import com.prance.lib.socket.PushService
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.pk.contract.IPKResultContract
import com.prance.teacher.features.pk.model.PKResult
import com.prance.teacher.features.pk.presenter.PKResultPresenter

class PKWaitingFragment : BaseFragment(), MessageListener, IPKResultContract.View {

    override fun layoutId(): Int = R.layout.fragment_on_waiting

    override var mPresenter: IPKResultContract.Presenter = PKResultPresenter()

    var questionId: Int? = null

    companion object {

        const val AUTO_LOAD = "AUTO_LOAD"
        const val QUESTION_ID = "QUESTION_ID"

        fun callingIntent(autoLoad: Boolean, questionId: Int): PKWaitingFragment {
            var fragment = PKWaitingFragment()
            val bundle = Bundle()
            bundle.putBoolean(AUTO_LOAD, autoLoad)
            bundle.putInt(QUESTION_ID, questionId)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        questionId = arguments?.getInt(QUESTION_ID)
        arguments?.run {
            if (containsKey(AUTO_LOAD) && getBoolean(AUTO_LOAD)) {
                getResult()
            }
        }
    }

    override fun onMessageResponse(msg: MessageEntity): Boolean {
        when (msg.cmd) {
            PushService.CMD_END_QUESTION -> {
                Handler(Looper.getMainLooper()).post { getResult() }
                return true
            }
        }
        return super.onMessageResponse(msg)
    }

    private fun getResult() {
        showProgress()
        questionId?.run {
            mPresenter.getPKResult(this)
        }

    }

    override fun onNetworkError(throwable: Throwable): Boolean {
        hideProgress()
        return false
    }

    override fun renderRank(it: PKResult) {
        activity?.supportFragmentManager?.inTransaction {
            replace(R.id.fragmentContainer, PKRankFragment.forPKResult(it))
        }
    }
}