package com.prance.teacher.features.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.http.ResultException
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.core.OnStartClassActivity
import com.prance.teacher.features.check.CheckKeyPadActivity
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.common.NetErrorFragment
import com.prance.teacher.features.login.model.VersionEntity
import com.prance.teacher.features.login.view.UpdateFragment
import com.prance.teacher.features.pk.PKActivity
import com.prance.teacher.features.students.model.StudentEntity
import com.prance.teacher.features.subject.SubjectActivity
import com.prance.teacher.features.subject.SubjectRankActivity
import com.prance.teacher.features.subject.view.SubjectRankFragment
import io.reactivex.Flowable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.TimeUnit

class WelcomeActivity : BaseActivity(), IWelcomeContract.View {

    override fun getContext(): Context = this

    override var mPresenter: IWelcomeContract.Presenter = WelcomePresenter()

    override fun initView(savedInstanceState: Bundle?) {
        //防止重复启动
        if (!this.isTaskRoot) {
            val mainIntent = intent
            val action = mainIntent.action
            if ((mainIntent.hasCategory(Intent.CATEGORY_LEANBACK_LAUNCHER) || mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER)) && action == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }
        super.initView(savedInstanceState)

        inited()

        if (BuildConfig.DEBUG) {
//            var question = ClassesDetailFragment.Question(1, 10, "1,0,0,0,4,1", 1, "A", 200)
//            startActivity(SubjectActivity.callingIntent(this, question))
//
//            var question = ClassesDetailFragment.Question(1, 10, "1,0,0,0,4,1", 1, "A", 200)
//            startActivity(PKActivity.callingIntent(this, question))
//            finish()
//            return

            EventBus.getDefault().register(this)

            Flowable.interval(0, 10, TimeUnit.SECONDS)
                    .mySubscribe {
                        for (activity in ActivityUtils.getActivityList()) {
                            if (activity is OnStartClassActivity) {
                                activity.finish()
                            }
                        }
                        var question = ClassesDetailFragment.Question(1, 10, "1,0,0,0,4,1", 1, "A", 200)
                        startActivity(SubjectActivity.callingIntent(this, question))
                    }

            return
        }
        mPresenter.checkVersion()
    }


    @Subscribe
    fun onEvent(result: SubjectRankFragment.QuestionResult) {
        if (BuildConfig.DEBUG) {

                startActivity(SubjectRankActivity.callingIntent(this, SubjectRankFragment.QuestionResult(
                        1, mutableListOf(
                        StudentEntity("呵呵", "https://upload.jianshu.io/users/upload_avatars/2694946/47cc8e69-2c02-4781-bb4a-43b2c415b4a8.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240"),
                        StudentEntity("呵呵", "https://upload.jianshu.io/users/upload_avatars/2694946/47cc8e69-2c02-4781-bb4a-43b2c415b4a8.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240"),
                        StudentEntity("呵呵", "https://upload.jianshu.io/users/upload_avatars/2694946/47cc8e69-2c02-4781-bb4a-43b2c415b4a8.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240"),
                        StudentEntity("呵呵", "https://upload.jianshu.io/users/upload_avatars/2694946/47cc8e69-2c02-4781-bb4a-43b2c415b4a8.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240"),
                        StudentEntity("呵呵", "https://upload.jianshu.io/users/upload_avatars/2694946/47cc8e69-2c02-4781-bb4a-43b2c415b4a8.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240"),
                        StudentEntity("呵呵", "https://upload.jianshu.io/users/upload_avatars/2694946/47cc8e69-2c02-4781-bb4a-43b2c415b4a8.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240")
                )
                )))
            return
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    override fun fragment(): BaseFragment = WelcomeFragment()

    override fun checkVersionCallBack(versionEntity: VersionEntity) {
        //显示更新提示
        versionEntity.let {
            if (versionEntity.codeVersion > AppUtils.getAppVersionCode()) {
                var updateFragment: UpdateFragment? = null
                for (fragment in supportFragmentManager.fragments) {
                    if (fragment is UpdateFragment) {
                        updateFragment = fragment
                    }
                }
                updateFragment?.startUpdate(versionEntity)
                        ?: supportFragmentManager?.inTransaction {
                            replace(R.id.fragmentContainer, UpdateFragment.forVersion(it))
                        }
            } else {
                nextStep()
            }
        }
    }

    override fun onNetworkError(throwable: Throwable): Boolean {
        if (throwable is ResultException) {
            nextStep()
            return true
        }
        supportFragmentManager.inTransaction {
            replace(R.id.fragmentContainer, NetErrorFragment.callIntent {
                retry()
                mPresenter.checkVersion()
            })
        }
        return true
    }

    private fun nextStep() {
        startActivity(CheckKeyPadActivity.callingIntent(this))
        finish()
    }
}