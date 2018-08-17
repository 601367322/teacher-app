package com.prance.teacher.features.students.view

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.sunvote.platform.UsbManagerImpl
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

        recycler.layoutManager = GridLayoutManager(activity, 6)

        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect?.bottom = resources.getDimensionPixelOffset(R.dimen.m48_0)
            }
        })

        recycler.adapter = mAdapter

        refresh.setOnClickListener {
            loadData()
        }

        refresh.performClick()

        complete.setOnClickListener { activity?.finish() }

        start.setOnClickListener {
            showProgress()
            UsbManagerImpl.baseStation.sn?.let {
                mPresenter.startBind(mClassesEntity.klass?.id.toString(), it)
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

        calculateBindStudent()

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

    private fun calculateBindStudent() {
        var count = 0
        for (student in mAdapter.data) {
            if (student.clickers != null) {
                count++
            }
        }
        bindStudentCount.text = Html.fromHtml("""已绑定 <font color="#3AF0EE">$count</font> 名学生""")
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

