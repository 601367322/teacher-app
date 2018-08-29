package com.prance.teacher.features.redpackage

import android.content.Context
import android.content.Intent
import com.prance.lib.database.MessageEntity
import android.os.Bundle
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.socket.MessageListener
import com.prance.lib.socket.PushService
import com.prance.lib.socket.PushServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.model.RedPackageSetting
import com.prance.teacher.features.redpackage.model.StudentScore
import com.prance.teacher.features.redpackage.view.RedPackageRankFragment
import com.prance.teacher.features.redpackage.view.RedPackageFragment

class RedPackageActivity : BaseActivity(), MessageListener {

    /**
     * 下发的抢红包设置
     */
    var mSetting: RedPackageSetting? = null

    private val mPushServicePresenterPresenter by lazy { PushServicePresenter(this, this) }

    /**
     * 抢红包fragment
     */
    var mGrabFragment: RedPackageFragment? = null

    companion object {
        fun callingIntent(context: Context, redPackage: RedPackageSetting): Intent {
            val intent = Intent(context, RedPackageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            intent.putExtra(RedPackageFragment.mSetTing, redPackage)
            return intent
        }
    }

    override fun fragment(): BaseFragment {
        if (mGrabFragment == null) {
            mGrabFragment = RedPackageFragment()
        }
        return mGrabFragment!!
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        mSetting = intent?.getSerializableExtra(RedPackageFragment.mSetTing) as RedPackageSetting?

        val bundle = Bundle()
        bundle.putSerializable(RedPackageFragment.mSetTing, mSetting)
        if (mGrabFragment!!.arguments != null) {
            mGrabFragment!!.arguments?.putAll(bundle)
        } else {
            mGrabFragment!!.arguments = bundle
        }

        mPushServicePresenterPresenter.bind()


        if (BuildConfig.DEBUG) {
//            Flowable.timer(3, TimeUnit.SECONDS)
//                    .subscribe {
//                        redPackageRank(mutableListOf(
//                                StudentScore(StudentsEntity("申兵兵", "http://cdn.aixifan.com/acfun-pc/2.4.13/img/logo.png"), 10, 5),
//                                StudentScore(StudentsEntity("申兵兵", "http://cdn.aixifan.com/acfun-pc/2.4.13/img/logo.png"), 10, 5),
//                                StudentScore(StudentsEntity("申兵兵", "http://cdn.aixifan.com/acfun-pc/2.4.13/img/logo.png"), 10, 5),
//                                StudentScore(StudentsEntity("申兵兵", "http://cdn.aixifan.com/acfun-pc/2.4.13/img/logo.png"), 10, 5)
//                        ))
//                    }
        }
    }

    override fun onMessageResponse(msg: MessageEntity): Boolean {
        when (msg.cmd) {
            PushService.END_INTERACTN -> {
                mGrabFragment!!.redPackageStop()
                finish()
            }
        }
        return super.onMessageResponse(msg)
    }

    override fun onServiceStatusConnectChanged(statusCode: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()

        mPushServicePresenterPresenter.unBind()
    }

    fun redPackageRank(scores: MutableList<StudentScore>) {
        scores.let {
            supportFragmentManager.inTransaction {
                replace(R.id.fragmentContainer, RedPackageRankFragment.create(RedPackageRankFragment.Companion.BundleScore(it)))
            }
        }
    }
}