package com.prance.teacher.features.welcome

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.http.ResultException
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.check.CheckKeyPadActivity
import com.prance.teacher.features.common.NetErrorFragment
import com.prance.teacher.features.login.model.VersionEntity
import com.prance.teacher.features.login.view.UpdateFragment
import android.app.PendingIntent
import com.blankj.utilcode.util.Utils
import com.prance.teacher.services.KillerBroadcast
import java.util.*


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
        }

        //自动强行关闭应用定时，防止"不退出APP则无法更新APP"的情况。
        startKillAlarm()

        mPresenter.checkVersion()
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

    private val mKillHour = 5

    private fun startKillAlarm() {
        val now = Calendar.getInstance()
        val tomorrow = Calendar.getInstance()
        //如果是当天凌晨，小于${mKillHour}点，则还是定到今天，否则定到明天
        if (now.get(Calendar.HOUR_OF_DAY) > mKillHour) {
            tomorrow.add(Calendar.DATE, 1)
        }
        tomorrow.set(Calendar.HOUR, mKillHour)
        tomorrow.set(Calendar.MINUTE, 0)
        tomorrow.set(Calendar.SECOND, 0)
        tomorrow.set(Calendar.MILLISECOND, 0)
        val diff = tomorrow.timeInMillis - now.timeInMillis
        LogUtils.i("距离自动关闭应用还有\t$diff")
        val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(KillerBroadcast.KILL)
        val pi = PendingIntent.getBroadcast(Utils.getApp(), 0, intent, 0)
        alarmManager.set(AlarmManager.RTC, tomorrow.timeInMillis, pi)
    }

}