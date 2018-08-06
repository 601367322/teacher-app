package com.prance.lib.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase

import org.greenrobot.greendao.database.Database

class DaoHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?) : DaoMaster.OpenHelper(context, name, factory) {

    /**
     * 更新数据库
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
        if (oldVersion <= 1) {
            MessageEntityDao.createTable(db, true)
        }
    }

}
