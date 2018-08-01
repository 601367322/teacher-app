package com.prance.teacher.features.classes

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.classes.view.ClassesDetailFragment
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
}