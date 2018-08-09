package com.prance.teacher.features.redpackage.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.subject.SubjectActivity
import com.prance.teacher.features.subject.view.SubjectOnStartFragment

/**
 *Created by rich on 2018/8/8
 */

class RankFragment: BaseFragment() {

    var rank: Array<Int>? = null

    companion object {
        const val RANK = "rank"
        fun create(rank: Array<Int>):RankFragment {
            var fragment = RankFragment()
            val bundle = Bundle()
            bundle.putSerializable(RANK, rank)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_rank

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        rank = arguments?.getSerializable(RANK) as Array<Int>
        rootView.findViewById<TextView>(R.id.tv1).setText(rank!![3].toString())
        rootView.findViewById<TextView>(R.id.tv1).setText(rank!![2].toString())
        rootView.findViewById<TextView>(R.id.tv1).setText(rank!![1].toString())
    }
}