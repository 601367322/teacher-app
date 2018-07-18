package com.prance.teacher.features.students.view

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.prance.teacher.features.students.contract.IStudentsContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.teacher.base.weight.FocusGridLayoutManager
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.students.model.StudentsEntity
import com.prance.teacher.features.students.presenter.StudentsPresenter
import kotlinx.android.synthetic.main.fragment_students.*

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

    private var mAdapter = StudentsAdapter()

    override var mPresenter: IStudentsContract.Presenter = StudentsPresenter()

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        recycler.layoutManager = FocusGridLayoutManager(activity, 10)
        recycler.adapter = mAdapter

        refresh.setOnClickListener {
            loadData()
        }

        refresh.performClick()

        complete.setOnClickListener { activity?.finish() }

        val classes = arguments?.getSerializable(CLASSES) as ClassesEntity
        displayBtn(classes)
    }

    private fun loadData() {
        showProgress()
        val classes = arguments?.getSerializable(CLASSES) as ClassesEntity
        val id = classes.klass?.id.toString()
        id.let {
            mPresenter.getStudentsByClassesId(id)
        }
    }

    override fun renderStudents(list: MutableList<StudentsEntity>) {
        hideProgress()
        mAdapter.data = list
        mAdapter.notifyDataSetChanged()
    }

    private fun displayBtn(classes: ClassesEntity) {
        if (classes.binding > 0) {
            complete.visibility = View.VISIBLE
            replace.visibility = View.VISIBLE
        }
    }

    override fun layoutId(): Int = R.layout.fragment_students

    override fun onNetworkError(throwable: Throwable): Boolean {
        hideProgress()
        return false
    }
}

