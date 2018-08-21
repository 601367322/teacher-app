package com.prance.teacher.features.subject.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.students.model.StudentsEntity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_subject_on_destroy.*
import java.io.Serializable
import java.util.concurrent.TimeUnit
import kotlin.math.min

class SubjectOnDestroyFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_subject_on_destroy

    var mQuestionResult: QuestionResult? = null

    var mDisposable: Disposable? = null
    var mTotalTime = 5

    var answerMap = mutableMapOf("A" to R.drawable.answer_a, "B" to R.drawable.answer_b, "C" to R.drawable.answer_c, "D" to R.drawable.answer_d, "对" to R.drawable.answer_true, "错" to R.drawable.answer_false)
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
            for (a in answer!!) {
                rightAnswers.addView(createAnswerImg(a))
            }

            answerResult.text = """答对：${answerMsg?.right}人       答错：${answerMsg?.wrong}人       未作答：${answerMsg?.noAnswer}人"""

            if (rank.isNotEmpty()) {
                for (i in 0..min(4, rank.size - 1)) {
                    rankNames[i].text = rank[i].name
                    rankAvatars[i].visibility = View.VISIBLE
                    GlideApp.with(this@SubjectOnDestroyFragment)
                            .load(rank[i].head)
                            .into(rankAvatars[i])
                }
            }
        }

        updateTimeText()
        mDisposable = Flowable.interval(1000, TimeUnit.MILLISECONDS)
                .take(5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mTotalTime--

                    updateTimeText()
                }
    }

    private fun updateTimeText() {
        if (mTotalTime == 0) {
            activity?.finish()
        }
        time.text = "${mTotalTime}秒"
    }

    override fun onDestroy() {
        super.onDestroy()

        mDisposable?.dispose()
    }

    fun createAnswerImg(char: Char): ImageView {
        val image = ImageView(context)
        image.layoutParams = LinearLayout.LayoutParams(resources.getDimensionPixelOffset(R.dimen.m73_0),resources.getDimensionPixelOffset(R.dimen.m73_0))
        image.setImageResource(answerMap[char.toString()]!!)
        return image
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