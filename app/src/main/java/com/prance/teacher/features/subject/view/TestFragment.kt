package com.prance.teacher.features.subject.view

import android.os.Bundle
import android.view.View
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R

class TestFragment :BaseFragment(){
    override fun layoutId(): Int {
        return R.layout.fragment_test1
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

    }
}