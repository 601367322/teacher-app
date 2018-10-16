package com.prance.teacher.features.classes.view

import android.content.Context
import android.os.Bundle
import android.view.View
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.classes.ClassesNextStepActivity
import com.prance.teacher.features.main.MainActivity
import com.prance.teacher.features.students.StudentsActivity
import kotlinx.android.synthetic.main.fragment_classes_next_step_exit_unbind_students.*
import org.greenrobot.eventbus.EventBus

/**
 * 存在未绑定答题器的学生！
 */
class ClassesNextStepExistUnBindStudents : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_classes_next_step_exit_unbind_students
    var mActivity: ClassesNextStepActivity? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as ClassesNextStepActivity
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        startBind.setOnClickListener {
            mActivity?.onTimeEnd()
        }
        jump.setOnClickListener {
            EventBus.getDefault().post(MainActivity.EventMainStartClass())
            activity?.finish()
        }
    }
}