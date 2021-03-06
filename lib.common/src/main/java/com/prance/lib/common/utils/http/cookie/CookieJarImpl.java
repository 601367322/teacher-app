package com.prance.lib.common.utils.http.cookie;


import com.prance.lib.common.utils.http.Exceptions;
import com.prance.lib.common.utils.http.cookie.store.CookieStore;
import com.prance.lib.common.utils.http.cookie.store.HasCookieStore;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 *
 * @author shenbingbing
 * @date 16/4/22
 */
public class CookieJarImpl implements CookieJar, HasCookieStore {
    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore) {
        if (cookieStore == null) {
            Exceptions.INSTANCE.illegalArgument("cookieStore can not be null.");
        }
        this.cookieStore = cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.add(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.get(url);
    }

    @Override
    public CookieStore getCookieStore() {
        return cookieStore;
    }
}
