package com.xiaoyv.flexiflix.extension.impl.java.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Class: [CommonInterceptor]
 *
 * @author why
 * @since 11/24/23
 */
class CommonInterceptor : Interceptor {
    private val methodGet = "GET"

    private val defaultUserAgent =
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:120.0) Gecko/20100101 Firefox/120.0"

    /**
     * 系统的 UA
     * Dalvik/2.1.0 Mozilla (Linux; U; Android 13; Pixel 4 XL Build/TP1A.221005.002)
     *
     * Bgm 推荐的 UA
     * - "xiaoyvyv/bangumi/${AppUtils.getAppVersionName()} (Android) (https://github.com/xiaoyvyv/bangumi)"
     * - 部分图片服务商禁止 Dalvik，这里替换一下
     */
    private val userAgent: String by lazy {
        System.getProperty("http.agent", defaultUserAgent).orEmpty()
            .replace("(.*?)\\(".toRegex(), "Mozilla/5.0 (")
    }

    private val Request.isNotGet
        get() = method.equals(methodGet, true).not()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return chain.proceed(
            request
                .newBuilder()
                .removeHeader("User-Agent")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.6,zh-HK;q=0.4,en;q=0.2")
                .addHeader("User-Agent", userAgent)
                .addHeader("Cookie", "kira=1")
                .let {
                    if (request.header("Referer").isNullOrBlank().not() || request.isNotGet) it
                    else it.addHeader("Referer", request.url.toString())
                }
                .build()
        )
    }
}