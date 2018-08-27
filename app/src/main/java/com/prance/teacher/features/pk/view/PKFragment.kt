package com.prance.teacher.features.pk.view

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import cn.sunars.sdk.SunARS
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.MessageEntity
import com.prance.lib.socket.MessageListener
import com.prance.lib.socket.PushService.Companion.PK_END
import com.prance.lib.socket.PushService.Companion.PK_RUNTIME_DATA
import com.prance.lib.socket.PushServicePresenter
import com.prance.lib.sunvote.service.SunARSListenerAdapter
import com.prance.lib.sunvote.service.SunVoteServicePresenter
import com.prance.teacher.features.pk.contract.IPKContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.match.view.generateKeyPadId
import com.prance.teacher.features.pk.model.PKRuntimeData
import com.prance.teacher.features.pk.model.PKSetting
import com.prance.teacher.features.pk.presenter.PKPresenter
import com.prance.teacher.features.subject.model.KeyPadResult
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.fragment_pk.*
import java.util.concurrent.TimeUnit

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

        Flowable.timer(3000,TimeUnit.MILLISECONDS)
                .mySubscribe {
                    (activity as FragmentActivity).supportFragmentManager.inTransaction {
                        replace(R.id.fragmentContainer, PKRankFragment())
                    }
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        SunARS.voteStop()

        mSunVoteServicePresenter.unBind()
        mPushServicePresenter.unBind()
    }

    override fun onMessageResponse(msg: MessageEntity) {
        runtimeData.post {
            when (msg.cmd) {
                PK_RUNTIME_DATA -> {
                    val data = msg.getData(PKRuntimeData::class.java)
                    runtimeData.text =
                            """平均正确率${data.correctRate}%
                                |平均作答时间${data.averageTime}秒
                                |第${data.order}名""".trimMargin()
                }
                PK_END -> {
                    (activity as FragmentActivity).supportFragmentManager.inTransaction {
                        replace(R.id.fragmentContainer, PKRankFragment())
                    }
                }
            }
        }
    }
}

