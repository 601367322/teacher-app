package com.prance.teacher.features.students.view

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.TextUtils
import android.view.View
import com.prance.lib.base.extension.invisible
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.Constants.CLASSES
import com.prance.lib.common.utils.Constants.STUDENTS
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.server.vo.teacher.ClassVo
import com.prance.lib.spark.SparkService
import com.prance.teacher.features.students.contract.IStudentsContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.classes.ClassesNextStepActivity
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.classes.view.ClassesFragment
import com.prance.teacher.features.deletekeypad.DeleteKeyPadActivity
import com.prance.teacher.features.main.MainActivity
import com.prance.teacher.features.modifybind.StudentsModifyBindActivity
import com.prance.teacher.features.students.model.StudentEntity
import com.prance.teacher.features.students.presenter.StudentsPresenter
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.fragment_students.*
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  下午12:56
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class StudentsFragment : BaseFragment(), IStudentsContract.View {

    companion object {

        fun forClasses(classes: ClassVo): StudentsFragment {
            val fragment = StudentsFragment()
            val arguments = Bundle()
            arguments.putSerializable(CLASSES, classes)
            fragment.arguments = arguments
            return fragment
        }
    }

    lateinit var mClassesEntity: ClassVo

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
        mClassesEntity = arguments?.getSerializable(CLASSES) as ClassVo

        recycler.layoutManager = GridLayoutManager(activity, 6)

        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect?.bottom = resources.getDimensionPixelOffset(R.dimen.m48_0)
            }
        })

        recycler.adapter = mAdapter

        startClass.setOnClickListener {
            EventBus.getDefault().post(MainActivity.EventMainStartClass())
            activity?.finish()
        }

        start.setOnClickListener {
            SparkService.mUsbSerialNum?.let {
                showBindProgress()
                mPresenter.startBind(mClassesEntity.id.toString(), it)
            }
        }

        modifyBind.setOnClickListener {
            context?.let {
                startActivityForResult(StudentsModifyBindActivity.callingIntent(it, mClassesEntity), mModifyBindRequestCode)
            }
        }

        updateKeyPadCount()

        loadData()
    }

    private fun loadData() {
        showProgress()
        val id = mClassesEntity.id.toString()
        id.let {
            mPresenter.getStudentsByClassesId(id)
        }
    }

    override fun onResume() {
        super.onResume()

        updateKeyPadCount()
    }

    private fun updateKeyPadCount() {
        SparkService.mUsbSerialNum?.run {
            matchKeyPadCount.text = Html.fromHtml("""答题器数 <font color="#3AF0EE">${mPresenter.getKeyPadCount(this)}</font>""")
        }
    }

    private fun showBindProgress() {
        bindProgress?.show()
    }

    private fun hideBindProgress() {
        bindProgress?.dismiss()
    }

    override fun renderStudents(list: MutableList<StudentEntity>) {
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
            if (s.clickerNumber == null) {
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

    override fun bindFail() {
        hideBindProgress()
        ToastUtils.showShort("绑定失败，请按“OK”键重新绑定")
        start.requestFocus()
    }

    override fun bindSuccess() {
        hideBindProgress()

        //刷新班级信息
        EventBus.getDefault().post(ClassesFragment.RefreshClasses())

        //更新用户列表和答题器显示
        ClassesDetailFragment.mStudentList = mAdapter.data
        EventBus.getDefault().post(ClassesDetailFragment.SendNameToKeyPad())

        //弹出绑定成功提示，3秒后自动消失
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
                    val students = (getSerializableExtra(STUDENTS) as DeleteKeyPadActivity.SerializableList<StudentEntity>).list
                    mAdapter.setNewData(students)
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun calculateBindStudent() {
        bindStudentCount.text = Html.fromHtml("""已绑定人数 <font color="#3AF0EE">${getBindStudentCount()}</font>""")
    }

    private fun getBindStudentCount(): Int {
        var count = 0
        for (student in mAdapter.data) {
            if (student.clickerNumber != null) {
                count++
            }
        }
        return count
    }

    override fun checkMatch() {
        var hasNoKeyPadStudent: StudentEntity? = null
        for (student in mAdapter.data) {
            if (TextUtils.isEmpty(student.clickerNumber)) {
                hasNoKeyPadStudent = student
            }
        }
        hasNoKeyPadStudent?.let {
            ToastUtils.showShort("答题器数量不足")

            mClassesEntity.bindingCount = getBindStudentCount()
            activity?.run {
                finish()

                startActivity(ClassesNextStepActivity.callingIntent(this, mClassesEntity))
            }
        }
        startClass?.requestFocus()
    }
}

