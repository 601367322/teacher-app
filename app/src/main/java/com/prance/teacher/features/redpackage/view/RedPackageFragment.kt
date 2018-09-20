package com.prance.teacher.features.redpackage.view

import android.os.*
import android.view.View
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.features.redpackage.contract.IRedPackageContract
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.RedPackageActivity
import com.prance.teacher.features.redpackage.presenter.RedPackagePresenter
import com.prance.teacher.features.redpackage.model.RedPackageSetting
import com.prance.teacher.features.redpackage.model.StudentScore
import com.prance.teacher.features.redpackage.view.red.RedPackage
import kotlinx.android.synthetic.main.fragment_red_package.*
import android.media.MediaPlayer
import com.chillingvan.canvasgl.glview.GLView
import com.prance.lib.spark.SparkListenerAdapter
import com.prance.lib.spark.SparkService
import com.prance.lib.spark.SparkServicePresenter
import com.prance.teacher.BuildConfig
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.spark.teaching.answertool.usb.model.ReceiveAnswer


/**
 * Description : 抢红包
 * @author  rich
 * @date 2018/7/26  下午2:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class RedPackageFragment : BaseFragment(), IRedPackageContract.View {


    companion object {
        const val mSetTing: String = "settting"
    }

    /**
     * 抢红包的设置参数
     */
    var mQuestion: RedPackageSetting? = null

    var mAnimGlView: GLView? = null

    var mMediaPlayer: MediaPlayer? = null

    override fun layoutId(): Int = R.layout.fragment_red_package

    private val mSparkServicePresenter by lazy {

        SparkServicePresenter(context!!, object : SparkListenerAdapter() {

            override fun onAnswerReceived(answer: ReceiveAnswer) {
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
        mQuestion = arguments?.getSerializable(mSetTing) as RedPackageSetting

        mAnimGlView = rootView.findViewById(R.id.animGlView)

        try {
            mMediaPlayer = MediaPlayer.create(context, R.raw.red_package_background)
            mMediaPlayer!!.setOnPreparedListener { mMediaPlayer -> mMediaPlayer.start() }
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.prepare()
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

    override fun onTimeEnd(scores: MutableList<StudentScore>) {
        (activity as RedPackageActivity).redPackageRank(scores)
    }

    override fun startSendRedPackage() {
        mSparkServicePresenter.sendQuestion(SparkService.QuestionType.COMMON)
    }

    override fun stopSendRedPackage() {
        mSparkServicePresenter.stopAnswer()
    }

    fun redPackageStop() {
        mPresenter.stopRedPackage()
    }

    override fun onDestroy() {
        super.onDestroy()

        mAnimGlView?.destroy()

        mPresenter.detachView()

        mSparkServicePresenter.unBind()

        mMediaPlayer?.stop()
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

}

