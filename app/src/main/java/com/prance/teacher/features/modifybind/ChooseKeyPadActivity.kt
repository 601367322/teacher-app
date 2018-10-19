package com.prance.teacher.features.modifybind

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.Constants.CLASSES
import com.prance.lib.common.utils.Constants.POSITION
import com.prance.lib.common.utils.Constants.STUDENTS
import com.prance.lib.server.vo.teacher.ClassVo
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.deletekeypad.DeleteKeyPadActivity
import com.prance.teacher.features.modifybind.view.ChooseKeyPadFragment
import com.prance.teacher.features.modifybind.view.StudentsModifyBindFragment
import com.prance.teacher.features.students.model.StudentsEntity

class ChooseKeyPadActivity : BaseActivity() {

    companion object {

        fun callingIntent(context: Context, list: DeleteKeyPadActivity.SerializableList<StudentsEntity>, mClassesEntity: ClassVo, position: Int): Intent {
            val intent = Intent(context, ChooseKeyPadActivity::class.java)
            intent.putExtra(STUDENTS, list)
            intent.putExtra(CLASSES, mClassesEntity)
            intent.putExtra(POSITION, position)
            return intent
        }
    }

    override fun fragment(): BaseFragment = ChooseKeyPadFragment
            .forStudents(
                    intent.getSerializableExtra(STUDENTS) as DeleteKeyPadActivity.SerializableList<StudentsEntity>,
                    intent.getSerializableExtra(CLASSES) as ClassVo,
                    intent.getIntExtra(POSITION,0)
            )

}