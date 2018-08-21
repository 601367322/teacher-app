package com.prance.teacher.features.subject.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.students.model.StudentsEntity
import kotlinx.android.synthetic.main.fragment_subject_on_destroy.*
import java.io.Serializable
import kotlin.math.min

class SubjectOnDestroyFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_subject_on_destroy

    var mQuestionResult: QuestionResult? = null

    var answerMap = mutableMapOf("A" to R.drawable.answer_a, "B" to R.drawable.answer_b, "C" to R.drawable.answer_c, "D" to R.drawable.answer_d)
    lateinit var rankNames: MutableList<TextView>
    lateinit var rankAvatars: MutableList<ImageView>

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

        rankNames = mutableListOf(rankText1, rankText2, rankText3, rankText4, rankText5)
        rankAvatars = mutableListOf(rankAvatar1, rankAvatar2, rankAvatar3, rankAvatar4, rankAvatar5)

        mQuestionResult?.run {
            rightAnswer.setImageResource(answerMap[answer]!!)

            answerResult.text = """答对：${answerMsg?.right}人       答错：${answerMsg?.wrong}人       未作答：${answerMsg?.noAnswer}人"""

            if (rank.isNotEmpty()) {
                for (i in 0..min(4, rank.size - 1)) {
                    rankNames[i].text = rank[i].name
                    rankAvatars[i].visibility = View.VISIBLE
                    GlideApp.with(this@SubjectOnDestroyFragment)
                            .load(rank[i].head)
                            .circleCrop()
                            .into(rankAvatars[i])
                }
            }
        }
    }

    class QuestionResult : Serializable {

        var classId: Int? = null
        var answerMsg: Answer? = null
        var answer: String? = null
        var rank: List<StudentsEntity> = mutableListOf()

        constructor()

        constructor(classId: Int?, answerMsg: Answer?, answer: String?, rank: List<StudentsEntity>) {
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

        constructor(right: Int, noAnswer: Int, wrong: Int) {
            this.right = right
            this.noAnswer = noAnswer
            this.wrong = wrong
        }
    }

}