package com.prance.lib.common.utils.http.cookie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bingbing on 2017/6/16.
 */

public class CookieDaoHelper extends SQLiteOpenHelper {

    public CookieDaoHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL
                ("CREATE TABLE IF NOT EXISTS t_global_data("
                        + "id integer primary key autoincrement,"
                        + "key varchar,"
                        + "m_str varchar)"
                );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
