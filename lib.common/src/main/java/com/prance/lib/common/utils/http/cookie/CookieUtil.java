package com.prance.lib.common.utils.http.cookie;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bingbing on 2017/6/16.
 */

public class CookieUtil {

    ContentResolver mResolver;

    public CookieUtil(ContentResolver resolver) {
        this.mResolver = resolver;
    }

    public void addCookie(String key, String value) {
        ContentValues values = new ContentValues();
        values.put(CookieProvider.KEY, key);
        values.put(CookieProvider.VALUE, value);
        mResolver.insert(CookieProvider.URI_GET_ALL_COOKIE, values);
    }


    public void deleteCookie(String key) {
        mResolver.delete(CookieProvider.URI_GET_ALL_COOKIE, CookieProvider.KEY + "=?", new String[]{key});
    }

    public void deleteAllCookie() {
        mResolver.delete(CookieProvider.URI_GET_ALL_COOKIE, null, null);
    }

    public Map<String, String> getCookiesStore() {
        Cursor cursor = mResolver.query(CookieProvider.URI_GET_ALL_COOKIE,
                null, null, null, null);

        Map<String, String> prefsMap = new HashMap<>();

        try {
            while (cursor.moveToNext()) {
                prefsMap.put(cursor.getString(1), cursor.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return prefsMap;
    }
}
