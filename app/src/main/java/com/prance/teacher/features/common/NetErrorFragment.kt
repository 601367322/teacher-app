package com.prance.teacher.features.common

import android.content.Context
import android.os.Bundle
import android.view.View
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.R.id.retry
import kotlinx.android.synthetic.main.fragment_neterror.*

class NetErrorFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_neterror

    private var onRetry: ((context: Context) -> Unit)? = null

    companion object {

        const val RETRY = "retry"

        fun callIntent(onRetry: ((context: Context) -> Unit)?): NetErrorFragment {
            val fragment = NetErrorFragment()
            fragment.onRetry = onRetry
            return fragment
        }
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        retry.setOnClickListener {
            activity?.run { onRetry?.invoke(this) }

        }
    }

}