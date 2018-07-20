package com.prance.teacher.features.classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.classes.view.ClassesFragment
import com.prance.teacher.features.students.StudentsActivity

class ClassesActivity : BaseActivity() {


    private var mAction: Int = ClassesFragment.ACTION_TO_CLASS

    companion object {

        fun callingIntent(context: Context) = Intent(context, ClassesActivity::class.java)

        fun callingIntent(context: Context, action: Int): Intent {
            val intent = Intent(context, ClassesActivity::class.java)
            intent.putExtra(ClassesFragment.ACTION, action)
            return intent
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mAction = intent?.getIntExtra(ClassesFragment.ACTION, ClassesFragment.ACTION_TO_CLASS)!!

        super.initView(savedInstanceState)
    }

    override fun fragment(): BaseFragment = ClassesFragment.forAction(intent?.getIntExtra(ClassesFragment.ACTION, ClassesFragment.ACTION_TO_CLASS))

    fun toNext(classesEntity: ClassesEntity) {
        when (mAction) {
            ClassesFragment.ACTION_TO_CLASS -> {
                startActivity(ClassesDetailActivity.callingIntent(this, classesEntity))
            }
            ClassesFragment.ACTION_TO_BIND -> {
                startActivity(StudentsActivity.callingIntent(this, classesEntity))
            }
        }
    }
}