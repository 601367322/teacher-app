package com.prance.teacher.features.students.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.prance.teacher.features.students.contract.IStudentsContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.teacher.base.weight.FocusGridLayoutManager
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.replacekeypad.ReplaceKeyPadActivity
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

    lateinit var mClassesEntity: ClassesEntity

    private var mAdapter = StudentsAdapter()

    override var mPresenter: IStudentsContract.Presenter = StudentsPresenter()

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mClassesEntity = arguments?.getSerializable(CLASSES) as ClassesEntity

        recycler.layoutManager = FocusGridLayoutManager(activity, 10)
        recycler.adapter = mAdapter

        refresh.setOnClickListener {
            loadData()
        }

        refresh.performClick()

        complete.setOnClickListener { activity?.finish() }

        start.setOnClickListener {
            showProgress()
            application.mBaseStation.sn?.let {
                mPresenter.startBind(mClassesEntity.klass?.id.toString(), application.mBaseStation.sn)
            }
        }

        replace.setOnClickListener {
            context?.let {
                startActivityForResult(ReplaceKeyPadActivity.callingIntent(it, mClassesEntity), 1001)
            }
        }
    }

    private fun loadData() {
        showProgress()
        val id = mClassesEntity.klass?.id.toString()
        id.let {
            mPresenter.getStudentsByClassesId(id)
        }
    }

    override fun renderStudents(list: MutableList<StudentsEntity>) {
        hideProgress()
        mAdapter.data = list
        mAdapter.notifyDataSetChanged()

        displayBtn(mClassesEntity)
    }

    private fun displayBtn(classes: ClassesEntity) {
        start.visibility = View.VISIBLE
        if (classes.binding > 0) {
            complete.visibility = View.VISIBLE
            replace.visibility = View.VISIBLE

            //本班学员都存在绑定关系后，不进入绑定状态，“开始绑定”按钮和文字提示隐藏。如下方“图2.1”；当学员0个绑定时，如“图1.3”；当学员绑定、非绑定状态都存在，如“图3.3”；
            if (classes.binding >= mAdapter.data.size) {
                start.visibility = View.GONE
                complete.requestFocus()
            }
        }
    }

    override fun layoutId(): Int = R.layout.fragment_students

    override fun onNetworkError(throwable: Throwable): Boolean {
        hideProgress()
        return true
    }

    override fun bindFail() {
        hideProgress()
        ToastUtils.showShort("绑定失败，请按“OK”键重新绑定")
        start.requestFocus()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                refresh.performClick()
            }
        }
    }

    override fun checkMatch() {
        var hasNoKeyPadStudent: StudentsEntity? = null
        for (student in mAdapter.data) {
            if (student.clickers == null || student.clickers?.isEmpty()!!) {
                hasNoKeyPadStudent = student
            }
        }
        hasNoKeyPadStudent?.let {
            ToastUtils.showShort("答题器数量不够，请去配对足够的答题器对未绑定的学员进行绑定操作")
        }
        complete.requestFocus()
    }
}

