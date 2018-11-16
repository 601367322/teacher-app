package com.prance.teacher.features.redpackage.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.Constants.SCORES
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.model.StudentScore
import com.prance.teacher.utils.SoundUtils
import kotlinx.android.synthetic.main.fragment_red_rank.*
import kotlinx.android.synthetic.main.layout_rank_background.*
import kotlinx.android.synthetic.main.layout_rank_background_score_layout.view.*
import java.io.Serializable
import kotlin.math.min

/**
 *Created by rich on 2018/8/8
 */

class RedPackageRankFragment : BaseFragment() {

    lateinit var scores: MutableList<StudentScore>

    var rankNames: MutableList<TextView> = mutableListOf()
    var rankScores: MutableList<TextView> = mutableListOf()
    var rankAvatars: MutableList<ImageView> = mutableListOf()

    companion object {

        fun create(scores: BundleScore): RedPackageRankFragment {
            var fragment = RedPackageRankFragment()
            val bundle = Bundle()
            bundle.putSerializable(SCORES, scores)
            fragment.arguments = bundle
            return fragment
        }

        class BundleScore : Serializable {
            var scores: MutableList<StudentScore>

            constructor(scores: MutableList<StudentScore>) {
                this.scores = scores
            }
        }
    }

    override fun layoutId(): Int = R.layout.fragment_red_rank

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        scores = (arguments?.getSerializable(SCORES) as BundleScore).scores

        SoundUtils.play("rank_background")

        val scoreLayouts = mutableListOf(scoreLayout1, scoreLayout2, scoreLayout3, scoreLayout4, scoreLayout5, scoreLayout6, scoreLayout7, scoreLayout8, scoreLayout9, scoreLayout10)

        for (layout in scoreLayouts) {
            rankNames.add(layout.rankText)
            rankScores.add(layout.scoreText)
            if (layout.rankAvatar != null)
                rankAvatars.add(layout.rankAvatar)
        }

        val rank = sort(scores)

        rankBackground.peopleNumber = rank.size

        for (i in 0 until min(10, rank.size)) {
            rankNames[i].text = rank[i].student.name

            rankScores[i].visible()
            rankScores[i].text = "${rank[i].score}分"

            if (i < rankAvatars.size) {
                rankAvatars[i].visible()
                GlideApp.with(this)
                        .load(rank[i].student.head)
                        .placeholder(R.drawable.default_avatar_boy)
                        .into(rankAvatars[i])
            }
        }


        if (BuildConfig.DEBUG) {
//            rankAvatar1.visible()
//            GlideApp.with(this)
//                    .load(R.drawable.match_empty_view)
//                    .circleCrop()
//                    .into(rankAvatar1)
        }

    }

    /**
     * 快速排序
     */
    private fun sort(array: MutableList<StudentScore>): MutableList<StudentScore> {

        if (array.size == 0 || array.size == 1) return array
        val flag = array[0]
        array.removeAt(0)
        val leftArray = mutableListOf<StudentScore>()
        val rightArray = mutableListOf<StudentScore>()
        array.forEach {
            if (it.score > flag.score) {
                leftArray.add(it)
            } else {
                rightArray.add(it)
            }
        }
        val sortedLeftArray = sort(leftArray)
        val sortedRightArray = sort(rightArray)

        val sortedArray = mutableListOf<StudentScore>()
        sortedLeftArray.forEach {
            sortedArray.add(it)
        }

        sortedArray.add(flag)
        sortedRightArray.forEach {
            sortedArray.add(it)
        }

        return sortedArray
    }
}