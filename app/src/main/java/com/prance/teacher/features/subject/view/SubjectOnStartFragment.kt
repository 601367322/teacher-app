package com.prance.teacher.features.subject.view

import android.os.Bundle
import android.view.View
import cn.sunars.sdk.SunARS
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.match.view.generateKeyPadId
import com.prance.teacher.features.subject.model.KeyPadResult

class SubjectOnStartFragment : BaseFragment() {

    public val mResult = mutableListOf<Any>()

    override fun layoutId(): Int = R.layout.fragment_subject_on_start

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
    }

    override fun needSunVoteService(): Boolean = true

    override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
        val keyId = generateKeyPadId(KeyID)
        when (iMode) {
            SunARS.KeyResult_info -> {
                sInfo?.let {
                    mResult.add(KeyPadResult(keyId, sInfo))
                }
            }
        }
    }
}