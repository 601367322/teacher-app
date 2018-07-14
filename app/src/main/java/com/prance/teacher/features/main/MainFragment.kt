package com.prance.teacher.features.main

import android.os.Bundle
import android.view.View
import com.prance.teacher.R
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.sunvote.service.SunVoteService

class MainFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_main

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        activity?.run {
            startService(SunVoteService.callingIntent(this))
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        activity?.run {
            stopService(SunVoteService.callingIntent(this))
        }
    }

}