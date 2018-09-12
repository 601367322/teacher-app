package com.prance.teacher.features.subject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.sunars.sdk.SunARS
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.MessageEntity
import com.prance.lib.socket.*
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.lib.test.setting.features.TestSettingActivity
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.students.model.StudentsEntity
import com.prance.teacher.features.subject.contract.ISubjectContract
import com.prance.teacher.features.subject.presenter.SubjectPresenter
import com.prance.teacher.features.subject.view.SubjectCountTimeFragment
import com.prance.teacher.features.subject.view.SubjectRankFragment
import com.prance.teacher.features.subject.view.SubjectOnStartFragment
import com.prance.teacher.features.subject.view.SubjectOnWaitingFragment
import io.reactivex.Flowable
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

/**
 * 上课答题
 */
class SubjectActivity : BaseActivity(), ISubjectContract.View, MessageListener {

    private var mQuestion: ClassesDetailFragment.Question? = null

    private val mPushServicePresenterPresenter by lazy { PushServicePresenter(this, this) }

    private var onStartFragment: SubjectOnStartFragment? = null

    override fun onMessageResponse(msg: MessageEntity): Boolean {
        when (msg.cmd) {
            PushService.CMD_END_QUESTION -> {
                //停止发送
                SunARS.voteStop()
                //结束答题
                val question = msg.getData(ClassesDetailFragment.Question::class.java)
                if (question.classId == mQuestion?.classId) {
                    onSubjectStop()
                }
            }
            PushService.QUESTION_RESULT -> {
                //答题结果
                val question = msg.getData(SubjectRankFragment.QuestionResult::class.java)
                if (question.classId == mQuestion?.classId) {
                    onSubjectDestroy(question)
                }
            }
        }
        return super.onMessageResponse(msg)
    }

    override fun onServiceStatusConnectChanged(statusCode: Int) {
    }

    companion object {

        const val QUESTION = "question"

        fun callingIntent(context: Context, question: ClassesDetailFragment.Question): Intent {
            val intent = Intent(context, SubjectActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            intent.putExtra(QUESTION, question)
            return intent
        }
    }

    override fun fragment(): BaseFragment = SubjectCountTimeFragment()

    override fun getContext(): Context? = this

    override var mPresenter: ISubjectContract.Presenter = SubjectPresenter()

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mQuestion = intent?.getSerializableExtra(QUESTION) as ClassesDetailFragment.Question?

        mPushServicePresenterPresenter.bind()

        if (BuildConfig.DEBUG) {
//            Flowable.timer(5, TimeUnit.SECONDS)
//                    .subscribe {
//                        onSubjectDestroy(SubjectRankFragment.QuestionResult(1, SubjectRankFragment.Answer(1, 2, 3), "ABC",
//                                mutableListOf(
//                                        StudentsEntity("申兵兵", "http://cdn.aixifan.com/acfun-pc/2.4.13/img/logo.png"),
//                                        StudentsEntity("申兵兵", "http://cdn.aixifan.com/acfun-pc/2.4.13/img/logo.png"),
//                                        StudentsEntity("申兵兵", "http://cdn.aixifan.com/acfun-pc/2.4.13/img/logo.png"),
//                                        StudentsEntity("申兵兵", "http://cdn.aixifan.com/acfun-pc/2.4.13/img/logo.png"),
//                                        StudentsEntity("申兵兵", "http://cdn.aixifan.com/acfun-pc/2.4.13/img/logo.png")
//                                )))
//                    }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mPushServicePresenterPresenter.unBind()
    }

    fun onSubjectStart() {
        mQuestion?.let {
            onStartFragment = SubjectOnStartFragment.forQuestion(it)
            supportFragmentManager.inTransaction {
                replace(R.id.fragmentContainer, onStartFragment)
            }
        }
    }

    private fun onSubjectStop() {

        mQuestion?.classId?.let {
            onStartFragment?.run {
                mPresenter.sendResult(it, mResult, mQuestion?.questionId!!.toString(), if (doubleScore) 1 else 0)
            }
        }

        Flowable.timer(1, TimeUnit.SECONDS)
                .mySubscribe {
                    if (!isDestroyed) {
                        supportFragmentManager.inTransaction {
                            replace(R.id.fragmentContainer, SubjectOnWaitingFragment())
                        }
                    }
                }
    }

    /**
     * 这里有个大坑
     *
     * 该方法内原代码是
     * supportFragmentManager.inTransaction {
     *      replace(R.id.fragmentContainer, SubjectRankFragment.forQuestionResult(questionResult))
     * }
     *
     * 但是，执行完毕后，基站会自动断开。经过反复测试，推测跟内存有关。由于SubjectActivity是透明的，可能会占用更多内存，导致USB读取数据阻塞中断
     *
     * 改为打开一个不透明的Activity，问题解决
     */
    private fun onSubjectDestroy(questionResult: SubjectRankFragment.QuestionResult) {
        //停止发送
        SunARS.voteStop()
        finish()

        System.gc()
        System.runFinalization()

        //TODO 不能在此打开一个新的界面，否则会影响小鱼的视频卡死
        EventBus.getDefault().post(questionResult)
    }
}
