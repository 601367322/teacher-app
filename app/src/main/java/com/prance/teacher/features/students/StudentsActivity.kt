package com.prance.teacher.features.students

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.spark.SparkService
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.BuildConfig
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
        if (!BuildConfig.DEBUG && SparkService.mUsbSerialNum == null) {
            ToastUtils.showShort("请先连接基站")
            finish()
            return
        }
        super.initView(savedInstanceState)
    }

    override fun fragment(): BaseFragment = StudentsFragment.forClasses(intent.getSerializableExtra(StudentsFragment.CLASSES) as ClassesEntity)
}