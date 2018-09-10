package com.prance.teacher.features.pk.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.prance.lib.base.extension.visible
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.pk.model.PKResult
import com.prance.teacher.features.pk.presenter.PKPresenter
import kotlinx.android.synthetic.main.fragment_pk_rank.*
import kotlinx.android.synthetic.main.layout_pk_rank_background.*

class PKRankFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_pk_rank

    companion object {

        const val PK_RESULT = "PKResult"

        fun forPKResult(pkResult: PKResult): PKRankFragment {
            var fragment = PKRankFragment()
            val bundle = Bundle()
            bundle.putSerializable(PK_RESULT, pkResult)
            fragment.arguments = bundle
            return fragment
        }
    }

    var numbers: MutableList<View> = mutableListOf()
    var classNames: MutableList<TextView> = mutableListOf()
    var correctRates: MutableList<TextView> = mutableListOf()
    var avgTimes: MutableList<TextView> = mutableListOf()
    var scores: MutableList<LinearLayout> = mutableListOf()
    val scoreRes = mutableMapOf(
            "+" to R.drawable.red_package_score_add,
            "0" to R.drawable.red_package_score_0,
            "1" to R.drawable.red_package_score_1,
            "2" to R.drawable.red_package_score_2,
            "3" to R.drawable.red_package_score_3,
            "4" to R.drawable.red_package_score_4,
            "5" to R.drawable.red_package_score_5,
            "6" to R.drawable.red_package_score_6,
            "7" to R.drawable.red_package_score_7,
            "8" to R.drawable.red_package_score_8,
            "9" to R.drawable.red_package_score_9
    )
    val rankRes = mutableMapOf(
            "0" to R.drawable.rank_number_0,
            "1" to R.drawable.rank_number_1,
            "2" to R.drawable.rank_number_2,
            "3" to R.drawable.rank_number_3,
            "4" to R.drawable.rank_number_4,
            "5" to R.drawable.rank_number_5,
            "6" to R.drawable.rank_number_6,
            "7" to R.drawable.rank_number_7,
            "8" to R.drawable.rank_number_8,
            "9" to R.drawable.rank_number_9
    )

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        numbers.add(number1)
        numbers.add(number2)
        numbers.add(number3)

        classNames.add(className1)
        classNames.add(className2)
        classNames.add(className3)

        correctRates.add(correctRate1)
        correctRates.add(correctRate2)
        correctRates.add(correctRate3)

        avgTimes.add(avgTime1)
        avgTimes.add(avgTime2)
        avgTimes.add(avgTime3)

        scores.add(scoreLayout1)
        scores.add(scoreLayout2)
        scores.add(scoreLayout3)

        val result = arguments?.getSerializable(PK_RESULT) as PKResult?

        var myNumber: Int = 0
        var myClass = ClassesDetailFragment.mClassesEntity

        if (BuildConfig.DEBUG) {
//            if (myClass == null) {
//                myClass = ClassesEntity(1, "第一名")
//            }
        }

        var classVO: PKPresenter.PKResultMessage.ClassVO? = null

        result?.let {
            it.orderClasses?.let {
                for ((index, i) in it.withIndex()) {
                    if (index < numbers.size) {
                        numbers[index].visible()
                        classNames[index].text = i.klass?.name
                        correctRates[index].text = "${i.correctRate}%"
                        avgTimes[index].text = i.averageTime.toString()
                        result.getScoreByNumber(index)?.let {
                            generateScoreImage(scores[index], it)
                        }
                    }
                    if (i.klass?.id == myClass?.klass?.id) {
                        myNumber = index + 1
                        classVO = i
                    }
                }
            }
        }

        myClass?.let {
            number4.visible()

            className4.text = myClass.klass?.name
            if (classVO == null) {
                classVO = PKPresenter.PKResultMessage.ClassVO(null, 0F, 0F)
            }
            correctRate4.text = "${classVO?.correctRate}%"
            avgTime4.text = classVO?.averageTime.toString()
            for (c in myNumber.toString()) {
                val image = ImageView(context)
                rankRes[c.toString()]?.let { image.setImageResource(it) }
                val layoutParams = LinearLayout.LayoutParams(resources.getDimensionPixelOffset(R.dimen.m110_0), resources.getDimensionPixelOffset(R.dimen.m110_0))
                image.layoutParams = layoutParams
                rankNumberLayout.addView(image)
            }
        }


    }

    fun generateScoreImage(scoreLayout: LinearLayout, score: Int) {
        for (c in score.toString()) {
            val image = ImageView(context)
            scoreRes[c.toString()]?.let { image.setImageResource(it) }
            val layoutParams = LinearLayout.LayoutParams(resources.getDimensionPixelOffset(R.dimen.m48_0), resources.getDimensionPixelOffset(R.dimen.m48_0))
            image.layoutParams = layoutParams
            scoreLayout.addView(image)
        }
    }
}