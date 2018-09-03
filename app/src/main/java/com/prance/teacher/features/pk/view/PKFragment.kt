package com.prance.teacher.features.pk.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import cn.sunars.sdk.SunARS
import com.chillingvan.canvasgl.glview.GLContinuousView
import com.chillingvan.canvasgl.glview.GLView
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.AnimUtil
import com.prance.lib.common.utils.dateFormat_Min_Second
import com.prance.lib.common.utils.format
import com.prance.lib.database.MessageEntity
import com.prance.lib.socket.MessageListener
import com.prance.lib.socket.PushService.Companion.PK_RUNTIME_DATA
import com.prance.lib.sunvote.service.SunARSListenerAdapter
import com.prance.lib.sunvote.service.SunVoteServicePresenter
import com.prance.teacher.features.pk.contract.IPKContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.match.view.generateKeyPadId
import com.prance.teacher.features.pk.PKActivity
import com.prance.teacher.features.pk.model.PKRuntimeData
import com.prance.teacher.features.pk.presenter.PKPresenter
import com.prance.teacher.features.subject.model.KeyPadResult
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

    var mAnimGlView: GLView? = null

    var boxLightAnim: ValueAnimator? = null
    var doubleScore = false

    var mActivity: PKActivity? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mActivity = context as PKActivity?
    }

    private val mSunVoteServicePresenter by lazy {
        SunVoteServicePresenter(context!!, object : SunARSListenerAdapter() {

            var answerList = mutableListOf<String>()

            override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String) {
                animGlView?.post {
                    val keyId = generateKeyPadId(KeyID)

                    //防止重复提交
                    if(answerList.contains(keyId)){
                        return@post
                    }
                    answerList.add(keyId)

                    //进度条宝箱
                    if (sInfo == mSetting.result) {
                        powerProgressbar.progress += 1
                    }
                    if ((powerProgressbar.progress.toFloat() / powerProgressbar.max.toFloat()) * 100 > 70) {
                        doubleScore = true

                        //宝箱打开动画
                        val animationDrawable = box.drawable as AnimationDrawable
                        animationDrawable.start()
                        //1秒后打开宝箱灯光
                        animGlView?.postDelayed({
                            boxLight.visible()
                            boxLightAnim = ObjectAnimator.ofFloat(boxLight, AnimUtil.ROTATION, 0F, 360F).setDuration(1000)
                            boxLightAnim!!.interpolator = LinearInterpolator()
                            boxLightAnim!!.repeatCount = Animation.INFINITE
                            boxLightAnim!!.repeatMode = ValueAnimator.RESTART
                            boxLightAnim!!.start()
                        }, 1000)
                    }
                    mActivity?.let {
                        //发送答题器结果
                        mPresenter.sendAnswer(it.mPushServicePresenter, KeyPadResult(keyId, sInfo, System.currentTimeMillis()), mSetting)
                    }
                }
            }
        })
    }

    override var mPresenter: IPKContract.Presenter = PKPresenter()

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mSetting = arguments?.getSerializable(SETTING) as ClassesDetailFragment.Question

        mAnimGlView = rootView.findViewById(R.id.animGlView)

        mSunVoteServicePresenter.bind()

        animGlView.countTimeListener = this

        powerProgressbar.max = mSetting.signCount

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

        mAnimGlView?.destroy()

        mSunVoteServicePresenter.unBind()
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
        activity?.run {
            this.supportFragmentManager.inTransaction {
                replace(R.id.fragmentContainer, PKWaitingFragment.callingIntent(false, mSetting.questionId!!))
            }
        }
    }

    override fun onMessageResponse(msg: MessageEntity): Boolean {
        runtimeData.post {
            when (msg.cmd) {
                PK_RUNTIME_DATA -> {
                    //所有班级的排名
                    val data = msg.getArrayData(PKRuntimeData::class.java)

                    //我所在班级的排名
                    var mRank: PKRuntimeData? = null
                    for (i in data) {
                        if (i.klass?.id == mSetting.classId) {
                            mRank = i
                        }
                    }

                    if (mRank == null) {
                        //没有学生作答
                        runtimeData?.text =
                                """平均正确率0%
                                |平均作答时间0秒
                                |第- -名""".trimMargin()
                    } else {
                        //正常情况
                        runtimeData?.text =
                                """平均正确率${mRank.correctRate}%
                                |平均作答时间${mRank.averageTime}秒
                                |第${data.indexOf(mRank) + 1}名""".trimMargin()
                    }
                }
            }
        }
        return super.onMessageResponse(msg)
    }
}

