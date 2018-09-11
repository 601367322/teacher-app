package com.prance.teacher.features.afterclass.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.prance.lib.sunvote.service.SunARSListenerAdapter
import com.prance.lib.sunvote.service.SunVoteServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.afterclass.AfterClassActivity
import com.prance.teacher.features.afterclass.contract.IAfterClassContract
import com.prance.teacher.features.afterclass.presenter.AfterClassPresenter
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.match.view.generateKeyPadId
import com.prance.teacher.features.students.model.StudentsEntity
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

    private var mSignStudents: MutableList<StudentsEntity> = mutableListOf()

    private val mSunVoteServicePresenter: SunVoteServicePresenter by lazy {
        SunVoteServicePresenter(context!!, object : SunARSListenerAdapter() {

            var answerList = mutableListOf<String>()

            override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
                //防止重复提交
                if (answerList.contains(KeyID)) {
                    return
                }
                answerList.add(KeyID)

                timer.post {
                    val keyId = generateKeyPadId(KeyID)

                    //签到学员才可以课后反馈
                    ClassesDetailFragment.checkIsSignStudent(mQuestion?.signStudents, keyId)
                            ?: return@post

                    mPresenter.saveChoose(keyId, sInfo ?: "")
                }
            }

        })
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mTime = rootView.findViewById(R.id.timer)
        mQuestion = arguments?.getSerializable(AfterClassActivity.feedback) as ClassesDetailFragment.Question

        mSignStudents = ClassesDetailFragment.getSignStudents(mQuestion?.signStudents)

        mPresenter.startReceive(mQuestion!!)

        mSunVoteServicePresenter.bind()
    }

    override fun onTimeChange(time: String) {
        mTime.text = time
    }

    override fun showLoading() {
        showProgress()
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

        mSunVoteServicePresenter.unBind()
    }
}

