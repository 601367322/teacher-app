package com.prance.lib.teacher.base

import android.annotation.SuppressLint
import android.app.Application
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.prance.lib.common.utils.ImageLoaderFactory
import com.prance.lib.common.utils.ModelUtil
import com.prance.lib.common.utils.http.OkHttpUtils
import com.prance.lib.database.DaoManager
import com.prance.lib.database.UserEntity
import com.prance.lib.third.inter.PluginsManager
import com.squareup.leakcanary.LeakCanary
import org.greenrobot.greendao.query.QueryBuilder


class TeacherApplication : Application() {

    var mUserInfo: UserEntity? = null

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
        LogUtils.getConfig().setLog2FileSwitch(true)

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

        QueryBuilder.LOG_SQL = true
        QueryBuilder.LOG_VALUES = true

        registerActivityLifecycleCallbacks(DefaultActivityLifecycleCallbacks())

    }

}
