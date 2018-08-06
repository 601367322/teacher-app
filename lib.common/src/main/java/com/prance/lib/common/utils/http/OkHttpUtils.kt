package com.prance.lib.common.utils.http

import com.blankj.utilcode.util.Utils
import com.prance.lib.common.utils.ModelUtil
import com.prance.lib.common.utils.http.cookie.CookieJarImpl
import com.prance.lib.common.utils.http.cookie.store.PersistentCookieStore
import okhttp3.OkHttpClient

class OkHttpUtils private constructor() {

    var mOkHttpClient: OkHttpClient

    init {
        var mBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

        //公共参数
        mBuilder.addInterceptor(CommonParamsInterceptor())

        //cookie
        mBuilder.cookieJar(CookieJarImpl(PersistentCookieStore(Utils.getApp())))

        //证书
        val sslSocketFactory = HttpsUtils.getSslSocketFactory(null, null, null)
        mBuilder.sslSocketFactory(sslSocketFactory)

        //日志
        if (ModelUtil.isTestModel) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            mBuilder.addInterceptor(logging)

//            if (ModelUtil.isTestModel)
//                utils.addInterceptor(ChuckInterceptor(this))
        }

        mOkHttpClient = mBuilder.build()
    }

    companion object {
        val instance = OkHttpUtils()
    }

}