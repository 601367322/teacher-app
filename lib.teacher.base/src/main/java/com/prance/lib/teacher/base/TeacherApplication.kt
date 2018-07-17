package com.prance.lib.teacher.base

import android.annotation.SuppressLint
import android.app.Application
import cn.sunars.sdk.SunARS
import com.blankj.utilcode.util.CrashUtils
import com.blankj.utilcode.util.Utils
import com.prance.lib.common.utils.UrlUtil
import com.prance.lib.third.inter.PluginsManager
import com.squareup.leakcanary.LeakCanary
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.common.utils.ImageLoaderFactory
import com.prance.lib.common.utils.ModelUtil
import com.prance.lib.database.BaseStationEntity
import com.prance.lib.database.DaoManager
import com.prance.lib.database.UserEntity
import com.prance.lib.teacher.base.http.OkHttpUtils


class TeacherApplication : Application(), SunARS.SunARSListener {

    lateinit var mUserInfo: UserEntity

    var mBaseStation: BaseStationEntity = BaseStationEntity()

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()

        /**
         * LeakCanary初始化
         */
        if (BuildConfig.DEBUG) LeakCanary.install(this)

        /**
         * 工具初始化
         */
        Utils.init(this)

        /**
         * 测试模式下，抓崩溃日志
         */
        if (!BuildConfig.DEBUG && ModelUtil.isTestModel)
            CrashUtils.init(UrlUtil.getBaseCrashDir()) { crashInfo, e ->
                LogUtils.e(crashInfo)
                AppUtils.relaunchApp()
            }

        /**
         * 测试开关生命周期
         */
//        registerActivityLifecycleCallbacks(PluginsManager.testSetting?.testSettingActivityLifeManager)

        /**
         * Bugly初始化
         */
        PluginsManager.bugly?.init()

        /**
         * 初始化图片加载器
         */
        ImageLoaderFactory.init(OkHttpUtils.instance.mOkHttpClient)

        /**
         * 初始化数据库
         */
        DaoManager.init(this)

        /**
         * 基站监控
         */
        SunARS.addListener(this)
    }


    override fun onConnectEventCallBack(iBaseID: Int, iMode: Int, sInfo: String?) {
        if (sInfo == SunARS.BaseStation_Connected) {
            //基站识别ID
            SunARS.readHDParam(0, SunARS.BaseStation_ID)
            //读取基站信息
            SunARS.readHDParam(0, SunARS.BaseStation_Channel)
        }
    }

    override fun onHDParamCallBack(iBaseID: Int, iMode: Int, sInfo: String?) {
        when (iMode) {
            SunARS.BaseStation_ID -> {
                mBaseStation.stationId = sInfo?.toInt()
            }
            SunARS.BaseStation_Channel -> {
                mBaseStation.stationChannel = sInfo?.toLong()
            }
        }
    }

    override fun onHDParamBySnCallBack(KeySn: String?, iMode: Int, sInfo: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVoteEventCallBack(iBaseID: Int, iMode: Int, sInfo: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onKeyEventCallBack(KeyID: String?, iMode: Int, Time: Float, sInfo: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStaEventCallBack(sInfo: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLogEventCallBack(sInfo: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataTxEventCallBack(sendData: ByteArray?, dataLen: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
