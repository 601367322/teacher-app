package com.prance.teacher.features.classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.BuildConfig
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.redpackage.RedPackageActivity
import com.prance.teacher.features.redpackage.model.RedPackageSetting
import com.prance.teacher.features.students.view.StudentsFragment

class ClassesDetailActivity : BaseActivity() {

    companion object {

        fun callingIntent(context: Context, classes: ClassesEntity): Intent {
            val intent = Intent(context, ClassesDetailActivity::class.java)
            intent.putExtra(StudentsFragment.CLASSES, classes)
            return intent
        }
    }

    override fun fragment(): BaseFragment = ClassesDetailFragment.forClasses(intent?.getSerializableExtra(StudentsFragment.CLASSES) as ClassesEntity)

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        //启动主页
        if (BuildConfig.DEBUG) {
            val redConfig = RedPackageSetting(1, 30, 1, 1)
            startActivity(RedPackageActivity.callingIntent(this, redConfig))
        }
    }
}