package com.prance.teacher.features.subject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.Constants.QUESTION
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.MessageEntity
import com.prance.lib.socket.*
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.R
import com.prance.teacher.core.OnStartClassActivity
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.pk.model.PKResult
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
class SubjectActivity : BaseActivity(), ISubjectContract.View, MessageListener, OnStartClassActivity {

    private var mQuestion: ClassesDetailFragment.Question? = null

    private val mPushServicePresenterPresenter by lazy { PushServicePresenter(this, this) }

    private var onStartFragment: SubjectOnStartFragment? = null

    override fun onMessageResponse(msg: MessageEntity): Boolean {
        when (msg.cmd) {
            PushService.CMD_END_QUESTION -> {
                //结束答题
                val question = msg.getData(ClassesDetailFragment.Question::class.java)
                if (question.classId == mQuestion?.classId) {
                    onSubjectStop()
                }
                //重新发送名字
                EventBus.getDefault().post(ClassesDetailFragment.SendNameToKeyPad())
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
//                                        StudentEntity("申兵兵", "http://cdn.aixifan.com/acfun-pc/2.4.13/img/logo.png"),
//                                        StudentEntity("申兵兵", "http://cdn.aixifan.com/acfun-pc/2.4.13/img/logo.png"),
//                                        StudentEntity("申兵兵", "http://cdn.aixifan.com/acfun-pc/2.4.13/img/logo.png"),
//                                        StudentEntity("申兵兵", "http://cdn.aixifan.com/acfun-pc/2.4.13/img/logo.png"),
//                                        StudentEntity("申兵兵", "http://cdn.aixifan.com/acfun-pc/2.4.13/img/logo.png")
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
                    if (result == null) {
                        supportFragmentManager.inTransaction {
                            replace(R.id.fragmentContainer, SubjectOnWaitingFragment())
                        }
                    }
                }
    }

    var result: SubjectRankFragment.QuestionResult? = null

    private fun onSubjectDestroy(questionResult: SubjectRankFragment.QuestionResult) {
        this.result = questionResult
        supportFragmentManager.inTransaction {
            replace(R.id.fragmentContainer, SubjectRankFragment.forQuestionResult(questionResult))
        }
    }
}
