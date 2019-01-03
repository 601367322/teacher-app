package com.prance.teacher.features.pk.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.utils.RxTimer.RxTimerUtil
import kotlinx.android.synthetic.main.fragment_on_waiting.*

class PKWaitingFragment : BaseFragment(){

    override fun layoutId(): Int = R.layout.fragment_on_waiting

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        RxTimerUtil.timer(3000,object :RxTimerUtil.IRxNext{
            override fun doNext(number: Long) {
                vsTimeoutHint.inflate()
            }
        })
    }

    override fun onDestroy() {
        RxTimerUtil.cancel()
        super.onDestroy()
    }
}