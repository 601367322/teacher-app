package com.prance.teacher.features.afterclass.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.prance.lib.common.utils.Constants.FEED_BACK
import com.prance.lib.spark.SparkListenerAdapter
import com.prance.lib.spark.SparkService
import com.prance.lib.spark.SparkServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.afterclass.contract.IAfterClassContract
import com.prance.teacher.features.afterclass.presenter.AfterClassPresenter
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.students.model.StudentEntity
import com.spark.teaching.answertool.usb.model.ReceiveAnswer
import kotlinx.android.synthetic.main.fragment_after_class.*

/**
 * Description : 课后答题界面
 * @author  rich
 * @date 2018/7/25  下午5:15
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class AfterClassFragment : BaseFragment(), IAfterClassContract.View {

    lateinit var mTime: TextView
    var mQuestion: ClassesDetailFragment.Question? = null

    override var mPresenter: IAfterClassContract.Presenter = AfterClassPresenter()

    override fun layoutId(): Int = R.layout.fragment_after_class

    private var mSignStudents: MutableList<StudentEntity> = mutableListOf()

    private val mSparkServicePresenter by lazy {
        SparkServicePresenter(context!!, object : SparkListenerAdapter() {

            override fun onAnswer(answer: ReceiveAnswer) {
                timer.post {
                    val keyId = answer.uid.toString()

                    //签到学员才可以课后反馈
                    ClassesDetailFragment.checkIsSignStudent(mQuestion?.signStudents, keyId)
                            ?: return@post

                    mPresenter.saveChoose(keyId, answer.answer)
                }
            }

            override fun onServiceConnected() {
                super.onServiceConnected()

                startSendQuestion()
            }

        })
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mTime = rootView.findViewById(R.id.timer)
        mQuestion = arguments?.getSerializable(FEED_BACK) as ClassesDetailFragment.Question

        mSignStudents = ClassesDetailFragment.getSignStudents(mQuestion?.signStudents)

        mPresenter.startReceive(mQuestion!!)

        mSparkServicePresenter.bind()
    }

    override fun onTimeChange(time: String) {
        mTime.text = time
    }

    override fun showLoading() {
        showProgress()
    }

    private fun startSendQuestion() {
        mSparkServicePresenter.sendQuestion(SparkService.QuestionType.SINGLE)
    }

    override fun stopSendQuestion() {
        mSparkServicePresenter.stopAnswer()
    }

    override fun onNetworkError(throwable: Throwable): Boolean {
        hideProgress()
        activity?.finish()
        return false
    }

    override fun confirmChooseSuccess() {
        activity?.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.stopReceive()

        mSparkServicePresenter.unBind()
    }
}

