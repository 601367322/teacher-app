package com.prance.teacher.features.pk.view

import android.animation.*
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import cn.sunars.sdk.SunARS
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.extension.invisible
import com.prance.lib.base.extension.isVisible
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
import com.prance.teacher.features.pk.rocket.BigRocket
import com.prance.teacher.features.students.model.StudentsEntity
import com.prance.teacher.features.subject.model.KeyPadResult
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_pk.*
import org.greenrobot.eventbus.Subscribe
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

    lateinit var mQuestion: ClassesDetailFragment.Question

    var mAnimGlView: PKAnimView? = null

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
                //防止重复提交
                if (answerList.contains(KeyID)) {
                    return
                }
                answerList.add(KeyID)

                animGlView?.post {
                    val keyId = generateKeyPadId(KeyID)

                    //签到学员才可以答题
                    ClassesDetailFragment.checkIsSignStudent(mQuestion.signStudents, keyId)
                            ?: return@post

                    //进度条宝箱
                    if (sInfo == mQuestion.result) {
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
                        mPresenter.sendAnswer(it.mPushServicePresenter, KeyPadResult(keyId, sInfo, System.currentTimeMillis()), mQuestion)
                    }
                }
            }
        })
    }

    override var mPresenter: IPKContract.Presenter = PKPresenter()

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mQuestion = arguments?.getSerializable(SETTING) as ClassesDetailFragment.Question

        mAnimGlView = rootView.findViewById(R.id.animGlView)

        mSunVoteServicePresenter.bind()

        animGlView.countTimeListener = this

        powerProgressbar.max = mQuestion.signStudents?.size ?: 0

        if (BuildConfig.DEBUG) {

//            Flowable.timer(3000, TimeUnit.MILLISECONDS)
//                    .mySubscribe {
//                        (activity as FragmentActivity).supportFragmentManager.inTransaction {
//                            replace(R.id.fragmentContainer, PKRankFragment.forPKResult(
//                                    PKResult(
//                                            mutableListOf(
//                                                    PKPresenter.PKResultMessage.ClassVO(PKPresenter.PKResultMessage.IDEntity(1, "第一名"), 2.1F, 90.0F),
//                                                    PKPresenter.PKResultMessage.ClassVO(PKPresenter.PKResultMessage.IDEntity(2, "第二名"), 12.1F, 60.0F),
//                                                    PKPresenter.PKResultMessage.ClassVO(PKPresenter.PKResultMessage.IDEntity(31, "第三名"), 3.1F, 70.0F),
//                                                    PKPresenter.PKResultMessage.ClassVO(PKPresenter.PKResultMessage.IDEntity(4, "第四名"), 4.1F, 10.0F),
//                                                    PKPresenter.PKResultMessage.ClassVO(PKPresenter.PKResultMessage.IDEntity(5, "第无名"), 25.1F, 920.0F)
//                                            ),
//                                            mutableListOf(
//                                                    PKResult.Question(3, 100),
//                                                    PKResult.Question(4, 200),
//                                                    PKResult.Question(5, 300)
//                                            )
//                                    )
//                            ))
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
        mQuestion.createTime = System.currentTimeMillis()
        SunARS.voteStart(mQuestion.type!!, mQuestion.param)
        animGlView?.post {
            tip.invisible()
            timer.visible()
            startCountTimer()
        }
    }

    private var mDisposable: Disposable? = null

    private fun startCountTimer() {
        mQuestion.duration?.run {
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
                replace(R.id.fragmentContainer, PKWaitingFragment.callingIntent(false, mQuestion.questionId!!))
            }
        }
    }

    override fun needEventBus(): Boolean = true

    @Subscribe
    fun onEvent(rocket: BigRocket) {
        timer.post {
            if (!scoreLayout.isVisible())
                scoreLayout.visible()
            val layoutParams = scoreLayout.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.marginStart = rocket.startPoint.x.toInt() + rocket.bitmap?.width - resources.getDimensionPixelOffset(R.dimen.m76_0)
            layoutParams.topMargin = rocket.startPoint.y.toInt() - resources.getDimensionPixelOffset(R.dimen.m52_0)
            scoreLayout.layoutParams = layoutParams
        }
    }

    override fun onMessageResponse(msg: MessageEntity): Boolean {
        timer.post {
            when (msg.cmd) {
                PK_RUNTIME_DATA -> {
                    //所有班级的排名
                    val data = msg.getArrayData(PKRuntimeData::class.java)

                    //我所在班级的排名
                    var mRank: PKRuntimeData? = null
                    for (i in data) {
                        if (i.klass?.id == mQuestion.classId) {
                            mRank = i
                        }
                    }

                    className.text = ClassesDetailFragment.mClassesEntity?.klass?.name

                    if (mRank == null) {
                        //没有学生作答
                        correctRate.text = "0%"
                        avgTime.text = "0.00秒"
                        rank.text = "第 x 名"
                    } else {
                        //正常情况
                        correctRate.text = "${mRank.correctRate}%"
                        avgTime.text = "${mRank.averageTime}秒"
                        rank.text = "第 ${data.indexOf(mRank) + 1} 名"
                    }
                }
            }
        }
        return super.onMessageResponse(msg)
    }
}

