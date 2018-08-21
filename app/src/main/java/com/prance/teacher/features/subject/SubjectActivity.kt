package com.prance.teacher.features.subject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.database.MessageEntity
import com.prance.lib.socket.*
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.students.model.StudentsEntity
import com.prance.teacher.features.subject.contract.ISubjectContract
import com.prance.teacher.features.subject.presenter.SubjectPresenter
import com.prance.teacher.features.subject.view.SubjectOnCreateFragment
import com.prance.teacher.features.subject.view.SubjectOnDestroyFragment
import com.prance.teacher.features.subject.view.SubjectOnStartFragment
import com.prance.teacher.features.subject.view.SubjectOnStopFragment
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

/**
 * 上课答题
 */
class SubjectActivity : BaseActivity(), ISubjectContract.View, MessageListener {

    private var mQuestion: ClassesDetailFragment.Question? = null

    private val mPushServicePresenterPresenter by lazy { PushServicePresenter(this, this) }

    private var onStartFragment: SubjectOnStartFragment? = null

    override fun onMessageResponse(msg: MessageEntity) {
        when (msg.cmd) {
            PushService.CMD_END_QUESTION -> {
                //结束答题
                val question = msg.getData(ClassesDetailFragment.Question::class.java)
                if (question.classId == mQuestion?.classId) {
                    onSubjectStop()
                }
            }
            PushService.QUESTION_RESULT -> {
                //答题结果
                val question = msg.getData(SubjectOnDestroyFragment.QuestionResult::class.java)
                if (question.classId == mQuestion?.classId) {
                    onSubjectDestroy(question)
                }
            }
        }
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

    override fun fragment(): BaseFragment = SubjectOnCreateFragment()

    override fun getContext(): Context? = this

    override var mPresenter: ISubjectContract.Presenter = SubjectPresenter()

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mQuestion = intent?.getSerializableExtra(QUESTION) as ClassesDetailFragment.Question?

        mPushServicePresenterPresenter.bind()

        if (BuildConfig.DEBUG) {
//        Flowable.timer(6, TimeUnit.SECONDS)
//                .subscribe {
//                    onSubjectStop()
//                }
            Flowable.timer(3, TimeUnit.SECONDS)
                    .subscribe {
                        onSubjectDestroy(
                                SubjectOnDestroyFragment
                                        .QuestionResult(
                                                1,
                                                SubjectOnDestroyFragment.Answer(1, 2, 3),
                                                "ABC",
                                                mutableListOf(
                                                        StudentsEntity("申兵兵", "https://www.baidu.com/img/bd_logo1.png"),
                                                        StudentsEntity("申兵兵", "https://www.baidu.com/img/bd_logo1.png"),
                                                        StudentsEntity("申兵兵", "https://www.baidu.com/img/bd_logo1.png")
                                                )))
                    }
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
                mPresenter.sendResult(it, mResult, mQuestion?.questionId!!.toString())
            }
        }

        supportFragmentManager.inTransaction {
            replace(R.id.fragmentContainer, SubjectOnStopFragment())
        }
    }

    private fun onSubjectDestroy(questionResult: SubjectOnDestroyFragment.QuestionResult) {
        supportFragmentManager.inTransaction {
            replace(R.id.fragmentContainer, SubjectOnDestroyFragment.forQuestionResult(questionResult))
        }
    }
}
