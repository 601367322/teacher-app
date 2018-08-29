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

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
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
        mPresenter.getPKResult()
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