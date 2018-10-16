package com.prance.teacher.features.students.view

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import com.prance.lib.base.extension.invisible
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.spark.SparkService
import com.prance.teacher.features.students.contract.IStudentsContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.deletekeypad.DeleteKeyPadActivity
import com.prance.teacher.features.modifybind.StudentsModifyBindActivity
import com.prance.teacher.features.modifybind.view.StudentsModifyBindFragment
import com.prance.teacher.features.students.model.StudentsEntity
import com.prance.teacher.features.students.presenter.StudentsPresenter
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.fragment_students.*
import java.util.concurrent.TimeUnit

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

    private val bindProgress by lazy(mode = LazyThreadSafetyMode.NONE) {
        context?.run {
            BindLoadingDialog(this)
        }
    }

    private val bindSuccessDialog by lazy(mode = LazyThreadSafetyMode.NONE) {
        context?.run {
            BindSuccessDialog(this)
        }
    }

    private var mAdapter = StudentsAdapter(R.layout.item_students)

    private var mModifyBindRequestCode = 10086

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

        startClass.setOnClickListener { activity?.finish() }

        start.setOnClickListener {
            SparkService.mUsbSerialNum?.let {
                showBindProgress()
                mPresenter.startBind(mClassesEntity.klass?.id.toString(), it)
            }
        }

        modifyBind.setOnClickListener {
            context?.let {
                startActivityForResult(StudentsModifyBindActivity.callingIntent(it, mClassesEntity), mModifyBindRequestCode)
            }
        }

        SparkService.mUsbSerialNum?.run {
            matchKeyPadCount.text = Html.fromHtml("""答题器数 <font color="#3AF0EE">${mPresenter.getKeyPadCount(this)}</font>""")
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

    private fun showBindProgress() {
        bindProgress?.show()
    }

    private fun hideBindProgress() {
        bindProgress?.dismiss()
    }

    override fun renderStudents(list: MutableList<StudentsEntity>) {
        hideProgress()
        mAdapter.setNewData(list)
        mAdapter.notifyDataSetChanged()

        studentCount.text = Html.fromHtml("""班级人数 <font color="#3AF0EE">${list.size}</font>""")

        calculateBindStudent()

        displayBtn()
    }

    /**
     * 按钮展示逻辑，全部绑定后，才有开始上课按钮
     */
    private fun displayBtn() {
        var existUnBindStudent = false

        for (s in mAdapter.data) {
            if (s.getClicker() == null) {
                existUnBindStudent = true
            }
        }

        if (existUnBindStudent) {
            start.visible()
            startClass.invisible()
            modifyBind.invisible()
            startClass.requestFocus()
        } else {
            startClass.visible()
            modifyBind.visible()
            start.invisible()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_students

    override fun onNetworkError(throwable: Throwable): Boolean {
        hideProgress()
        return true
    }

    override fun bindFail() {
        hideBindProgress()
        ToastUtils.showShort("绑定失败，请按“OK”键重新绑定")
        start.requestFocus()
    }

    override fun bindSuccess() {
        hideBindProgress()
        bindSuccessDialog?.show()
        Flowable.timer(3, TimeUnit.SECONDS)
                .mySubscribe {
                    bindSuccessDialog?.dismiss()
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mModifyBindRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                data?.run {
                    val students = (getSerializableExtra(StudentsModifyBindFragment.STUDENTS) as DeleteKeyPadActivity.SerializableList<StudentsEntity>).list
                    mAdapter.setNewData(students)
                    mAdapter.notifyDataSetChanged()
                }
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
        startClass.requestFocus()
    }
}

