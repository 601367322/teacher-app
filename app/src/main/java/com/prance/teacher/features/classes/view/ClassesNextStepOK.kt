package com.prance.teacher.features.classes.view

import android.os.Bundle
import android.view.View
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import kotlinx.android.synthetic.main.fragment_classes_next_step_ok.*

/**
 * 检测完成，所有学生已绑定答题器
 */
class ClassesNextStepOK : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_classes_next_step_ok

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        modifyBind.setOnClickListener {
            activity?.run {
//                startActivity()
            }
        }
    }
}