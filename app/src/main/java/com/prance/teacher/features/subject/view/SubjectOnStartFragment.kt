package com.prance.teacher.features.subject.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import cn.sunars.sdk.SunARS
import com.prance.lib.sunvote.platform.UsbManagerImpl
import com.prance.lib.sunvote.service.SunARSListenerAdapter
import com.prance.lib.sunvote.service.SunVoteServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.match.view.generateKeyPadId
import com.prance.teacher.features.subject.SubjectActivity
import com.prance.teacher.features.subject.model.KeyPadResult
import kotlinx.android.synthetic.main.fragment_after_class.*
import java.util.*

/**
 * 开始答题
 */
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

        UsbManagerImpl.baseStation.sn?.let {
            mQuestion?.run {
                //基站开始发送题目
                SunARS.voteStart(type!!, param)
            }
        }

        mSunVoteServicePresenter.bind()
    }

    private val mSunVoteServicePresenter: SunVoteServicePresenter by lazy { SunVoteServicePresenter(context!!, object : SunARSListenerAdapter() {

        /**
         * 防止初始化的时候，UsbManagerImpl.baseStation是空的，当重新绑定Service的时候，再执行发一遍题目
         * SunARS.voteStart(type!!, param) 这行代码，只执行一次，要么初始化时执行，要么绑定完Service，打开usb后执行
         */
        override fun onConnectEventCallBack(iBaseID: Int, iMode: Int, sInfo: String?) {
            super.onConnectEventCallBack(iBaseID, iMode, sInfo)
            if (sInfo == SunARS.BaseStation_Connected) {
                mQuestion?.run {
                    //基站开始发送题目
                    SunARS.voteStart(type!!, param)
                }
            }
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
    })}



    override fun onDestroy() {
        super.onDestroy()
        //停止发送
        SunARS.voteStop()

        mSunVoteServicePresenter.unBind()
    }


}