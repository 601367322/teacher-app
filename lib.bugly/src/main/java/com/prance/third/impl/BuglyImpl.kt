package com.prance.third.impl


import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ProcessUtils
import com.blankj.utilcode.util.Utils
import com.prance.lib.common.utils.Constants
import com.prance.lib.common.utils.Constants.APP_CHANNEL
import com.prance.lib.common.utils.Constants.BUGLY_APPID
import com.prance.lib.common.utils.MetaDataUtil
import com.prance.lib.common.utils.ModelUtil
import com.prance.lib.third.inter.IBugly
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import android.widget.Toast
import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils.startActivity
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.bugly.BuildConfig
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.UpgradeInfo
import com.tencent.bugly.beta.upgrade.UpgradeListener
import com.tencent.bugly.beta.Beta.upgradeListener
import com.tencent.bugly.beta.upgrade.UpgradeStateListener


/**
 * Created by shenbingbing on 16/7/29.
 */

class BuglyImpl : IBugly {

    override fun init() {

        if (BuildConfig.DEBUG)
            Beta.upgradeListener = UpgradeListener { ret, strategy, isManual, isSilence ->
                if (strategy != null) {
                }
            }

        /* 设置更新状态回调接口 */
        Beta.upgradeStateListener = object : UpgradeStateListener {

            override fun onDownloadCompleted(p0: Boolean) {
                LogUtils.d("onDownloadCompleted")
            }

            override fun onUpgradeSuccess(isManual: Boolean) {
                LogUtils.d("onUpgradeSuccess")
            }

            override fun onUpgradeFailed(isManual: Boolean) {
                LogUtils.d("onUpgradeFailed")
            }

            override fun onUpgrading(isManual: Boolean) {
                LogUtils.d("onUpgrading")
            }

            override fun onUpgradeNoVersion(isManual: Boolean) {
                LogUtils.d("onUpgradeNoVersion")
            }
        }

        val strategy = CrashReport.UserStrategy(Utils.getApp())
        strategy.appChannel = MetaDataUtil.getMetaDataInApp(APP_CHANNEL)
        strategy.appVersion = AppUtils.getAppVersionName()
        strategy.appPackageName = AppUtils.getAppPackageName()
        // 获取当前进程名
        val processName = ProcessUtils.getCurrentProcessName()
        // 设置是否为上报进程
        strategy.isUploadProcess = processName == null || processName == AppUtils.getAppPackageName()

        Bugly.init(Utils.getApp(), MetaDataUtil.getMetaDataInApp(BUGLY_APPID), ModelUtil.isTestModel, strategy)
    }

    override fun checkUpdate() {
        Beta.checkUpgrade()
    }
}
