package com.prance.teacher.features.pk.view

import android.os.Bundle
import android.view.View
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.pk.model.PKResult

class PKRankFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_pk_rank

    companion object {

        const val PK_RESULT = "PKResult"

        fun forPKResult(pkResult: PKResult): PKRankFragment {
            var fragment = PKRankFragment()
            val bundle = Bundle()
            bundle.putSerializable(PK_RESULT, pkResult)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

    }
}