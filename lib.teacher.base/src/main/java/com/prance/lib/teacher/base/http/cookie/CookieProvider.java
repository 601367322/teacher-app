package com.prance.lib.teacher.base.http.cookie;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by bingbing on 2016/12/18.
 */

public class CookieProvider extends ContentProvider {

    public static final String COOKIE_DATABASE_NAME = "PranceCookiePrefsFile.db";
    public static final String TABLE_NAME = "t_global_data";
    public static final String KEY = "key";
    public static final String VALUE = "m_str";
    public static final String AUTHORITY = "com.prance.teacher";
    public static final String PATH = "cookie";
    public static final int CODE = 1;

    public static final Uri URI_GET_ALL_COOKIE = Uri.parse("content://" + AUTHORITY + "/" + PATH);

    private static final UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, PATH, CODE);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        CookieDaoHelper helper = new CookieDaoHelper(getContext(), COOKIE_DATABASE_NAME, null, 1);
        db = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (mUriMatcher.match(uri)) {
            case CODE:
                return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (mUriMatcher.match(uri)) {
            case CODE:
                db.insert(TABLE_NAME, null, values);
                break;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case CODE:
                db.delete(TABLE_NAME, selection, selectionArgs);
                break;
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
