package com.prance.teacher.features.replacekeypad

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.replacekeypad.view.ReplaceKeyPadFragment

import com.prance.teacher.features.students.view.StudentsFragment


class ReplaceKeyPadActivity : BaseActivity() {

    companion object {

        fun callingIntent(context: Context, classes: ClassesEntity): Intent {
            val intent = Intent(context, ReplaceKeyPadActivity::class.java)
            intent.putExtra(StudentsFragment.CLASSES, classes)
            return intent
        }
    }

    override fun fragment(): BaseFragment = ReplaceKeyPadFragment.forClasses(intent.getSerializableExtra(StudentsFragment.CLASSES) as ClassesEntity)
}