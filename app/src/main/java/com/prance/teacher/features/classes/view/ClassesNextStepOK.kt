package com.prance.teacher.features.classes.view

import android.content.Context
import android.os.Bundle
import android.view.View
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.classes.ClassesNextStepActivity
import com.prance.teacher.features.modifybind.StudentsModifyBindActivity
import kotlinx.android.synthetic.main.fragment_classes_next_step_ok.*

/**
 * 检测完成，所有学生已绑定答题器
 */
class ClassesNextStepOK : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_classes_next_step_ok

    var mActivity: ClassesNextStepActivity? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as ClassesNextStepActivity
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        modifyBind.setOnClickListener {
            activity?.run {
                mActivity?.mClassEntity?.run {
                    startActivity(StudentsModifyBindActivity.callingIntent(activity!!,this))
                    finish()
                }
            }
        }

        startClass.setOnClickListener {
            mActivity?.onTimeEnd()
        }
    }
}