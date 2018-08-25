package com.prance.teacher.features.pk.view

import android.os.Bundle
import android.view.View
import cn.sunars.sdk.SunARS
import com.prance.lib.socket.MessageListener
import com.prance.lib.socket.PushServicePresenter
import com.prance.lib.sunvote.service.SunARSListenerAdapter
import com.prance.lib.sunvote.service.SunVoteServicePresenter
import com.prance.teacher.features.pk.contract.IPKContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.match.view.generateKeyPadId
import com.prance.teacher.features.pk.model.PKSetting
import com.prance.teacher.features.pk.presenter.PKPresenter
import com.prance.teacher.features.subject.model.KeyPadResult

/**
 * Description :
 * @author  Sen
 * @date 2018/8/24  下午4:07
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class PKFragment : BaseFragment(), IPKContract.View, MessageListener {

    companion object {
        const val SETTING = "setting"

        fun forSetting(setting: PKSetting): PKFragment {
            val fragment = PKFragment()
            val arguments = Bundle()
            arguments.putSerializable(SETTING, setting)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_pk

    lateinit var mSetting: PKSetting

    private val mSunVoteServicePresenter by lazy {
        SunVoteServicePresenter(context!!, object : SunARSListenerAdapter() {

            override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String) {
                val keyId = generateKeyPadId(KeyID)
                mPresenter.sendAnswer(mPushServicePresenter, KeyPadResult(keyId, sInfo, System.currentTimeMillis()), mSetting)
            }
        })
    }

    private val mPushServicePresenter by lazy {
        PushServicePresenter(context!!, this)
    }

    override var mPresenter: IPKContract.Presenter = PKPresenter()

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mSetting = arguments?.getSerializable(SETTING) as PKSetting

        mSunVoteServicePresenter.bind()
        mPushServicePresenter.bind()

        SunARS.voteStart(mSetting.type, mSetting.param)
    }

    override fun onDestroy() {
        super.onDestroy()
        SunARS.voteStop()

        mSunVoteServicePresenter.unBind()
        mPushServicePresenter.unBind()
    }

}

