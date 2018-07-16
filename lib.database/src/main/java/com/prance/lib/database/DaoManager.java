package com.prance.lib.database;

import android.content.Context;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

/**
 * 创建数据库、创建数据库表、包含增删改查的操作以及数据库的升级
 *
 * @author shenbingbing
 * @date 2017/5/5
 */

public class DaoManager {

    private static final String DB_NAME = "shuangshi";

    private static DaoSession daoSession;

    /**
     * 在Application中调用,初始化数据库
     *
     * @param context
     */
    public static void init(Context context) {
        if (daoSession == null) {
            DaoHelper helper = new DaoHelper(context, DB_NAME, null);
            Database db = helper.getWritableDb();
            DaoMaster daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }


}