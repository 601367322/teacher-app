package com.prance.teacher.features.classes.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.MessageEntity
import com.prance.lib.socket.MessageListener
import com.prance.lib.socket.PushService
import com.prance.lib.socket.PushService.Companion.ATTEND_CLASS
import com.prance.lib.socket.PushService.Companion.CMD_SEND_QUESTION
import com.prance.lib.socket.PushService.Companion.INTERACT_START
import com.prance.lib.socket.PushService.Companion.PK_START
import com.prance.lib.socket.PushService.Companion.QUIZ
import com.prance.lib.socket.PushServicePresenter
import com.prance.lib.sunvote.service.SunARSListenerAdapter
import com.prance.lib.sunvote.service.SunVoteServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.afterclass.AfterClassActivity
import com.prance.teacher.features.check.CheckKeyPadActivity
import com.prance.teacher.features.classes.contract.IClassesDetailContract
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.classes.presenter.ClassesDetailPresenter
import com.prance.teacher.features.pk.PKActivity
import com.prance.teacher.features.redpackage.RedPackageActivity
import com.prance.teacher.features.redpackage.model.RedPackageSetting
import com.prance.teacher.features.students.model.StudentsEntity
import com.prance.teacher.features.students.view.StudentsFragment.Companion.CLASSES
import com.prance.teacher.features.subject.SubjectActivity
import com.prance.teacher.features.subject.SubjectRankActivity
import com.prance.teacher.features.subject.view.SubjectRankFragment
import com.prance.teacher.utils.IntentUtils
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.fragment_classes_detail.*
import org.json.JSONObject
import java.io.Serializable
import java.util.concurrent.TimeUnit

/**
 * 班级详情页面
 */
class ClassesDetailFragment : BaseFragment(), MessageListener, IClassesDetailContract.View {

    override fun layoutId(): Int = R.layout.fragment_classes_detail

    private val mPushServicePresenter by lazy { PushServicePresenter(context!!, this) }

    private val mSunVoteServicePresenter: SunVoteServicePresenter by lazy {
        SunVoteServicePresenter(context!!, object : SunARSListenerAdapter() {
        })
    }

    var REQUEST_CODE = 10001

    companion object {

        fun forClasses(classes: ClassesEntity): ClassesDetailFragment {
            val fragment = ClassesDetailFragment()
            val arguments = Bundle()
            arguments.putSerializable(CLASSES, classes)
            fragment.arguments = arguments
            return fragment
        }

        /**
         * 班级学生列表的集合
         */
        var mStudentList: MutableList<StudentsEntity>? = null

        var mClassesEntity: ClassesEntity? = null
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mClassesEntity = arguments?.getSerializable(CLASSES) as ClassesEntity
        //获取学生列表
        mPresenter.getStudentsByClassesId(mClassesEntity?.klass!!.id.toString())

        readyClass.setOnClickListener {
            context?.let {
                //准备就绪，检查答题器
                startActivityForResult(CheckKeyPadActivity.callingIntent(it, ClassesFragment.ACTION_TO_CLASS), REQUEST_CODE)
            }
        }

        endClass.setOnClickListener { activity?.finish() }

        classesTitle.text = mClassesEntity?.klass?.name
        classesSubTitle.text = mClassesEntity?.klass?.addr
        classesDate.text = """${mClassesEntity?.klass?.startTime}-${mClassesEntity?.klass?.endTime}"""

        if (BuildConfig.DEBUG) {
            //开始Socket监听
            mPushServicePresenter.bind()

            mSunVoteServicePresenter.bind()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        //开始Socket监听
                        mPushServicePresenter.bind()

                        context?.run {
                            try {
                                startActivity(IntentUtils.callingXYDial())
                            } catch (e: Exception) {
                                e.printStackTrace()
                                ToastUtils.showShort("请使用小鱼易联")
                            }
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()

        mPushServicePresenter.unBind()

        if (BuildConfig.DEBUG) {
            mSunVoteServicePresenter.unBind()
        }
    }


    override fun onMessageResponse(msg: MessageEntity): Boolean {
        when (msg.cmd) {
            CMD_SEND_QUESTION -> {
                //开始答题
                val question = msg.getData(Question::class.java)
                if (question.classId == mClassesEntity?.klass?.id) {
                    ActivityUtils.finishActivity(SubjectActivity::class.java)
                    context?.run {
                        startActivity(SubjectActivity.callingIntent(this, question))
                    }
                }
            }
            INTERACT_START -> {
                //抢红包
                val setting = msg.getData(RedPackageSetting::class.java)
                if (setting.classId == mClassesEntity?.klass?.id) {
                    ActivityUtils.finishActivity(RedPackageActivity::class.java)
                    context?.run {
                        startActivity(RedPackageActivity.callingIntent(this, setting))
                    }
                }
            }
            QUIZ -> {
                //课后反馈
                val feedBack = msg.getData(Question::class.java)
                if (feedBack.classId == mClassesEntity?.klass?.id) {
                    ActivityUtils.finishActivity(AfterClassActivity::class.java)
                    context?.run {
                        startActivity(AfterClassActivity.callingIntent(this, feedBack))
                    }
                }
            }
            PK_START -> {
                val pkSetting = msg.getData(Question::class.java)
                if (pkSetting.classId == mClassesEntity?.klass?.id) {
                    ActivityUtils.finishActivity(PKActivity::class.java)
                    context?.run {
                        startActivity(PKActivity.callingIntent(this, pkSetting))
                    }
                }
            }
        }
        return false
    }

    class Question : Serializable {

        var classId: Int? = null
        var type: Int? = null
        var param: String? = null
        var questionId: Int? = null
        var result: String? = null
        var createTime = System.currentTimeMillis()
        var duration: Int? = null
        var signCount: Int = 0

        constructor(classId: Int?, type: Int?, param: String?, questionId: Int?, answer: String?, signCount: Int) {
            this.classId = classId
            this.type = type
            this.param = param
            this.questionId = questionId
            this.result = answer
            this.signCount = signCount
        }

        constructor(classId: Int?, type: Int?, param: String?, questionId: Int?, answer: String?, duration: Int?) {
            this.classId = classId
            this.type = type
            this.param = param
            this.questionId = questionId
            this.result = answer
            this.duration = duration
        }
    }

    /**
     * Netty链接成功
     */
    override fun onServiceStatusConnectChanged(statusCode: Int) {
        when (statusCode) {
            MessageListener.STATUS_CONNECT_SUCCESS -> {
                val json = JSONObject()
                json.put(PushService.CMD, ATTEND_CLASS)
                json.put("classId", mClassesEntity?.klass?.id)
                mPushServicePresenter.mService?.sendMessage(json.toString())
            }
        }
    }

    override var mPresenter: IClassesDetailContract.Presenter = ClassesDetailPresenter()

    override fun showLoding() {
        showProgress()
    }

    override fun studentList(list: MutableList<StudentsEntity>) {
        hideProgress()
        mStudentList = list

        //提前下载学生头像缓存
        mStudentList?.let {
            for (student in it) {
                GlideApp.with(this)
                        .asBitmap()
                        .load(student.head)
                        .submit()
            }
        }

    }

    override fun onNetworkError(throwable: Throwable): Boolean {
        hideProgress()
        return true
    }
}