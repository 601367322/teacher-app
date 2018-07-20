package com.prance.teacher.features.classes

import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.classes.view.ClassesFragment

class ClassesDetailActivity:BaseActivity(){

    override fun fragment(): BaseFragment = ClassesDetailFragment()
}