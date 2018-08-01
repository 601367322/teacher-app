package com.prance.teacher.features.subject.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import cn.sunars.sdk.SunARS
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.match.view.generateKeyPadId
import com.prance.teacher.features.subject.SubjectActivity
import com.prance.teacher.features.subject.model.KeyPadResult
import java.util.*

class SubjectOnStartFragment : BaseFragment() {

    val mResult = mutableListOf<KeyPadResult>()
    var mQuestion: ClassesDetailFragment.Question? = null

    companion object {

        fun forQuestion(question: ClassesDetailFragment.Question): SubjectOnStartFragment {
            var fragment = SubjectOnStartFragment()
            val bundle = Bundle()
            bundle.putSerializable(SubjectActivity.QUESTION, question)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_subject_on_start

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mQuestion = arguments?.getSerializable(SubjectActivity.QUESTION) as ClassesDetailFragment.Question?

        application.mBaseStation.sn?.let {
            mQuestion?.run {
                SunARS.voteStart(type!!, param)
            }
        }
    }

    override fun needSunVoteService(): Boolean = true

    override fun onConnectEventCallBack(iBaseID: Int, iMode: Int, sInfo: String?) {
        super.onConnectEventCallBack(iBaseID, iMode, sInfo)
        if (sInfo == SunARS.BaseStation_Connected) {
            mQuestion?.run {
                SunARS.voteStart(type!!, param)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SunARS.voteStop()
    }

    override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
        val keyId = generateKeyPadId(KeyID)
        when (iMode) {
            SunARS.KeyResult_info -> {
                sInfo?.let {
                    mResult.add(KeyPadResult(keyId, sInfo, Date().time))
                }
            }
        }
    }
}