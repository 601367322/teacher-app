package com.prance.teacher.features.subject.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.students.model.StudentsEntity
import com.prance.teacher.utils.SoundUtils
import io.reactivex.Flowable
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_subject_on_destroy.*
import java.io.Serializable
import java.util.concurrent.TimeUnit
import kotlin.math.min

class SubjectOnDestroyFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_subject_on_destroy

    var mQuestionResult: QuestionResult? = null

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

        SoundUtils.play("rank_background")

        mQuestionResult = arguments?.getSerializable(QUESTION_RESULT) as QuestionResult?

        Flowable.timer(500,TimeUnit.MILLISECONDS)
                .mySubscribe {

                    rankNames = mutableListOf(rankText1, rankText2, rankText3, rankText4, rankText5)
                    rankAvatars = mutableListOf(rankAvatar1, rankAvatar2, rankAvatar3, rankAvatar4, rankAvatar5)

                    mQuestionResult?.run {

                        if (rank.isNotEmpty()) {
                            for (i in 0..min(4, rank.size - 1)) {
                                rankNames[i].text = rank[i].name
                                rankAvatars[i].visibility = View.VISIBLE
                                GlideApp.with(this@SubjectOnDestroyFragment)
                                        .load(rank[i].head)
                                        .into(rankAvatars[i])
                            }
                        }
                    } }
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