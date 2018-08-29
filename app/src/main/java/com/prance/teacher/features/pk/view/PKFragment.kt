package com.prance.teacher.features.pk.view

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.text.format.DateUtils
import android.view.View
import cn.sunars.sdk.SunARS
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.autoGenericCode
import com.prance.lib.common.utils.dateFormat_Min_Second
import com.prance.lib.common.utils.format
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.MessageEntity
import com.prance.lib.socket.MessageListener
import com.prance.lib.socket.PushService.Companion.CMD_END_QUESTION
import com.prance.lib.socket.PushService.Companion.PK_RUNTIME_DATA
import com.prance.lib.socket.PushServicePresenter
import com.prance.lib.sunvote.service.SunARSListenerAdapter
import com.prance.lib.sunvote.service.SunVoteServicePresenter
import com.prance.teacher.features.pk.contract.IPKContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.R.id.runtimeData
import com.prance.teacher.features.afterclass.AfterClassActivity.Companion.feedback
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.match.view.generateKeyPadId
import com.prance.teacher.features.pk.model.PKRuntimeData
import com.prance.teacher.features.pk.presenter.PKPresenter
import com.prance.teacher.features.subject.model.KeyPadResult
import com.prance.teacher.features.subject.view.SubjectOnStopFragment
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_pk.*
import java.util.concurrent.TimeUnit

/**
 * Description :
 * @author  Sen
 * @date 2018/8/24  下午4:07
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class PKFragment : BaseFragment(), IPKContract.View, MessageListener, ICountTimeListener {

    companion object {
        const val SETTING = "setting"

        fun forSetting(setting: ClassesDetailFragment.Question): PKFragment {
            val fragment = PKFragment()
            val arguments = Bundle()
            arguments.putSerializable(SETTING, setting)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_pk

    lateinit var mSetting: ClassesDetailFragment.Question

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
        mSetting = arguments?.getSerializable(SETTING) as ClassesDetailFragment.Question

        mSunVoteServicePresenter.bind()
        mPushServicePresenter.bind()

        animGlView.countTimeListener = this

        if (BuildConfig.DEBUG) {

//            Flowable.timer(3000, TimeUnit.MILLISECONDS)
//                    .mySubscribe {
//                        (activity as FragmentActivity).supportFragmentManager.inTransaction {
//                            replace(R.id.fragmentContainer, PKRankFragment())
//                        }
//                    }
        }
    }

    override fun onResume() {
        super.onResume()
        animGlView.onResume()
    }

    override fun onPause() {
        super.onPause()
        animGlView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        SunARS.voteStop()

        mSunVoteServicePresenter.unBind()
        mPushServicePresenter.unBind()
    }

    //倒计时结束
    override fun onTimeCountEnd() {
        mSetting.createTime = System.currentTimeMillis()
        SunARS.voteStart(mSetting.type!!, mSetting.param)
        animGlView?.post {
            runtimeData.visible()
            timer.visible()
            startCountTimer()
        }
    }

    private var mDisposable: Disposable? = null

    private fun startCountTimer() {
        mSetting.duration?.run {
            var time = this
            mDisposable = Flowable.interval(1, TimeUnit.SECONDS)
                    .take(time.toLong())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        time--
                        timer?.text = format(dateFormat_Min_Second, time.toLong() * 1000)
                        if (time < 1) {
                            endPK()
                        }
                    }
        }

    }

    private fun endPK() {
        mDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        (activity as FragmentActivity).supportFragmentManager.inTransaction {
            replace(R.id.fragmentContainer, SubjectOnStopFragment())
        }
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
            }
        }
    }
}

