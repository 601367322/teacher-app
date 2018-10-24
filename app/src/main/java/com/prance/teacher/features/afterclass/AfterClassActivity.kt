package com.prance.teacher.features.afterclass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.Constants.FEED_BACK
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.core.OnStartClassActivity
import com.prance.teacher.features.afterclass.view.AfterClassFragment
import com.prance.teacher.features.classes.view.ClassesDetailFragment


class AfterClassActivity : BaseActivity(), OnStartClassActivity {
    var mFeedBack: ClassesDetailFragment.Question? = null
    var mFragment: AfterClassFragment? = null

    companion object {
        fun callingIntent(context: Context, fb: ClassesDetailFragment.Question): Intent {
            val intent = Intent(context, AfterClassActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            intent.putExtra(FEED_BACK, fb)
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
        mFeedBack = intent?.getSerializableExtra(FEED_BACK) as ClassesDetailFragment.Question?
        val bundle = Bundle()
        bundle.putSerializable(FEED_BACK, mFeedBack)
        if (mFragment!!.arguments != null) {
            mFragment!!.arguments?.putAll(bundle)
        } else {
            mFragment!!.arguments = bundle
        }
    }
}