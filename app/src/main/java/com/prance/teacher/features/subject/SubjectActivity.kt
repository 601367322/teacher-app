package com.prance.teacher.features.subject

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.database.MessageEntity
import com.prance.lib.socket.MessageListener
import com.prance.lib.socket.PushService
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.subject.contract.ISubjectContract
import com.prance.teacher.features.subject.presenter.SubjectPresenter
import com.prance.teacher.features.subject.view.SubjectOnCreateFragment
import com.prance.teacher.features.subject.view.SubjectOnDestroyFragment
import com.prance.teacher.features.subject.view.SubjectOnStartFragment
import com.prance.teacher.features.subject.view.SubjectOnStopFragment

/**
 * 上课答题
 */
class SubjectActivity : BaseActivity(), ISubjectContract.View, MessageListener {

    var mPushBinder: PushService.PushServiceBinder? = null
    private var mQuestion: ClassesDetailFragment.Question? = null

    var onStartFragment: SubjectOnStartFragment? = null

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
            intent.putExtra(QUESTION, question)
            return intent
        }
    }

    override fun fragment(): BaseFragment = SubjectOnCreateFragment()

    override fun getContext(): Context? = this

    override var mPresenter: ISubjectContract.Presenter = SubjectPresenter()

    private var mPushServiceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPushBinder = service as PushService.PushServiceBinder
            mPushBinder?.addListener(this@SubjectActivity)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mQuestion = intent?.getSerializableExtra(QUESTION) as ClassesDetailFragment.Question?

        bindService(PushService.callingIntent(this), mPushServiceConnection, Service.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()

        mPushBinder?.run {
            removeListener(this@SubjectActivity)
        }
        unbindService(mPushServiceConnection)
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
                mPresenter.sendResult(it, mResult,mQuestion?.questionId!!.toString())
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
