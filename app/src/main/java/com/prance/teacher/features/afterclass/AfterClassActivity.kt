package com.prance.teacher.features.afterclass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.afterclass.model.FeedBack
import com.prance.teacher.features.afterclass.view.AfterClassFragment


class AfterClassActivity : BaseActivity() {
    var mFeedBack: FeedBack? = null
    var mFragment: AfterClassFragment? = null

    companion object {
        const val feedback: String = "feedback"
        fun callingIntent(context: Context, fb: FeedBack): Intent {
            val intent = Intent(context, AfterClassActivity::class.java)
            intent.putExtra(feedback, fb)
            return intent
        }
    }

    override fun fragment(): BaseFragment {
        if (mFragment == null) {
            mFragment = AfterClassFragment()
        }
        return mFragment!!
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mFeedBack = intent?.getSerializableExtra(AfterClassActivity.feedback) as FeedBack?
        val bundle = Bundle()
        bundle.putSerializable(feedback, mFeedBack)
        if (mFragment!!.arguments != null) {
            mFragment!!.arguments?.putAll(bundle)
        } else {
            mFragment!!.arguments = bundle
        }
    }
}