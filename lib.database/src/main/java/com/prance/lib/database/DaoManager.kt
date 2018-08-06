package com.prance.lib.database

import android.content.Context

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils

import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.query.QueryBuilder

/**
 * 创建数据库、创建数据库表、包含增删改查的操作以及数据库的升级
 *
 * @author shenbingbing
 * @date 2017/5/5
 */

object DaoManager {

    private val DB_NAME = "shuangshi"

    var daoSession: DaoSession? = null
        private set

    /**
     * 在Application中调用,初始化数据库
     *
     * @param context
     */
    fun init(context: Context) {
        if (daoSession == null) {
            val helper = DaoHelper(context, DB_NAME, null)
            val db = helper.writableDb
            val daoMaster = DaoMaster(db)
            daoSession = daoMaster.newSession()
        }
    }


}