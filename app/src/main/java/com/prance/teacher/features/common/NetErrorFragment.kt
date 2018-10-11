package com.prance.teacher.features.common

import android.content.Context
import android.os.Bundle
import android.view.View
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import kotlinx.android.synthetic.main.fragment_neterror.*
import java.io.Serializable

class NetErrorFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_neterror

    companion object {

        const val RETRY = "retry"

        fun callIntent(retry: Retry): NetErrorFragment {
            val fragment = NetErrorFragment()
            val bundle = Bundle()
            bundle.putSerializable(RETRY, retry)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        var retryBean = arguments?.getSerializable(RETRY) as Retry

        retry.setOnClickListener {
            activity?.run { retryBean.onRetry?.invoke(this) }

        }
    }

    class Retry(var onRetry: ((context: Context) -> Unit)? = null) : Serializable

}