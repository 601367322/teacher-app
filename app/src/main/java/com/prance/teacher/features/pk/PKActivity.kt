package com.prance.teacher.features.pk

import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.pk.view.PKFragment

class PKActivity :BaseActivity(){

    override fun fragment(): BaseFragment? = PKFragment()
}