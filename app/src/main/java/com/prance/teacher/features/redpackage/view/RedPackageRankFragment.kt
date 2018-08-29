package com.prance.teacher.features.redpackage.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.model.StudentScore
import com.prance.teacher.utils.SoundUtils
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_rank.*
import java.io.Serializable
import java.util.concurrent.TimeUnit
import kotlin.math.min

/**
 *Created by rich on 2018/8/8
 */

class RedPackageRankFragment : BaseFragment() {

    lateinit var scores: MutableList<StudentScore>

    var mDisposable: Disposable? = null

    var mTotalTime = 5

    lateinit var rankNames: MutableList<TextView>
    lateinit var rankScores: MutableList<TextView>
    lateinit var rankAvatars: MutableList<ImageView>

    companion object {
        const val SCORES = "scores"
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

    override fun layoutId(): Int = R.layout.fragment_rank

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        scores = (arguments?.getSerializable(SCORES) as BundleScore).scores

        SoundUtils.play("rank_background")

        rankNames = mutableListOf(rankText1, rankText2, rankText3)
        rankScores = mutableListOf(rankScore1, rankScore2, rankScore3)
        rankAvatars = mutableListOf(rankAvatar1, rankAvatar2, rankAvatar3)

        val rank = sort(scores)

        Flowable.timer(500,TimeUnit.MILLISECONDS)
                .mySubscribe {
                    for (i in 0 until min(3, rank.size)) {
                        rankNames[i].text = rank[i].student.name
                        rankScores[i].text = "${rank[i].score}分"
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