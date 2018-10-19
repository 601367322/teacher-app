package com.prance.teacher.features.modifybind

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.Constants.CLASSES
import com.prance.lib.server.vo.teacher.ClassVo
import com.prance.lib.spark.SparkService
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.BuildConfig
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.modifybind.view.StudentsModifyBindFragment
import com.prance.teacher.features.students.view.StudentsFragment

class StudentsModifyBindActivity : BaseActivity() {

    companion object {

        fun callingIntent(context: Context, classes: ClassVo): Intent {
            val intent = Intent(context, StudentsModifyBindActivity::class.java)
            intent.putExtra(CLASSES, classes)
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

    override fun fragment(): BaseFragment = StudentsModifyBindFragment.forClasses(intent.getSerializableExtra(CLASSES) as ClassVo)
}