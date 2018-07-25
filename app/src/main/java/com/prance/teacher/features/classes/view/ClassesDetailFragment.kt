package com.prance.teacher.features.classes.view

import android.os.Bundle
import android.view.View
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.common.utils.weight.AlertDialog
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.students.view.StudentsFragment.Companion.CLASSES
import com.prance.teacher.utils.IntentUtils
import kotlinx.android.synthetic.main.fragment_classes_detail.*

class ClassesDetailFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_classes_detail

    lateinit var mClassesEntity: ClassesEntity

    companion object {

        fun forClasses(classes: ClassesEntity): ClassesDetailFragment {
            val fragment = ClassesDetailFragment()
            val arguments = Bundle()
            arguments.putSerializable(CLASSES, classes)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mClassesEntity = arguments?.getSerializable(CLASSES) as ClassesEntity

        readyClass.setOnClickListener {
            context?.let {
                AlertDialog(it)
                        .setMessage("确定准备就绪？")
                        .setConfirmButton("确定", {
                            try {
                                startActivity(IntentUtils.callingXYDial())
                            } catch (e: Exception) {
                                e.printStackTrace()
                                ToastUtils.showShort("请使用小鱼易联")
                            }
                        })
                        .setCancelButton("取消", null)
                        .show()
            }
        }

        endClass.setOnClickListener { activity?.finish() }

        classesTitle.text = mClassesEntity.klass?.name
        classesSubTitle.text = mClassesEntity.klass?.addr
        classesDate.text = """${mClassesEntity.klass?.startTime}-${mClassesEntity.klass?.endTime}"""
    }
}