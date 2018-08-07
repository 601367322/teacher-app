package com.prance.lib.teacher.base

import android.annotation.SuppressLint
import android.app.Application
import cn.sunars.sdk.SunARS
import com.blankj.utilcode.util.*
import com.prance.lib.third.inter.PluginsManager
import com.squareup.leakcanary.LeakCanary
import com.prance.lib.common.utils.ImageLoaderFactory
import com.prance.lib.common.utils.ModelUtil
import com.prance.lib.database.BaseStationEntity
import com.prance.lib.database.DaoManager
import com.prance.lib.database.UserEntity
import com.prance.lib.common.utils.http.OkHttpUtils
import org.greenrobot.greendao.query.QueryBuilder


class TeacherApplication : Application(), SunARS.SunARSListener {

    var mUserInfo: UserEntity? = null

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
         * 日志
         */
        if (!ModelUtil.isTestModel)
            LogUtils.getConfig().setLogSwitch(false)

        /**
         * 注册生命周期
         */
        val lifes = PluginsManager.teacher?.getLifecycle()
        lifes?.let {
            for (life in it) {
                registerActivityLifecycleCallbacks(life)
            }
        }

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

        if (ModelUtil.isTestModel) {
            QueryBuilder.LOG_SQL = true
            QueryBuilder.LOG_VALUES = true
        }

        registerActivityLifecycleCallbacks(DefaultActivityLifecycleCallbacks())

        //没有基站时，测试开发使用
//        if (BuildConfig.DEBUG)
//            mBaseStation = BaseStationEntity(0, "test")
    }


    override fun onConnectEventCallBack(iBaseID: Int, iMode: Int, sInfo: String?) {
        when (iMode) {
            SunARS.BaseStation_Connected_Model -> {
                when (sInfo) {
                    SunARS.BaseStation_Connected -> {
                        //基站识别ID
                        SunARS.readHDParam(0, SunARS.BaseStation_ID)
                        //读取基站信息
                        SunARS.readHDParam(0, SunARS.BaseStation_Channel)
                    }
                    SunARS.BaseStation_DisConnected -> {
                        mBaseStation = BaseStationEntity()
                    }
                }
            }
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
    }

    override fun onVoteEventCallBack(iBaseID: Int, iMode: Int, sInfo: String?) {
    }

    override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
    }

    override fun onStaEventCallBack(sInfo: String?) {
    }

    override fun onLogEventCallBack(sInfo: String?) {
    }

    override fun onDataTxEventCallBack(sendData: ByteArray?, dataLen: Int) {
    }

}
