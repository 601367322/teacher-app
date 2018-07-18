package com.prance.teacher.features.students.view

import android.os.Bundle
import android.view.View
import com.prance.teacher.features.students.contract.IStudentsContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.students.presenter.StudentsPresenter

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  下午12:56
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class StudentsFragment : BaseFragment(), IStudentsContract.View {

    companion object {
        const val CLASSES = "classes"

        fun forClasses(classes: ClassesEntity): StudentsFragment {
            val fragment = StudentsFragment()
            val arguments = Bundle()
            arguments.putSerializable(CLASSES, classes)
            fragment.arguments = arguments
            return fragment
        }
    }

    override var mPresenter: IStudentsContract.Presenter = StudentsPresenter()

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
    }

    override fun layoutId(): Int = R.layout.fragment_students

}

