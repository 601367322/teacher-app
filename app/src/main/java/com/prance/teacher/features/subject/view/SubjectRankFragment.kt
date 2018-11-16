package com.prance.teacher.features.subject.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.Constants.QUESTION_RESULT
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.students.model.StudentEntity
import com.prance.teacher.utils.SoundUtils
import kotlinx.android.synthetic.main.fragment_subject_on_destroy.*
import kotlinx.android.synthetic.main.layout_rank_background.*
import kotlinx.android.synthetic.main.layout_rank_background_score_layout.view.*
import java.io.Serializable
import kotlin.math.min

class SubjectRankFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_subject_on_destroy

    var mQuestionResult: QuestionResult? = null

    var rankNames: MutableList<TextView> = mutableListOf()
    var rankAvatars: MutableList<ImageView> = mutableListOf()

    companion object {

        fun forQuestionResult(questionResult: QuestionResult): SubjectRankFragment {
            var fragment = SubjectRankFragment()
            val bundle = Bundle()
            bundle.putSerializable(QUESTION_RESULT, questionResult)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        SoundUtils.play("rank_background")

        mQuestionResult = arguments?.getSerializable(QUESTION_RESULT) as QuestionResult?
        val scoreLayouts = mutableListOf(scoreLayout1, scoreLayout2, scoreLayout3, scoreLayout4, scoreLayout5, scoreLayout6, scoreLayout7, scoreLayout8, scoreLayout9, scoreLayout10)

        for (layout in scoreLayouts) {
            rankNames.add(layout.rankText)
            if (layout.rankAvatar != null)
                rankAvatars.add(layout.rankAvatar)
        }

        mQuestionResult?.run {

            if (rank.isNotEmpty()) {
                rankBackground.peopleNumber = rank.size

                for (i in 0..min(10, rank.size - 1)) {
                    rankNames[i].text = rank[i].name

                    if (i < rankAvatars.size) {
                        rankAvatars[i].visible()
                        GlideApp.with(this@SubjectRankFragment)
                                .load(rank[i].head)
                                .placeholder(R.drawable.default_avatar_boy)
                                .into(rankAvatars[i])
                    }
                }
            }
        }

    }

    class QuestionResult : Serializable {

        var classId: Int? = null
        var rank: List<StudentEntity> = mutableListOf()

        constructor(classId: Int?, rank: List<StudentEntity>) {
            this.classId = classId
            this.rank = rank
        }


    }


}