package com.prance.teacher.features.login

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R


class LoginFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LogUtils.d(ScreenUtils.getScreenDensity())
        LogUtils.d(ScreenUtils.getScreenDensityDpi())
        LogUtils.d(ScreenUtils.getScreenHeight())
        LogUtils.d(ScreenUtils.getScreenWidth())

    }
}