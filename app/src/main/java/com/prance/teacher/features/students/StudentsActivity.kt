package com.prance.teacher.features.students

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.TeacherApplication
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.students.view.StudentsFragment

class StudentsActivity : BaseActivity() {

    companion object {

        fun callingIntent(context: Context, classes: ClassesEntity): Intent {
            val intent = Intent(context, StudentsActivity::class.java)
            intent.putExtra(StudentsFragment.CLASSES, classes)
            return intent
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        if ((application as TeacherApplication).mBaseStation.sn == null) {
            ToastUtils.showShort("请先连接基站")
            finish()
            return
        }
        super.initView(savedInstanceState)
    }

    override fun fragment(): BaseFragment = StudentsFragment.forClasses(intent.getSerializableExtra(StudentsFragment.CLASSES) as ClassesEntity)
}