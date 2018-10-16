package com.prance.teacher.features.modifybind.view

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.common.utils.weight.AlertDialog
import com.prance.lib.spark.SparkService
import com.prance.teacher.features.students.contract.IStudentsContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.deletekeypad.DeleteKeyPadActivity
import com.prance.teacher.features.modifybind.contract.IStudentsModifyBindContract
import com.prance.teacher.features.modifybind.presenter.StudentsModifyBindPresenter
import com.prance.teacher.features.students.model.StudentsEntity
import com.prance.teacher.features.students.presenter.StudentsPresenter
import com.prance.teacher.features.students.view.StudentsFragment
import com.prance.teacher.features.students.view.StudentsFragment.Companion.CLASSES
import kotlinx.android.synthetic.main.fragment_students_modify.*
import java.io.Serializable

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  下午12:56
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class StudentsModifyBindFragment : BaseFragment(), IStudentsModifyBindContract.View {

    companion object {

        const val STUDENTS = "students"

        fun forClasses(classes: ClassesEntity): StudentsModifyBindFragment {
            val fragment = StudentsModifyBindFragment()
            val arguments = Bundle()
            arguments.putSerializable(CLASSES, classes)
            fragment.arguments = arguments
            return fragment
        }
    }

    lateinit var mClassesEntity: ClassesEntity

    override fun layoutId(): Int = R.layout.fragment_students_modify

    private var mAdapter = StudentsModifyBindAdapter(R.layout.item_students_modify_bind)

    override var mPresenter: IStudentsModifyBindContract.Presenter = StudentsModifyBindPresenter()

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

        SparkService.mUsbSerialNum?.run {
            matchKeyPadCount.text = Html.fromHtml("""答题器数 <font color="#3AF0EE">${mPresenter.getKeyPadCount(this)}</font>""")
        }

        complete.setOnClickListener {
            val intent = Intent()
            intent.putExtra(STUDENTS, DeleteKeyPadActivity.SerializableList(mAdapter.data))
            activity?.run {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        mAdapter.setOnItemClickListener { _, _, position ->
            context?.run {
                AlertDialog(this)
                        .setMessage("确认修改该绑定关系吗？")
                        .setCancelButton("取消", null)
                        .setConfirmButton("确认") { _ ->

                        }
                        .show()
            }
        }

        loadData()
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
        mAdapter.setNewData(list)
        mAdapter.notifyDataSetChanged()
        studentCount.text = Html.fromHtml("""班级人数 <font color="#3AF0EE">${list.size}</font>""")
        calculateBindStudent()
    }


    override fun onNetworkError(throwable: Throwable): Boolean {
        hideProgress()
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
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
        bindStudentCount.text = Html.fromHtml("""已绑定人数 <font color="#3AF0EE">$count</font>""")
    }

}
