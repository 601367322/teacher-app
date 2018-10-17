package com.prance.teacher.features.modifybind

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.deletekeypad.DeleteKeyPadActivity
import com.prance.teacher.features.modifybind.view.ChooseKeyPadFragment
import com.prance.teacher.features.modifybind.view.StudentsModifyBindFragment
import com.prance.teacher.features.students.model.StudentsEntity

class ChooseKeyPadActivity : BaseActivity() {

    companion object {
        const val CLASSES = "classes"

        const val POSITION = "position"

        fun callingIntent(context: Context, list: DeleteKeyPadActivity.SerializableList<StudentsEntity>, mClassesEntity: ClassesEntity, position: Int): Intent {
            val intent = Intent(context, ChooseKeyPadActivity::class.java)
            intent.putExtra(StudentsModifyBindFragment.STUDENTS, list)
            intent.putExtra(CLASSES, mClassesEntity)
            intent.putExtra(POSITION, position)
            return intent
        }
    }

    override fun fragment(): BaseFragment = ChooseKeyPadFragment
            .forStudents(
                    intent.getSerializableExtra(StudentsModifyBindFragment.STUDENTS) as DeleteKeyPadActivity.SerializableList<StudentsEntity>,
                    intent.getSerializableExtra(CLASSES) as ClassesEntity,
                    intent.getIntExtra(POSITION,0)
            )

}