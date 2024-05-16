package com.xiaoyv.flexiflix.extension.impl.java.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * [AdBlockInterceptor] M3U8 去除广告等不连续片段
 *
 * ```m3u8
 * #EXTM3U
 * #EXT-X-VERSION:3
 * #EXT-X-TARGETDURATION:4
 * #EXT-X-PLAYLIST-TYPE:VOD
 * #EXT-X-MEDIA-SEQUENCE:0
 * #EXTINF:2.24,
 * 0000000.ts
 * #EXTINF:2.48,
 * 0000001.ts
 * #EXTINF:1.32,
 * 0000002.ts
 * #EXTINF:2.32,
 * 0000328.ts
 * #EXTINF:2,
 * 0000329.ts
 * #EXT-X-DISCONTINUITY
 * #EXTINF:4.12,
 * /video/adjump/time/17137142947010000000.ts
 * #EXTINF:3,
 * /video/adjump/time/17137142947020000001.ts
 * #EXTINF:2.6,
 * /video/adjump/time/17137142947020000002.ts
 * #EXTINF:2.72,
 * /video/adjump/time/17137142947020000003.ts
 * #EXTINF:3,
 * /video/adjump/time/17137142947020000004.ts
 * #EXTINF:1.8,
 * /video/adjump/time/17137142947020000005.ts
 * #EXT-X-DISCONTINUITY
 * #EXTINF:1.96,
 * 0000330.ts
 * 0000630.ts
 * #EXTINF:2.4,
 * 0000631.ts
 * #EXTINF:2,
 * 0000632.ts
 * #EXTINF:2,
 * 0000633.ts
 * #EXTINF:2,
 * 0000634.ts
 * #EXTINF:1.12,
 * 0000635.ts
 * #EXT-X-ENDLIST
 * ```
 *
 * @author why
 * @since 5/11/24
 */
class AdBlockInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // 只处理 m3u8
        val string = request.url.toString()
        if (!string.endsWith(".m3u8", true)) {
            return chain.proceed(request)
        }

        val response = chain.proceed(request)
        val responseBody = response.body
        val content = responseBody.string()
        val replace =
            content.replace("#EXT-X-DISCONTINUITY[\\s\\S]+#EXT-X-DISCONTINUITY\n".toRegex(), "")
        val newBody: ResponseBody = replace.toResponseBody(responseBody.contentType())

        return response.newBuilder()
            .body(newBody)
            .build()
    }
}