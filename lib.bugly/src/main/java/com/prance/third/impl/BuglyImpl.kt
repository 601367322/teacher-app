package com.prance.third.impl


import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ProcessUtils
import com.blankj.utilcode.util.Utils
import com.prance.lib.common.utils.Constants
import com.prance.lib.common.utils.MetaDataUtil
import com.prance.lib.common.utils.ModelUtil
import com.prance.lib.third.inter.IBugly
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport

/**
 * Created by shenbingbing on 16/7/29.
 */

class BuglyImpl : IBugly {

    override fun init() {
        val strategy = CrashReport.UserStrategy(Utils.getApp())
        strategy.appVersion = AppUtils.getAppVersionName()
        strategy.appPackageName = AppUtils.getAppPackageName()
        // 获取当前进程名
        val processName = ProcessUtils.getCurrentProcessName()
        // 设置是否为上报进程
        strategy.isUploadProcess = processName == null || processName == AppUtils.getAppPackageName()

        Bugly.init(Utils.getApp(), MetaDataUtil.getMetaDataInApp(Constants.BUGLY_APPID), ModelUtil.isTestModel, strategy)
    }


}
