package com.prance.teacher.features.modifybind

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.deletekeypad.DeleteKeyPadActivity
import com.prance.teacher.features.modifybind.view.ChooseKeyPadFragment
import com.prance.teacher.features.modifybind.view.StudentsModifyBindFragment
import com.prance.teacher.features.students.model.StudentsEntity

class ChooseKeyPadActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context, list: DeleteKeyPadActivity.SerializableList<StudentsEntity>): Intent {
            val intent = Intent(context, ChooseKeyPadActivity::class.java)
            intent.putExtra(StudentsModifyBindFragment.STUDENTS, list)
            return intent
        }
    }

    override fun fragment(): BaseFragment = ChooseKeyPadFragment.forStudents(intent.getSerializableExtra(StudentsModifyBindFragment.STUDENTS) as DeleteKeyPadActivity.SerializableList<StudentsEntity>)

}