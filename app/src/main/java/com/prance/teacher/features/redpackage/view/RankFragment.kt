package com.prance.teacher.features.redpackage.view

import android.os.Bundle
import android.view.View
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.model.StudentScore
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

class RankFragment : BaseFragment() {

    lateinit var scores: MutableList<StudentScore>

    var mDisposable: Disposable? = null

    var mTotalTime = 5

    companion object {
        const val SCORES = "scores"
        fun create(scores: BundleScore): RankFragment {
            var fragment = RankFragment()
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

        val rank = sort(scores)

        val builder = StringBuilder()
        for (i in 0 until min(3, rank.size)) {
            builder.append("第${i + 1}名：${rank[i].student.name}  ${rank[i].score}分\n")
        }

        rankText.text = builder.toString()

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