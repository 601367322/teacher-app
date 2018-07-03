package com.prance.teacher.features.main

import com.prance.teacher.lib.base.core.platform.BaseActivity
import com.prance.teacher.lib.base.core.platform.BaseFragment

class MainActivity : BaseActivity() {

    override fun fragment(): BaseFragment = MainFragment()


}