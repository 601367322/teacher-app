package com.prance.teacher.features.subject

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.subject.view.SubjectRankFragment
import com.prance.teacher.features.subject.view.SubjectRankFragment.Companion.QUESTION_RESULT

/**
 * 上课答题
 */
class SubjectRankActivity : BaseActivity(){

    companion object {

        fun callingIntent(context: Context, questionResult: SubjectRankFragment.QuestionResult): Intent {
            val intent = Intent(context, SubjectRankActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            intent.putExtra(QUESTION_RESULT, questionResult)
            return intent
        }
    }

    override fun fragment(): BaseFragment = SubjectRankFragment.forQuestionResult(intent.getSerializableExtra(QUESTION_RESULT) as SubjectRankFragment.QuestionResult)

}
