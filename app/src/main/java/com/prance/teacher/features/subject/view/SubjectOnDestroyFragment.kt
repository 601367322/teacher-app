package com.prance.teacher.features.subject.view

import android.os.Bundle
import android.view.View
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import kotlinx.android.synthetic.main.fragment_subject_on_destroy.*
import java.io.Serializable
import kotlin.math.min

class SubjectOnDestroyFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_subject_on_destroy

    var mQuestionResult: QuestionResult? = null

    companion object {

        const val QUESTION_RESULT = "questionResult"

        fun forQuestionResult(questionResult: QuestionResult): SubjectOnDestroyFragment {
            var fragment = SubjectOnDestroyFragment()
            val bundle = Bundle()
            bundle.putSerializable(QUESTION_RESULT, questionResult)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mQuestionResult = arguments?.getSerializable(QUESTION_RESULT) as QuestionResult?

        mQuestionResult?.run {
            answerResult.text = """答对：${answerMsg?.right}人      答错：${answerMsg?.wrong}人     未作答：${answerMsg?.noAnswer}人"""

            var sb = StringBuilder()
            for (i in 0..min(5, rank.size)) {
                sb.append("""第${i}名：${rank[i].name}""")
                sb.append("\n")
            }
            rankText.text = sb.toString()
        }
    }

    class QuestionResult : Serializable {

        var classId: Int? = null
        var answerMsg: Answer? = null
        var answer: String? = null
        var rank: List<Student> = mutableListOf()

        constructor()

        constructor(classId: Int?, answerMsg: Answer?, answer: String?, rank: List<Student>) {
            this.classId = classId
            this.answerMsg = answerMsg
            this.answer = answer
            this.rank = rank
        }


    }

    class Answer : Serializable {
        var right: Int = 0
        var noAnswer: Int = 0
        var wrong: Int = 0
    }

    class Student : Serializable {
        var name: String? = null
    }

}