package com.prance.teacher.features.subject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.sunars.sdk.SunARS
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.socket.*
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.redpackage.model.StudentScore
import com.prance.teacher.features.redpackage.view.RankFragment
import com.prance.teacher.features.students.model.StudentsEntity
import com.prance.teacher.features.subject.SubjectActivity.Companion.QUESTION
import com.prance.teacher.features.subject.contract.ISubjectContract
import com.prance.teacher.features.subject.presenter.SubjectPresenter
import com.prance.teacher.features.subject.view.SubjectOnDestroyFragment
import com.prance.teacher.features.subject.view.SubjectOnDestroyFragment.Companion.QUESTION_RESULT
import com.prance.teacher.features.subject.view.SubjectOnStartFragment
import com.prance.teacher.features.subject.view.SubjectOnStopFragment
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

/**
 * 上课答题
 */
class SubjectRankActivity : BaseActivity(){

    companion object {

        fun callingIntent(context: Context, questionResult: SubjectOnDestroyFragment.QuestionResult): Intent {
            val intent = Intent(context, SubjectRankActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            intent.putExtra(QUESTION_RESULT, questionResult)
            return intent
        }
    }

    override fun fragment(): BaseFragment = SubjectOnDestroyFragment.forQuestionResult(intent.getSerializableExtra(QUESTION_RESULT) as SubjectOnDestroyFragment.QuestionResult)

}
