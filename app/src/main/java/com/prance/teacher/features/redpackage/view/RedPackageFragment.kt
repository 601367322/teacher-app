package com.prance.teacher.features.redpackage.view

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import com.chillingvan.canvasgl.glview.GLView
import com.prance.lib.base.extension.invisible
import com.prance.lib.common.utils.Constants.SETTING
import com.prance.lib.spark.SparkListenerAdapter
import com.prance.lib.spark.SparkService
import com.prance.lib.spark.SparkServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.redpackage.RedPackageActivity
import com.prance.teacher.features.redpackage.contract.IRedPackageContract
import com.prance.teacher.features.redpackage.model.RedPackageSetting
import com.prance.teacher.features.redpackage.model.StudentScore
import com.prance.teacher.features.redpackage.presenter.RedPackagePresenter
import com.prance.teacher.features.redpackage.view.red.RedPackage
import com.spark.teaching.answertool.usb.model.ReceiveAnswer
import kotlinx.android.synthetic.main.fragment_red_package.*
import org.greenrobot.eventbus.EventBus

class RedPackageFragment : BaseFragment(), IRedPackageContract.View {

    companion object {

        fun forSetting(setting: RedPackageSetting): RedPackageFragment {
            val fragment = RedPackageFragment()
            val bundle = Bundle()
            bundle.putSerializable(SETTING, setting)
            fragment.arguments = bundle
            return fragment
        }
    }

    /**
     * 抢红包的设置参数
     */
    var mQuestion: RedPackageSetting? = null

    var mAnimGlView: GLView? = null

    var mMediaPlayer: MediaPlayer? = null

    override fun layoutId(): Int = R.layout.fragment_red_package

    private val mSparkServicePresenter by lazy {

        SparkServicePresenter(context!!, object : SparkListenerAdapter(true) {

            override fun onAnswer(answer: ReceiveAnswer) {
                val keyId = answer.uid.toString()

                //签到学员才可以抢红包
                ClassesDetailFragment.checkIsSignStudent(mQuestion?.signStudents, keyId)
                        ?: return

                mPresenter.grabRedPackage(keyId, answer.answer)
            }

        })
    }

    override var mPresenter: IRedPackageContract.Presenter = RedPackagePresenter()

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mQuestion = arguments?.getSerializable(SETTING) as RedPackageSetting

        mAnimGlView = rootView.findViewById(R.id.animGlView)

        try {
            mMediaPlayer = MediaPlayer.create(context, R.raw.red_package_background)
            mMediaPlayer!!.setOnPreparedListener { mMediaPlayer ->
                mMediaPlayer.start()
                mMediaPlayer.setVolume(0.3f, 0.3f)
            }
            mMediaPlayer!!.isLooping = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mPresenter.startRedPackage(mQuestion)

        mSparkServicePresenter.bind()
    }

    override fun onShowPackage(redPackage: RedPackage) {
        animGlView?.addItem(redPackage)
        redPackage.startFall()
    }

    /**
     * 结束
     */
    override fun onTimeEnd(scores: MutableList<StudentScore>) {
        animGlView?.clear()
        timer.invisible()
        gameOver.start {
            try {

                mMediaPlayer?.stop()
                mMediaPlayer?.release()
                mMediaPlayer = null

                (activity as RedPackageActivity).redPackageRank(scores)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun startSendRedPackage() {
        mSparkServicePresenter.sendQuestion(SparkService.QuestionType.COMMON)
    }

    override fun stopSendRedPackage() {
        mSparkServicePresenter.stopAnswer()

        //重新发送姓名
        EventBus.getDefault().post(ClassesDetailFragment.SendNameToKeyPad())
    }

    override fun startTimer(totalTime: Long) {
        timer.start(totalTime.toInt())
    }

    fun redPackageStop() {
        mPresenter.stopRedPackage()
    }

    override fun onDestroy() {

        //重新发送姓名
        EventBus.getDefault().post(ClassesDetailFragment.SendNameToKeyPad())

        super.onDestroy()

        mAnimGlView?.destroy()

        mPresenter.detachView()

        mSparkServicePresenter.stopAnswer()

        mSparkServicePresenter.unBind()

        mMediaPlayer?.stop()
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

}

