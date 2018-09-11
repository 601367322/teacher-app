package com.prance.teacher.features.redpackage.view

import android.os.*
import android.view.View
import com.prance.lib.sunvote.service.SunARSListenerAdapter
import com.prance.lib.sunvote.service.SunVoteServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.features.redpackage.contract.IRedPackageContract
import com.prance.teacher.R
import com.prance.teacher.features.match.view.generateKeyPadId
import com.prance.teacher.features.redpackage.RedPackageActivity
import com.prance.teacher.features.redpackage.presenter.RedPackagePresenter
import com.prance.teacher.features.redpackage.model.RedPackageSetting
import com.prance.teacher.features.redpackage.model.StudentScore
import com.prance.teacher.features.redpackage.view.red.RedPackage
import kotlinx.android.synthetic.main.fragment_red_package.*
import android.media.MediaPlayer
import com.chillingvan.canvasgl.glview.GLView
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.students.model.StudentsEntity


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

    private val mSunVoteServicePresenter: SunVoteServicePresenter by lazy {

        SunVoteServicePresenter(context!!, object : SunARSListenerAdapter() {
            override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
                animGlView.post {
                    val keyId = generateKeyPadId(KeyID)

                    //签到学员才可以抢红包
                    ClassesDetailFragment.checkIsSignStudent(mQuestion?.signStudents, keyId)
                            ?: return@post

                    mPresenter.grabRedPackage(keyId, sInfo)
                }
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

        mSunVoteServicePresenter.bind()
    }

    override fun onShowPackage(redPackage: RedPackage) {
        animGlView?.addItem(redPackage)
        redPackage.startFall()
    }

    override fun onTimeEnd(scores: MutableList<StudentScore>) {
        (activity as RedPackageActivity).redPackageRank(scores)
    }

    fun redPackageStop() {
        mPresenter.stopRedPackage()
    }

    override fun onDestroy() {
        super.onDestroy()

        mAnimGlView?.destroy()

        mPresenter.detachView()

        mSunVoteServicePresenter.unBind()

        mMediaPlayer?.stop()
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

}

