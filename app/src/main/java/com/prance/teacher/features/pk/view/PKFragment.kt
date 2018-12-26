package com.prance.teacher.features.pk.view

import android.animation.*
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import com.prance.lib.base.extension.invisible
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.AnimUtil
import com.prance.lib.common.utils.Constants.SETTING
import com.prance.lib.database.MessageEntity
import com.prance.lib.socket.MessageListener
import com.prance.lib.socket.PushService
import com.prance.lib.socket.PushService.Companion.PK_RUNTIME_DATA
import com.prance.lib.spark.SparkListenerAdapter
import com.prance.lib.spark.SparkServicePresenter
import com.prance.teacher.features.pk.contract.IPKContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.pk.PKActivity
import com.prance.teacher.features.pk.model.PKRuntimeData
import com.prance.teacher.features.pk.presenter.PKPresenter
import com.prance.teacher.features.pk.rocket.BigRocket
import com.prance.teacher.features.subject.model.KeyPadResult
import com.prance.teacher.utils.SoundUtils
import com.spark.teaching.answertool.usb.model.ReceiveAnswer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_pk.*
import org.greenrobot.eventbus.Subscribe

/**
 * Description :
 * @author  Sen
 * @date 2018/8/24  下午4:07
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class PKFragment : BaseFragment(), IPKContract.View, MessageListener, ICountTimeListener {

    companion object {

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

    var mMediaPlayer: MediaPlayer? = null

    var mActivity: PKActivity? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mActivity = context as PKActivity?
    }

    private val mSparkServicePresenter by lazy {
        SparkServicePresenter(context!!, object : SparkListenerAdapter() {

            override fun onAnswer(answer: ReceiveAnswer) {

                animGlView?.post {
                    try {
                        val keyId = answer.uid.toString()

                        val keyPadResult = KeyPadResult(keyId, answer.answer, System.currentTimeMillis())

                        //签到学员才可以答题
                        ClassesDetailFragment.checkIsSignStudent(mQuestion.signStudents, keyId)
                                ?: return@post

                        //进度条宝箱
                        if (keyPadResult.answer == mQuestion.result) {
                            powerProgressbar.progress += 1
                        }
                        if ((powerProgressbar.progress.toFloat() / powerProgressbar.max.toFloat()) * 100 > 70) {
                            doubleScore = true

                            //宝箱打开动画
                            val animationDrawable = box.drawable as AnimationDrawable
                            animationDrawable.start()
                            //1秒后打开宝箱灯光
                            animGlView?.postDelayed({
                                try {
                                    boxLight.visible()

                                    SoundUtils.play("open_box")

                                    boxLightAnim = ObjectAnimator.ofFloat(boxLight, AnimUtil.ROTATION, 0F, 360F).setDuration(1000)
                                    boxLightAnim!!.interpolator = LinearInterpolator()
                                    boxLightAnim!!.repeatCount = Animation.INFINITE
                                    boxLightAnim!!.repeatMode = ValueAnimator.RESTART
                                    boxLightAnim!!.start()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }, 1000)
                        }
                        mActivity?.let {
                            //发送答题器结果
                            mPresenter.sendAnswer(it.mPushServicePresenter, KeyPadResult(keyId, answer.answer, System.currentTimeMillis()), mQuestion)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }

    override var mPresenter: IPKContract.Presenter = PKPresenter()

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mQuestion = arguments?.getSerializable(SETTING) as ClassesDetailFragment.Question

        mAnimGlView = rootView.findViewById(R.id.animGlView)

        mSparkServicePresenter.bind()

        animGlView.countTimeListener = this

        powerProgressbar.max = mQuestion.signStudents?.size ?: 0

        className.text = ClassesDetailFragment.mClassesEntity?.klass?.name

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
        mSparkServicePresenter.stopAnswer()

        mDisposable?.dispose()
        mDisposable = null

        mMediaPlayer?.stop()
        mMediaPlayer?.release()
        mMediaPlayer = null

        mAnimGlView?.destroy()

        mSparkServicePresenter.unBind()
    }

    //倒计时结束
    override fun onTimeCountEnd() {
        mQuestion.createTime = System.currentTimeMillis()
        //发送题目
        mSparkServicePresenter.sendQuestion(mQuestion.getQuestionType())
        animGlView?.post {

            try {
                mMediaPlayer = MediaPlayer.create(context, R.raw.pk_background)
                mMediaPlayer!!.setOnPreparedListener { mMediaPlayer ->
                    mMediaPlayer.start()
                    mMediaPlayer.setVolume(0.3f, 0.3f)
                }
                mMediaPlayer!!.isLooping = true
            } catch (e: Exception) {
                e.printStackTrace()
            }

            startCountTimer()
        }
    }

    private var mDisposable: Disposable? = null

    private fun startCountTimer() {
        mQuestion.duration?.run {
            timer.start(this) {
                endPK()
            }
        }
    }

    private fun endPK(showGameOver: Boolean = true) {
        activity?.run {
            try {
                isEnd = true

                //隐藏倒计时
                timer.end()
                timer.invisible()

                //清理火箭
                animGlView.clear()

                //隐藏计分板
                val layoutParams = scoreLayout.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.topMargin = resources.getDimensionPixelOffset(R.dimen.m2000_0)
                scoreLayout.layoutParams = layoutParams

                if (showGameOver) {
                    //开始游戏结束动画
                    gameOver.start {
                        try {
                            mMediaPlayer?.stop()
                            mMediaPlayer?.release()
                            mMediaPlayer = null

                            activity?.run {
                                (this as PKActivity).endPk()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun needEventBus(): Boolean = true

    var isEnd = false

    @Subscribe
    fun onEvent(rocket: BigRocket) {
        timer.post {
            scoreLayout?.run {
                if (!isEnd) {
                    val layoutParams = scoreLayout.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.marginStart = rocket.startPoint.x.toInt() + rocket.bitmap?.width - resources.getDimensionPixelOffset(R.dimen.m76_0)
                    layoutParams.topMargin = rocket.startPoint.y.toInt() - resources.getDimensionPixelOffset(R.dimen.m52_0)
                    scoreLayout.layoutParams = layoutParams
                }
            }
        }
    }

    override fun onMessageResponse(msg: MessageEntity): Boolean {
        timer.post {
            when (msg.cmd) {
                PK_RUNTIME_DATA -> {
                    //所有班级的排名
                    try {
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
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                PushService.CMD_END_QUESTION -> {
                    try {
                        endPK(false)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return super.onMessageResponse(msg)
    }
}

