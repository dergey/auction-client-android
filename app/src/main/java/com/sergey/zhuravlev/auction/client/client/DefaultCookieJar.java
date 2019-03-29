package com.sergey.zhuravlev.auction.client.client;

import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class DefaultCookieJar implements CookieJar {

    private Map<HttpUrl, List<Cookie>> cookies;

    public DefaultCookieJar() {
        cookies = new HashMap<>();
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        Log.d("DefaultCookieJar", "saveFromResponse: " + url + " " + Arrays.toString(cookies.toArray()));
        this.cookies.put(url, cookies);
    }


    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = this.cookies.get(url);
        if (cookies == null) return Collections.emptyList();
        return cookies;
    }
}
