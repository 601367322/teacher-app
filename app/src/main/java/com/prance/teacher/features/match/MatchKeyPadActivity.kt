package com.prance.teacher.features.match

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.match.view.MatchKeyPadFragment

class MatchKeyPadActivity : BaseActivity() {

    companion object {
        const val STUDENT_COUNT = "studentCount"

        fun callingIntent(context: Context, studentCount: Int?): Intent {
            val intent = Intent(context, MatchKeyPadActivity::class.java)
            studentCount?.run {
                intent.putExtra(STUDENT_COUNT, studentCount)
            }
            return intent
        }
    }

    override fun fragment(): BaseFragment = MatchKeyPadFragment.forStudentCount(intent?.getIntExtra(STUDENT_COUNT, -1))

}